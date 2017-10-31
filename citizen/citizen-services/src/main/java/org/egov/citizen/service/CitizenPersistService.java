package org.egov.citizen.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.model.AuditDetails;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.model.ServiceReqResponse;
import org.egov.citizen.repository.CitizenServiceRepository;
import org.egov.citizen.repository.CollectionRepository;
import org.egov.citizen.repository.ServiceReqRepository;
import org.egov.citizen.web.contract.PGPayload;
import org.egov.citizen.web.contract.PGPayloadResponse;
import org.egov.citizen.web.contract.ReceiptRequest;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.citizen.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class CitizenPersistService {

	public static final Logger LOGGER = LoggerFactory
			.getLogger(CitizenPersistService.class);
		
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${kafka.topics.save.service}")
	private String createServiceTopic;
	
	@Value("${kafka.topics.update.service}")
	private String updateServiceTopic;
	
	@Value("${egov.services.Id_Gen_Service.hostname}")
	private String idGenHost;
	
	@Value("${egov.services.Id_Gen_Service.getId}")
	private String idGenGetIdUrl;
	
	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ServiceReqRepository serviceReqRepository;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Value("${egov.thread.sleep.time}")
	private Long threadTimeOut;
	
	public Map<String, Object> search(ServiceRequestSearchCriteria serviceRequestSearchCriteria, RequestInfo requestInfo){
		List<Map<String, Object>> mapsRes = serviceReqRepository.search(serviceRequestSearchCriteria);
		LOGGER.info("Result from db for search: "+mapsRes);
		List<Map<String, Object>> maps = new ArrayList<>();
		for(int i = 0; i< mapsRes.size() - 1; i++){
			Map<String, Object> serviceReqMap = new HashMap<>();
			serviceReqMap = mapsRes.get(i);
			maps.add(serviceReqMap);

		}
		List<Object> response = new ArrayList<>();
		Map<String, Object> responeMap = new HashMap<>();		
		for(Map<String, Object> map : maps){

			LOGGER.info("Map: "+map);
		
			String s = (String)map.get("serviceReq");
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				Map<String, Object> m= objectMapper.readValue(s, Map.class);
				if(m.containsKey("serviceReq"))
					response.add(m.get("serviceReq"));
				else {
					response.add(m);
				}
				System.out.println("m:"+m);
			} catch (IOException e) {
				LOGGER.info("Exception: ",e);
			}

		}
		System.out.println("serviceReq:"+response);
		
		responeMap.put("serviceReq", response);
		responeMap.put("comments", mapsRes.get(mapsRes.size() - 1).get("comments"));
		responeMap.put("documents", mapsRes.get(mapsRes.size() - 1).get("documents"));
		if(null != serviceRequestSearchCriteria.getAnonymous()){
			if(true == serviceRequestSearchCriteria.getAnonymous()){
				Object receiptresponse = null;
				try{
					receiptresponse = collectionRepository.searchReceipt(requestInfo, 
							serviceRequestSearchCriteria.getTenantId(), serviceRequestSearchCriteria.getServiceRequestId());
				}catch(Exception e){
					LOGGER.info("Couldn't fetch receiptdetails for srn: "+serviceRequestSearchCriteria.getServiceRequestId());
				}
				
				responeMap.put("receiptDetails", receiptresponse);
			}
		}
		responeMap.put("ResponseInfo", responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		return responeMap;
	}
	
	public ServiceReqResponse create(String serviceReqJson) {

		String id = getServiceReqId();
		serviceReqJson = makeServiceCall(serviceReqJson, id, true);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ServiceReqRequest serviceReqRequest = null;
		try {
		serviceReqRequest = objectMapper.readValue(serviceReqJson, ServiceReqRequest.class);
		serviceReqRequest.getServiceReq().setServiceRequestId(id);
		serviceReqRequest.getServiceReq().setAuditDetails(getAuditDetaisl(serviceReqRequest.getRequestInfo(), true));
		LOGGER.info("parsed value: "+serviceReqRequest);
		} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		

		
		LOGGER.info("serviceReqRequest:"+serviceReqRequest);
		ObjectMapper mapper = new ObjectMapper();
		try{
		//String request = mapper.writeValueAsString(serviceReqRequest);
		//kafkaTemplate.send(createServiceTopic, serviceReqRequest);
		serviceReqRepository.persistServiceReq(serviceReqRequest);
		} catch (Exception ex){
			LOGGER.error("failed to send kafka"+ex);
		ex.printStackTrace();
		}
		return getResponse(serviceReqRequest);
		}

	public String getServiceReqId() {

		String req = "{\"RequestInfo\":{\"apiId\":\"org.egov.ptis\",\"ver\":\"1.0\",\"ts\":\"20934234234234\",\"action\":\"asd\",\"did\":\"4354648646\",\"key\":\"xyz\",\"msgId\":\"654654\",\"requesterId\":\"61\",\"authToken\":\"6ffa37c2-338f-4630-8721-ce54a9146fb9\"},\"idRequests\":[{\"idName\":\"CS.ServiceRequest\",\"tenantId\":\"default\",\"format\":\"SRN-[cy:MM]-[fy:yyyy-yy]-[d{4}]\"}]}";

		String url = idGenHost+idGenGetIdUrl;
		log.info("url:"+url);
		log.info("req: "+req);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<String>(req, headers);
		ResponseEntity<String> res = restTemplate.postForEntity(url, entity, String.class);
		System.out.println("idRes:" + res.getBody());
		String idResp = res.getBody();
		String id = JsonPath.read(idResp, "$.idResponses[0].id");
		return id;
	}
	
	
	
	private ServiceReqResponse getResponse(ServiceReqRequest serviceReqRequest){
		ServiceReqResponse serviceRes = new ServiceReqResponse();
		serviceRes.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(serviceReqRequest.getRequestInfo(), true));
		serviceRes.setServiceReq(serviceReqRequest.getServiceReq());
		return serviceRes;
	}
	
    public ServiceReqResponse update(String serviceReqJson) {
    	serviceReqJson = makeServiceCall(serviceReqJson, null, false);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ServiceReqRequest serviceReqRequest = null;
		try {
			serviceReqRequest = objectMapper.readValue(serviceReqJson, ServiceReqRequest.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AuditDetails auditDetails = getAuditDetaisl(serviceReqRequest.getRequestInfo(), false);
		LOGGER.info("serviceReqRequest: "+serviceReqRequest);
		serviceReqRequest.getServiceReq().setAuditDetails(getAuditDetaisl(serviceReqRequest.getRequestInfo(), true));
		serviceReqRequest.getServiceReq().getAuditDetails().setLastModifiedBy(auditDetails.getLastModifiedBy());
		serviceReqRequest.getServiceReq().getAuditDetails().setLastModifiedDate(auditDetails.getLastModifiedDate());

		
		log.info("update serviceReqRequest:"+serviceReqRequest);
		
		try{
			//kafkaTemplate.send(updateServiceTopic, serviceReqRequest);
			serviceReqRepository.updateServiceReq(serviceReqRequest);
		} catch (Exception ex){
			log.error("failed to send kafka"+ex);
			ex.printStackTrace();
		}
		return getResponse(serviceReqRequest);
	}
    
    public String makeServiceCall(String serviceReqJson, String id, boolean isCreate){
    	log.info("serviceReqJson1:"+serviceReqJson);
    	DocumentContext documentContext = JsonPath.parse(serviceReqJson);
    	
		if (isCreate) {
			documentContext.put("$.serviceReq", "serviceRequestId", id);
			documentContext.put("$.serviceReq", "consumerCode", id);
		}
    	
    	if((documentContext.read("$.serviceReq.serviceCode").toString().equals("WATER_NEWCONN") ||
    			documentContext.read("$.serviceReq.serviceCode").toString().equals("BPA_FIRE_NOC") ||
    			documentContext.read("$.serviceReq.serviceCode").toString().equals("TL_NEWCONN")) && isCreate){
    		documentContext.put("$.serviceReq.backendServiceDetails[0].request.Demands[0]", "consumerCode", id);
    		String url = documentContext.read("$.serviceReq.backendServiceDetails[1].url");
    		url = url.replaceAll("consumerCode=", "consumerCode="+id);
    		documentContext.put("$.serviceReq.backendServiceDetails[1]","url", url);
    		serviceReqJson = documentContext.jsonString();
    	}
    	log.info("documentContext:"+documentContext.jsonString());
    	Object serviceCall = JsonPath.read(serviceReqJson, "$.serviceReq.backendServiceDetails");
    	System.out.println(serviceCall instanceof List);
    	if(serviceCall != null){
    		List<LinkedHashMap<String, Object>> list =(List<LinkedHashMap<String, Object>>) serviceCall;
    		log.info("list:"+list);
    		for(int i=0; i<list.size(); i++){
    			LinkedHashMap<String, Object> map = list.get(i);
    			String url = (String)map.get("url");
    			LinkedHashMap<String, Object> requestMap = (LinkedHashMap<String, Object>)map.get("request");
    			
    			log.info("url:"+url+","+"requestMap:"+requestMap);
    			HttpHeaders headers = new HttpHeaders();
    			headers.setContentType(MediaType.APPLICATION_JSON);
    			
    			HttpEntity<LinkedHashMap<String, Object>> entity = new HttpEntity<LinkedHashMap<String, Object>>(requestMap, headers);
    			ResponseEntity<String> res = null;
    			try{
    				res = restTemplate.postForEntity(url, entity, String.class);
    			} catch (Exception ex){
    				ex.printStackTrace();
    			}
    			System.out.println("res:"+res);
    			String resBody = res.getBody();
    			System.out.println("resBody:"+resBody);
    			DocumentContext documentContext2 = JsonPath.parse(resBody);
    			String responsePath = "$.serviceReq.backendServiceDetails["+i+"]";
    			System.out.println("responsePath:"+responsePath);
    			//documentContext.set(responsePath, resBody);
    			documentContext.put(responsePath, "response", documentContext2.json());
    			
    			/*if(i==1 && documentContext.read("$.serviceReq.serviceCode").toString().equals("WATER_NEWCONN")&&isCreate){
    				String ackNum = documentContext2.read("$.Connection[0].acknowledgementNumber");
    				documentContext.put("$.serviceReq", "consumerCode", ackNum);
    			}*/
    			
    	    	serviceReqJson = documentContext.jsonString();
    	    	log.info("serviceReqJson2:"+serviceReqJson);
    	    	
    	    	Thread currentThread = Thread.currentThread();
    	    	if(list.size()>1){
    	    		try {
    	    			log.info("sleep");
    	    			currentThread.sleep(threadTimeOut);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	    	}
    	    	
    		}
    	}
    	return serviceReqJson;
    }
    
    /*public String populateNextServiceCallData(){
    	
    }*/
    
	public PGPayload generatePGPayload(ReceiptRequest receiptRequest, RequestInfo requestInfo){
		LOGGER.info("Generating PGPayload..");
		StringBuilder msgForHash = new StringBuilder();
		String delimiter="|";
		msgForHash.append(receiptRequest.getAmountPaid()).append(delimiter)
		          .append(receiptRequest.getDate()).append(delimiter)
		          .append(receiptRequest.getBillNumber()).append(delimiter)
		          .append(receiptRequest.getBiller()).append(delimiter)
		          .append(receiptRequest.getBillService());
		
		String hashKey = applicationProperties.getHashKey();
		String requestHash = getHashedValue(msgForHash.toString(), hashKey);
		LOGGER.info("Request hash obtained: "+requestHash);
		
		PGPayload pgPayload = PGPayload.builder()
							  .requestInfo(requestInfo)
							  .consumerCode(receiptRequest.getConsumerCode())
							  .tenantId(receiptRequest.getTenantId())
							  .billService(receiptRequest.getBillService())
							  .serviceRequestId(receiptRequest.getServiceRequestId())
							  .amountPaid(receiptRequest.getAmountPaid())
							  .biller(receiptRequest.getBiller())
							  .billNumber(receiptRequest.getBillNumber())
							  .date(receiptRequest.getDate())
							  .email(requestInfo.getUserInfo().getEmailId())
							  .mobileNo(requestInfo.getUserInfo().getMobileNumber())
							  .requestHash(requestHash)
							  .uid(requestInfo.getUserInfo().getId())
							  .retrunUrl(receiptRequest.getReturnUrl())
							  .build();
		LOGGER.info("PGPayload generated: "+pgPayload);
		
		LOGGER.info("Persiting data to db: "+pgPayload);
		serviceReqRepository.persistPaymentData(pgPayload, null, true);
		
		return pgPayload;
		
	}
	
	public boolean validatingPGResponse(PGPayloadResponse pGPayLoadResponse){
		LOGGER.info("Validating pgrespnse..");
		boolean isValid = false;		
		LOGGER.info("Persiting data to db: "+pGPayLoadResponse);
		serviceReqRepository.persistPaymentData(null, pGPayLoadResponse, false);
		
		StringBuilder msgForHash = new StringBuilder();
		String delimiter="|";
		msgForHash.append(pGPayLoadResponse.getStatus()).append(delimiter)
		          .append(pGPayLoadResponse.getBillNumber()).append(delimiter)
		          .append(pGPayLoadResponse.getBillService()).append(delimiter)
		          .append(pGPayLoadResponse.getTransactionId()).append(delimiter)
		          .append(pGPayLoadResponse.getServiceRequestId()).append(delimiter)
		          .append(pGPayLoadResponse.getUid());
		
		LOGGER.info("msg to be hashed: "+msgForHash.toString());
		String hashKey = applicationProperties.getHashKey();
		String responseHash = getHashedValue(msgForHash.toString(), hashKey);
		
		if(responseHash.equals(pGPayLoadResponse.getResponseHash())){
			isValid = true;
			LOGGER.info("Response hash is valid!");
			return isValid;

		}
		LOGGER.info("Hash receieved: "+pGPayLoadResponse.getResponseHash());
		LOGGER.info("Hash generated to match: "+responseHash);
		LOGGER.info("Response hash is INVALID!");
		return isValid;
		
	}
	
	private static String getHashedValue(String msg, String keyString) {
	    String digest = null;
	    try {
	      SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacSHA256");
	      Mac mac = Mac.getInstance("HmacSHA256");
	      mac.init(key);
	      byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));
	      StringBuffer hash = new StringBuffer();
	      for (int i = 0; i < bytes.length; i++) {
	        String hex = Integer.toHexString(0xFF & bytes[i]);
	        if (hex.length() == 1) {
	          hash.append('0');
	        }
	        hash.append(hex);
	      }
	      digest = hash.toString();
	    } catch (UnsupportedEncodingException e) {
	    } catch (InvalidKeyException e) {
	    } catch (NoSuchAlgorithmException e) {
	    }
	    return digest;
	}
    
    
    
    private AuditDetails getAuditDetaisl(RequestInfo requestInfo, boolean isCreate){
		AuditDetails auditDetails = new AuditDetails();
		
		if(isCreate){
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getId());
			auditDetails.setCreatedDate(new Date().getTime());
		}
		auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId());
		auditDetails.setLastModifiedDate(new Date().getTime());
		
		return auditDetails;
	}
    
    
}
