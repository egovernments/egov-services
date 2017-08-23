package org.egov.citizen.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.config.CitizenServiceConstants;
import org.egov.citizen.exception.CustomException;
import org.egov.citizen.model.BillResponse;
import org.egov.citizen.model.BillingServiceRequestWrapper;
import org.egov.citizen.model.IdGenerationReqWrapper;
import org.egov.citizen.model.IdRequest;
import org.egov.citizen.model.InstrumentType;
import org.egov.citizen.model.RequestInfoWrapper;
import org.egov.citizen.model.SearchDemand;
import org.egov.citizen.model.ServiceConfig;
import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.Value;
import org.egov.citizen.producer.CitizenProducer;
import org.egov.citizen.repository.BillingServiceRepository;
import org.egov.citizen.repository.CitizenServiceRepository;
import org.egov.citizen.repository.CollectionRepository;
import org.egov.citizen.repository.ResponseRepository;
import org.egov.citizen.web.contract.Bill;
import org.egov.citizen.web.contract.BillDetail;
import org.egov.citizen.web.contract.Instrument;
import org.egov.citizen.web.contract.PGPayload;
import org.egov.citizen.web.contract.Receipt;
import org.egov.citizen.web.contract.ReceiptReq;
import org.egov.citizen.web.contract.ReceiptRequest;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jayway.jsonpath.JsonPath;


@Service
public class CitizenService {
	
	public static final Logger LOGGER = LoggerFactory
			.getLogger(CitizenService.class);
	
	@Autowired
	private CitizenProducer citizenProducer; 
	
	@Autowired
	private BillingServiceRepository billingServiceRepository;
	
	@Autowired
	private ResponseRepository responseRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private CitizenServiceRepository citizenServiceRepository;
	
	@Autowired
	private RestTemplate restTemaplate;

	public List<Value> getQueryParameterList(List<ServiceConfig> list, String serviceCode, Object config) {

		List<Value> list1 = new ArrayList<Value>();
		for (ServiceConfig serviceConfig : list) {

			if (serviceConfig.getServiceCode().equals(serviceCode)) {
				SearchDemand searchDemand = serviceConfig.getSearchDemand();
				List<String> queryParamList = searchDemand.getQueryAppend();

				for (String queryParam : queryParamList) {

					String[] value = queryParam.split(":");
					Value val = new Value();
					val.setKey(value[0].trim());
					val.setValue(JsonPath.read(config, value[1]));
					list1.add(val);
				}
			}
		}
		return list1;
	}

	public RequestInfoWrapper getRequestInfo(Object config) {

		RequestInfo requestInfo = new RequestInfo();

		RequestInfoWrapper infoWrapper = new RequestInfoWrapper();

		requestInfo.setAction(JsonPath.read(config, "$.requestInfo.action"));
		requestInfo.setApiId(JsonPath.read(config, "$.requestInfo.apiId"));
		requestInfo.setAuthToken(JsonPath.read(config, "$.requestInfo.authToken"));
		requestInfo.setVer(JsonPath.read(config, "$.requestInfo.ver"));
		requestInfo.setTs(Long.valueOf(JsonPath.read(config, "$.requestInfo.ts").toString()));
		requestInfo.setDid(JsonPath.read(config, "$.requestInfo.did"));
		requestInfo.setMsgId(JsonPath.read(config, "$.requestInfo.msgId"));
		requestInfo.setKey(JsonPath.read(config, "$.requestInfo.key"));
		User user = new User();
		user.setId(Long.valueOf(JsonPath.read(config, "$.requestInfo.userInfo.id")));
		requestInfo.setUserInfo(user);

		infoWrapper.setRequestInfo(requestInfo);

		return infoWrapper;
	}

