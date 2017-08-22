package org.egov.citizen.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.egov.citizen.model.Demand;
import org.egov.citizen.model.DemandDetail;
import org.egov.citizen.model.DemandRequest;
import org.egov.citizen.model.SearchDemand;
import org.egov.citizen.model.ServiceCollection;
import org.egov.citizen.model.ServiceConfig;
import org.egov.citizen.model.ServiceConfigs;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqResponse;
import org.egov.citizen.model.ServiceResponse;
import org.egov.citizen.model.Value;
import org.egov.citizen.service.CitizenService;
import org.egov.citizen.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	public ServiceCollection serviceDefination;

	@Autowired
	public ServiceConfigs serviceConfigs;

	@Autowired
	public CitizenService citizenService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	public RestTemplate restTemplate;

	@PostMapping(value = "/_search")
	@ResponseBody
	public ResponseEntity<?> getService(@RequestBody RequestInfo requestInfo,
			@RequestParam(value = "tenantId", required = false) String tenantId) {
		ServiceResponse serviceRes = new ServiceResponse();
		serviceRes.setResponseInfo(new ResponseInfo());
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
		Object demands = citizenService.getResponse(url, config, results);
		servcieReq.setBackendServiceDetails(demands);
		citizenService.sendMessageToKafka(servcieReq);
		serviceReqResponse.setServiceReq(servcieReq);
		serviceReqResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(citizenService.getRequestInfo(config).getRequestInfo(), true));
		return new ResponseEntity<>(serviceReqResponse, HttpStatus.OK);
	}
	
	@PostMapping(value = "/requests/_update")
	public ResponseEntity<?> updateService(HttpEntity<String> httpEntity){
		
		String json = httpEntity.getBody();
		Object config = Configuration.defaultConfiguration().jsonProvider().parse(json);
		final ObjectMapper objectMapper = new ObjectMapper();
		ServiceReq servcieReq = objectMapper.convertValue(JsonPath.read(config, "$.serviceReq"), ServiceReq.class);

		List<ServiceConfig> list = serviceConfigs.getServiceConfigs();
		
		String url = "";
		String[] results=null;
		SearchDemand searchDemand = null;
		for (ServiceConfig serviceConfig : list) {
			if (serviceConfig.getServiceCode().equals(servcieReq.getServiceCode())) {
				searchDemand = serviceConfig.getSearchDemand();
				url = searchDemand.getCreateDemandRequest().getUrl();
				String demandRequest = searchDemand.getCreateDemandRequest().getDemandRequest();
				Object demand = Configuration.defaultConfiguration().jsonProvider().parse(demandRequest);
		
				
				
			}
		}

       return null;	
	}
	

}
