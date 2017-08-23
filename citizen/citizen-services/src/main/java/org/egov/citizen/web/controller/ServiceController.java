package org.egov.citizen.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.config.CitizenServiceConstants;
import org.egov.citizen.exception.CustomException;
import org.egov.citizen.model.RequestInfoWrapper;
import org.egov.citizen.model.SearchDemand;
import org.egov.citizen.model.ServiceCollection;
import org.egov.citizen.model.ServiceConfig;
import org.egov.citizen.model.ServiceConfigs;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqResponse;
import org.egov.citizen.model.ServiceResponse;
import org.egov.citizen.model.Value;
import org.egov.citizen.service.CitizenService;
import org.egov.citizen.web.contract.ReceiptRequest;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.citizen.web.contract.factory.ResponseInfoFactory;
import org.egov.citizen.web.errorhandlers.Error;
import org.egov.citizen.web.errorhandlers.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

@RestController
public class ServiceController {
	public static final Logger LOGGER = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	public ServiceCollection serviceDefination;

	@Autowired
	public ServiceConfigs serviceConfigs;

	@Autowired
	public CitizenService citizenService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	public RestTemplate restTemplate;

	@PostMapping(value = "/_search")
	public ResponseEntity<?> getService(@RequestBody @Valid RequestInfoWrapper requestInfo,
			@RequestParam(value = "tenantId", required = false) String tenantId) {
		ServiceResponse serviceRes = new ServiceResponse();
		serviceRes.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo.getRequestInfo(), true));
		serviceRes.setServices(serviceDefination.getServices());
		return new ResponseEntity<>(serviceRes, HttpStatus.OK);
	}

	@PostMapping(value = "/requests/_create")
	public ResponseEntity<?> createService(HttpEntity<String> httpEntity) {

		ServiceReqResponse serviceReqResponse = new ServiceReqResponse();

		String json = httpEntity.getBody();
		Object config = Configuration.defaultConfiguration().jsonProvider().parse(json);
		List<String> results = null;
		final ObjectMapper objectMapper = new ObjectMapper();
		ServiceReq servcieReq = objectMapper.convertValue(JsonPath.read(config, "$.serviceReq"), ServiceReq.class);
		LOGGER.info("servcieReq: "+servcieReq.toString());
		List<ServiceConfig> list = serviceConfigs.getServiceConfigs();
		String url = "";
		SearchDemand searchDemand = null;
		for (ServiceConfig serviceConfig : list) {
			if (serviceConfig.getServiceCode().equals(servcieReq.getServiceCode())) {
				searchDemand = serviceConfig.getSearchDemand();
				url = searchDemand.getUrl();
				results = searchDemand.getResult();
			}
		}
		List<Value> queryParamList = citizenService.getQueryParameterList(list, servcieReq.getServiceCode(), config);
		String sequenceNumber = citizenService.generateSequenceNumber(searchDemand, config);
		servcieReq.setServiceRequestId(sequenceNumber);
		url = citizenService.getUrl(url, queryParamList);
		LOGGER.info("config: "+config);
		Object demands = citizenService.generateResponseObject(url, citizenService.getRequestInfo(config), results);
		servcieReq.setBackendServiceDetails(demands);
		citizenService.sendMessageToKafka(servcieReq);
		serviceReqResponse.setServiceReq(servcieReq);
		serviceReqResponse.setResponseInfo(responseInfoFactory
				.createResponseInfoFromRequestInfo(citizenService.getRequestInfo(config).getRequestInfo(), true));
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/requests/_update")
	public ResponseEntity<?> updateService(HttpEntity<String> httpEntity) {

		String json = httpEntity.getBody();
		Object config = Configuration.defaultConfiguration().jsonProvider().parse(json);
		final ObjectMapper objectMapper = new ObjectMapper();
		ServiceReq servcieReq = objectMapper.convertValue(JsonPath.read(config, "$.serviceReq"), ServiceReq.class);

		List<ServiceConfig> list = serviceConfigs.getServiceConfigs();

		String url = "";
		SearchDemand searchDemand = null;
		for (ServiceConfig serviceConfig : list) {
			if (serviceConfig.getServiceCode().equals(servcieReq.getServiceCode())) {
				searchDemand = serviceConfig.getSearchDemand();
				long applicationFee= serviceConfig.getSearchDemand().getApplicationFee();
				url = searchDemand.getCreateDemandRequest().getUrl();
				String demandRequest = searchDemand.getCreateDemandRequest().getDemandRequest();

				StringBuilder builder = new StringBuilder();
				builder.append(searchDemand.getUrl()).append("?consumerCode="+servcieReq.getConsumerCode()+"&tenantId="+servcieReq.getTenantId());
				LOGGER.info("CONFIG: "+config.toString());
				RequestInfoWrapper requestInfo =  citizenService.getRequestInfo(config);
				Object response = restTemplate.postForObject(builder.toString(), requestInfo, Object.class);
				
				JSONObject jObject;

				try {
					jObject = new JSONObject(demandRequest);
					String configString = JsonPath.read(config, "$.requestInfo");
					jObject.put("RequestInfo", configString);

					for (Value value : servcieReq.getAttributeValues()) {
						// todo
							jObject.getJSONArray("Demands").getJSONObject(0).put("consumerCode", servcieReq.getConsumerCode());
							jObject.getJSONArray("Demands").getJSONObject(0).put("consumerType", "ConsumerType");
							jObject.getJSONArray("Demands").getJSONObject(0).put("tenantId", servcieReq.getTenantId());
							jObject.getJSONArray("Demands").getJSONObject(0).put("businessService",applicationProperties.getBusinessService());
							jObject.getJSONArray("Demands").getJSONObject(0).put("taxPeriodFrom", applicationProperties.getTaxPeriodFrom());
							jObject.getJSONArray("Demands").getJSONObject(0).put("taxPeriodTo", applicationProperties.getTaxPeriodTo());
					    	jObject.getJSONArray("Demands").getJSONObject(0).put("minimumAmountPayable",BigDecimal.valueOf(Long.valueOf(applicationFee)));
							jObject.getJSONArray("Demands").getJSONObject(0).getJSONArray("demandDetails").getJSONObject(0).put("taxHeadMasterCode", "PT_TAX");
							jObject.getJSONArray("Demands").getJSONObject(0).getJSONArray("demandDetails").getJSONObject(0).put("taxAmount",applicationFee);
							jObject.getJSONArray("Demands").getJSONObject(0).getJSONArray("demandDetails").getJSONObject(0).put("collectionAmount", "100");
							String demandsString = JsonPath.read(config, "$.requestInfo.userInfo.id");
							jObject.getJSONArray("Demands").getJSONObject(0).getJSONObject("owner").put("id",demandsString);
					}

					//citizenService.createDemand(url, jObject.toString());
					Object billRes = citizenService.generateBill(requestInfo, servcieReq.getConsumerCode(), 
							applicationProperties.getBusinessService(), servcieReq.getTenantId());
					LOGGER.info("Bills generated: "+billRes.toString());
					
					Object pgPayLoad = citizenService.generatePGPayload(requestInfo.getRequestInfo(), servcieReq.getConsumerCode(), 
							applicationProperties.getBusinessService(), servcieReq.getTenantId(), servcieReq.getServiceRequestId());
					LOGGER.info("PGPayLoad generated: "+pgPayLoad.toString());
					JSONObject backendServiceDetails = new JSONObject();
					backendServiceDetails.put("BillResponse", billRes);
					backendServiceDetails.put("PGPayload", pgPayLoad);
					
					servcieReq.setBackendServiceDetails(backendServiceDetails.toString());
					
				} catch (CustomException e) {
					Error error = new Error();
					error.setCode(e.getCode());
					error.setMessage(e.getCustomMessage());
					error.setDescription(e.getDescription());

					return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

				}
				 catch (Exception e) {
						return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);

				 }
			}
		}
		ServiceReqResponse serviceReqResponse = new ServiceReqResponse();
		serviceReqResponse.setServiceReq(servcieReq);
		serviceReqResponse.setResponseInfo(responseInfoFactory
				.createResponseInfoFromRequestInfo(citizenService.getRequestInfo(config).getRequestInfo(), true));
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);	}

	@PostMapping(value = "/requests/receipt/_create")
	public ResponseEntity<?> createReceipt(@RequestBody @Valid ReceiptRequest receiptReq, BindingResult errors) {

		if (receiptReq.getStatus().get(0).equals("FAILURE") || receiptReq.getStatus().get(0).equals("CANCEL")) {
			Error error = new Error();
			error.setCode(400);
			error.setMessage(CitizenServiceConstants.FAIL_STATUS_MSG);
			error.setDescription(CitizenServiceConstants.FAIL_STATUS_DESC);

			return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

		}
		ServiceReqResponse serviceReqResponse = new ServiceReqResponse();
		ServiceReq serviceReq = new ServiceReq();
		serviceReq.setBackendServiceDetails(receiptReq);
		serviceReqResponse.setServiceReq(serviceReq);

		/*
		 * String json = httpEntity.getBody(); Object config =
		 * Configuration.defaultConfiguration().jsonProvider().parse(json);
		 * List<String> results = null; final ObjectMapper objectMapper = new
		 * ObjectMapper(); ReceiptRequest receiptReq =
		 * objectMapper.convertValue(config, ReceiptRequest.class);
		 */

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("Request for receipt creation: " + receiptReq.toString());
		ServiceReq serviceRequest = null;
		try {
			serviceRequest = citizenService.createReceiptForPayment(receiptReq);
		} catch (CustomException e) {
			Error error = new Error();
			error.setCode(e.getCode());
			error.setMessage(e.getCustomMessage());
			error.setDescription(e.getDescription());

			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		serviceReqResponse.setServiceReq(serviceRequest);

		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}

	@PostMapping(value = "/requests/_search")
	@ResponseBody
	public ResponseEntity<?> getServiceRequests(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestInfoerrors,
			@ModelAttribute @Valid ServiceRequestSearchCriteria serviceRequestSearchCriteria,
			BindingResult errors) {
		
		if (requestInfoerrors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		
		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		List<ServiceReq> serviceRequests = new ArrayList<>();
		try{
			serviceRequests = citizenService.getServiceRequests(serviceRequestSearchCriteria);
		}catch(CustomException e){
			Error error = new Error();
			error.setCode(e.getCode());
			error.setMessage(e.getCustomMessage());
			error.setDescription(e.getDescription());
			
			return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);	
		}
		
		ServiceReqResponse serviceRes = new ServiceReqResponse();
		serviceRes.setServiceRequests(serviceRequests);
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
		serviceRes.setResponseInfo(responseInfo);
		return new ResponseEntity<>(serviceRes, HttpStatus.OK);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}
