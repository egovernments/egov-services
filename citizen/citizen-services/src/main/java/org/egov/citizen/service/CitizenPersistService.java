package org.egov.citizen.service;

import java.io.IOException;
import java.util.Date;

import org.egov.citizen.model.ServiceReq;
import org.egov.citizen.model.ServiceReqRequest;
import org.egov.citizen.model.ServiceReqResponse;
import org.egov.citizen.web.contract.factory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CitizenPersistService {

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
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	public ServiceReqResponse create(String serviceReqJson) {
		
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
		String id = getServiceReqId();
		
		serviceReqRequest.getServiceReq().setServiceRequestId(id);
		log.info("serviceReqRequest:"+serviceReqRequest);
		ObjectMapper mapper = new ObjectMapper();
		try{
			//String request = mapper.writeValueAsString(serviceReqRequest);
			//kafkaTemplate.send(createServiceTopic, serviceReqRequest);
			persistServiceReq(serviceReqRequest);
		} catch (Exception ex){
			log.error("failed to send kafka"+ex);
			ex.printStackTrace();
		}
		return getResponse(serviceReqRequest);
	}

	public String getServiceReqId() {
		String req = "{\"RequestInfo\":{\"apiId\":\"org.egov.ptis\",\"ver\":\"1.0\",\"ts\":\"20934234234234\",\"action\":\"asd\",\"did\":\"4354648646\",\"key\":\"xyz\",\"msgId\":\"654654\",\"requesterId\":\"61\",\"authToken\":\"1d8c2fee-50b3-43bf-affe-fc2e0196b514\"},\"idRequests\":[{\"idName\":\"CS.ServiceRequest\",\"tenantId\":\"default\",\"format\":\"SRN-[cy:MM]/[fy:yyyy-yy]-[d{4}]\"}]}";
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
	
	public void persistServiceReq(ServiceReqRequest serviceReqRequest){
		RequestInfo requestInfo = serviceReqRequest.getRequestInfo();
		ServiceReq serviceReq = serviceReqRequest.getServiceReq();
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonValue = null;
		try {
			 jsonValue = objectMapper.writeValueAsString(serviceReqRequest);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "INSERT INTO egov_citizen_service_req(id, tenantid, userid, "
				+ "servicecode, consumercode, email, mobilenumber, assignedto, createddate, "
				+ "lastmodifiedddate, createdby, lastmodifiedby, jsonvalue) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"; 
		final Object[] obj = new Object[] {serviceReq.getServiceRequestId(),serviceReq.getTenantId(), requestInfo.getUserInfo().getId(),
				serviceReq.getServiceCode(),serviceReq.getConsumerCode(),serviceReq.getEmail(),serviceReq.getPhone(),serviceReq.getAssignedTo(),
				new Date().getTime(),new Date().getTime(),requestInfo.getUserInfo().getId(),requestInfo.getUserInfo().getId(),
				jsonValue};
	        try {
	            jdbcTemplate.update(query, obj);
	        } catch (final Exception ex) {
	            log.info("the exception from insert query : " + ex);
	        }
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
	
		log.info("update serviceReqRequest:"+serviceReqRequest);
		
		try{
			kafkaTemplate.send(updateServiceTopic, serviceReqRequest);
		} catch (Exception ex){
			log.error("failed to send kafka"+ex);
			ex.printStackTrace();
		}
		return getResponse(serviceReqRequest);
	}
}
