package org.egov.pgr.service;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.CountResponse;
import org.egov.pgr.contract.ServiceReq;
import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.pgr.repository.ServiceRequestRepository;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceRequestService {

	@Value("${kafka.topics.save.servicereq}")
	private String saveTopic;

	@Value("${kafka.topics.update.servicereq}")
	private String updateTopic;

	@Autowired
	private ResponseInfoFactory factory;
	
	public static final Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);
	
	@Autowired
	private ServiceRequestRepository serviceRequestRepository;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaProducer;

	/***
	 * Asynchronous method performs business and adds the data into egov-persister
	 * queue
	 * 
	 * @param request
	 * @return
	 */
	public ServiceReqResponse create(ServiceReqRequest request) {

		log.debug(" the incoming request obj in service : {}", request);

		kafkaProducer.send(saveTopic, request);
		return getServiceReqResponse(request);
	}

	/***
	 * returns ServiceReqResponse built based on the given ServiceReqRequest
	 * 
	 * @param serviceReqRequest
	 * @return serviceReqResponse
	 */
	public ServiceReqResponse getServiceReqResponse(ServiceReqRequest serviceReqRequest) {

		return ServiceReqResponse.builder()
				.responseInfo(factory.createResponseInfoFromRequestInfo(serviceReqRequest.getRequestInfo(), true))
				.serviceReq(serviceReqRequest.getServiceReq()).build();
	}
	
	/**
	 * Method to return service requests received from the repo to the controller in the reqd format
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return ServiceReqResponse
	 * @author vishal
	 */
	public ServiceReqResponse getServiceRequests(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria){
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);	
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServiceReqResponse serviceReqResponse = new ServiceReqResponse();
		Object response = null;
		response = serviceRequestRepository.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		logger.info("Searcher response: ",response);
		serviceReqResponse = mapper.convertValue(response, ServiceReqResponse.class);
		
		return serviceReqResponse;
	}
	
	
	public Object getCount(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria){
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);	
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Object response = null;
		response = serviceRequestRepository.getCount(requestInfo, serviceReqSearchCriteria);
		logger.info("Searcher response: ",response);
		if(null == response) {
			return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
					0D);
		}else {
			Double count = JsonPath.read(response, "$.count[0].count");
			return new CountResponse(factory.createResponseInfoFromRequestInfo(requestInfo, false),
					count);
		}
	}

}