	public String generateSequenceNumber(SearchDemand searchDemand, Object config) {

		IdRequest idRequest = searchDemand.getGenerateId().getRequest().getIdRequests().get(0);
		System.out.println("Expression: "+idRequest.getTenantId());
		Object tenantId = JsonPath.read(config, idRequest.getTenantId());
		idRequest.setTenantId(tenantId.toString());

		List<IdRequest> list = new ArrayList<IdRequest>();
		list.add(idRequest);
		IdGenerationReqWrapper reqWrapper = new IdGenerationReqWrapper();
		reqWrapper.setRequestInfo(getRequestInfo(config).getRequestInfo());
		reqWrapper.setIdRequests(list);

		Object response = new RestTemplate().postForObject(searchDemand.getGenerateId().getUrl(), reqWrapper,
				Object.class);

		Object generateId = JsonPath.read(response, searchDemand.getGenerateId().getResult());

		System.out.println("obje:  " + generateId);
		return generateId.toString();
	}
	
	public String getUrl(String url,List<Value> queryParamList){
		
		url = url + "?";
		for (Value val : queryParamList) {

			String url1 = val.getKey() + "=" + val.getValue() + "&";
			url = url + url1;
		}
		url = url.substring(0, url.length() - 1);
		
		return url;
	}
	
	public Object generateResponseObject(String url, Object config, List<String> results){
		
		RequestInfoWrapper requestInfo =  getRequestInfo(config);
		Object response = responseRepository.generateResponseObject(url, requestInfo, results);
		
		return response;
	}
	
	public Object createDemand(String url,String demand){
		
		
		Object response = restTemaplate.postForObject(url, demand,	Object.class);
		
		System.out.println("response" + response);
		
		return response;
	}
	
	public Object createReceiptForPayment(ReceiptRequest receiptReq){
		BillingServiceRequestWrapper billingServiceRequestWrapper = new BillingServiceRequestWrapper();
		billingServiceRequestWrapper.setBillingServiceRequestInfo(receiptReq.getRequestInfo());
		billingServiceRequestWrapper.setBillNumber(receiptReq.getBillNumber());
		billingServiceRequestWrapper.setTenantId(receiptReq.getTenantId());
		billingServiceRequestWrapper.setConsumerNumber(receiptReq.getConsumerCode());
		BillResponse billResponse = null;
		try{
			billResponse = billingServiceRepository.getBill(billingServiceRequestWrapper);
		}catch(Exception e){
			LOGGER.error("Couldn't fetch bill: ", e);
			throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CitizenServiceConstants.BILL_INVALID_MSG, CitizenServiceConstants.BILL_INVALID_DESC);
		}
		
		LOGGER.info("Bill number: "+receiptReq.getBillNumber());
		LOGGER.info("Response from billing svc: "+billResponse);
		
		if(!billResponse.getBill().isEmpty()){
			LOGGER.info("Receipt creation flow starts");
			
			List<Bill> bills = billResponse.getBill();
			for(Bill bill: bills){
				bill.setPaidBy(bill.getPayeeName());
				for(BillDetail billDetail: bill.getBillDetails()){
					billDetail.setAmountPaid(receiptReq.getAmountPaid());
				}
				
			}
			Instrument instrument = new Instrument();
			InstrumentType instrumentType = new InstrumentType();
			instrumentType.setName("Cash");
			instrumentType.setTenantId(receiptReq.getTenantId());
			instrument.setAmount(receiptReq.getAmountPaid());
			instrument.setInstrumentType(instrumentType);
			Receipt receipt = new Receipt();
			receipt.setBill(bills);
			receipt.setTenantId(receiptReq.getTenantId());
			receipt.setInstrument(instrument);
			List<Receipt> receipts = new ArrayList<>();
			receipts.add(receipt);
			ReceiptReq receiptRequest = new ReceiptReq();
			receiptRequest.setRequestInfo(receiptReq.getRequestInfo());
			receiptRequest.setReceipt(receipts);
			receiptRequest.setTenantId(receiptReq.getTenantId());
			
			LOGGER.info("Request for creation of receipt: "+receiptRequest.toString());
			Object receiptResponse = null;
			try{
				receiptResponse = collectionRepository.createReceipt(receiptRequest);
			}catch(Exception e){
				LOGGER.error("Error creating receipt in collection service. ", e);
				throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
						CitizenServiceConstants.RCPT_INVALID_MSG, CitizenServiceConstants.RCPT_INVALID_DESC);
			}
	
