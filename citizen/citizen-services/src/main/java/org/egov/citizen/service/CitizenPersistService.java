package org.egov.citizen.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.egov.citizen.config.ApplicationProperties;
import org.egov.citizen.model.AuditDetails;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.model.ServiceReqResponse;
import org.egov.citizen.producer.CitizenProducer;
import org.egov.citizen.repository.ServiceReqRepository;
import org.egov.citizen.web.contract.ServiceRequestSearchCriteria;
import org.egov.citizen.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.fasterxml.jackson.core.JsonProcessingException;
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
	private CitizenProducer citizenProducer; 
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
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
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private ServiceReqRepository serviceReqRepository;
	 
	
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

		//Map<String, Object> 
		
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
		System.out.println("response:"+response);
		
		responeMap.put("serviceReq", response);
		responeMap.put("comments", mapsRes.get(mapsRes.size() - 1).get("comments"));
		responeMap.put("documents", mapsRes.get(mapsRes.size() - 1).get("documents"));

		responeMap.put("ResponseInfo", responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		return responeMap;
	}
	
	public ServiceReqResponse create(String serviceReqJson) {

		serviceReqJson = makeServiceCall(serviceReqJson);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ServiceReqRequest serviceReqRequest = null;
		try {
		serviceReqRequest = objectMapper.readValue(serviceReqJson, ServiceReqRequest.class);
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
		String id = getServiceReqId();

		serviceReqRequest.getServiceReq().setServiceRequestId(id);
		serviceReqRequest.getServiceReq().setAuditDetails(getAuditDetaisl(serviceReqRequest.getRequestInfo(), true));
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

		String req = "{\"RequestInfo\":{\"apiId\":\"org.egov.ptis\",\"ver\":\"1.0\",\"ts\":\"20934234234234\",\"action\":\"asd\",\"did\":\"4354648646\",\"key\":\"xyz\",\"msgId\":\"654654\",\"requesterId\":\"61\",\"authToken\":\"d1feb52b-fb61-4066-be09-398de105e472\"},\"idRequests\":[{\"idName\":\"CS.ServiceRequest\",\"tenantId\":\"default\",\"format\":\"SRN-[cy:MM]/[fy:yyyy-yy]-[d{4}]\"}]}";

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
    
    public String makeServiceCall(String serviceReqJson){
    	log.info("serviceReqJson1:"+serviceReqJson);
    	DocumentContext documentContext = JsonPath.parse(serviceReqJson);
    	Object serviceCall = JsonPath.read(serviceReqJson, "$.serviceReq.backendServiceDetails");
    	if(serviceCall != null){
    		List<LinkedHashMap<String, Object>> list =(List<LinkedHashMap<String, Object>>) serviceCall;
    		log.info("list:"+list);
    		for(int i=0; i<list.size(); i++){
    			LinkedHashMap<String, Object> map = list.get(i);
    			String url = (String)map.get("url");
    			LinkedHashMap<String, Object> requestMap = (LinkedHashMap<String, Object>)map.get("request");
    			
    			log.info("url:"+url+","+"requestStr:"+requestMap);
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
    	    	serviceReqJson = documentContext.jsonString();
    	    	log.info("serviceReqJson2:"+serviceReqJson);
    		}
    	}
    	return serviceReqJson;
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