			LOGGER.info("Response from collection svc: "+receiptResponse);
			ServiceReq serviceReq = new ServiceReq();
			Object response = null;
			try{
				LOGGER.info("Fetching updated dues.....");
				response = billingServiceRepository.getDemand(billingServiceRequestWrapper);
			}catch(Exception e){
				LOGGER.error("Couldn't fetch Updated Dues: ", e);
				throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
						CitizenServiceConstants.DUES_INVALID_MSG, CitizenServiceConstants.DUES_INVALID_DESC);
			}
			
			LOGGER.info("Response for Dues..: "+response);
			
			serviceReq.setBackendServiceDetails(receiptResponse+","+response);
			
			try {
				LOGGER.info("Object being pushed to kafka queue for update........: "+serviceReq.toString());
				citizenProducer.producer(applicationProperties.getUpdateServiceTopic(),
						applicationProperties.getUpdateServiceTopicKey(), serviceReq);
			} catch (Exception e) {
				LOGGER.error("Error while pushing to kafka queue. ", e);
				throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
						CitizenServiceConstants.KAFKA_PUSH_FAIL_MSG, CitizenServiceConstants.KAFKA_PUSH_FAIL_DESC);
			}

			
			return receiptResponse;
		
		}else{
			LOGGER.error("Zero bills returned for this consumerCode: "+receiptReq.getConsumerCode());
			throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CitizenServiceConstants.BILL_INVALID_MSG, CitizenServiceConstants.BILL_INVALID_DESC);
		}
	}
	
	public Object generateBill(RequestInfo requestInfo, String consumerCode, String buisnessService, String tenantId){
		LOGGER.info("Generate bill flow starts for mobile no: "+consumerCode.toString());
		BillResponse billResponse = null;
		try{
			billResponse = billingServiceRepository.generateBillForDemand(requestInfo, tenantId, consumerCode, buisnessService);
		}catch(Exception e){
			LOGGER.error("Couldn't fetch bill: ", e);
			throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CitizenServiceConstants.BILL_GEN_FAIL_MSG, CitizenServiceConstants.BILL_GEN_FAIL_DESC);
		}
				
		LOGGER.info("Response from billing svc: "+billResponse);
		
		return billResponse;
		
	}
	
	public PGPayload generatePGPayload(RequestInfo requestInfo, String consumerCode, String buisnessService, String tenantId,
			String serviceRequestId){
		LOGGER.info("Generating PGPayload..");
		PGPayload pgPayload = PGPayload.builder()
							  .requestInfo(requestInfo).consumerCode(consumerCode).tenantId(tenantId).billServuce(buisnessService)
							  .serviceRequestId(serviceRequestId).build();
		return pgPayload;
		
	}
	
	public List<ServiceReq> getServiceRequests(ServiceRequestSearchCriteria serviceRequestSearchCriteria){
		List<ServiceReq> serviceRequests = new ArrayList<>();
		try{
			serviceRequests = citizenServiceRepository.getServiceRequests(serviceRequestSearchCriteria);
		}catch(Exception e){
			LOGGER.error("Couldn't fetch service requests ", e);
			throw new CustomException(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.toString()),
					CitizenServiceConstants.SVCREQ_NOT_FOUND_MSG, CitizenServiceConstants.SVCREQ_NOT_FOUND_DESC);
		}

		LOGGER.info("ServiceRequests obtained:   "+serviceRequests.toString());
		
		return serviceRequests;
		
	}
	
	public void sendMessageToKafka(ServiceReq servcieReq){
		
		try {
			citizenProducer.producer(applicationProperties.getCreateServiceTopic(),
					applicationProperties.getCreateServiceTopicKey(), servcieReq);
		} catch (Exception e) {
			LOGGER.debug("service createAsync:" + e);
			throw new RuntimeException(e);
		}
		}
		
	}
