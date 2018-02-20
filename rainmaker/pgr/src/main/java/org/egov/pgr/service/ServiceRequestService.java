package org.egov.pgr.service;

import org.egov.common.contract.request.RequestInfo;
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
	private ResponseInfoFactory responseInfoFactory;
	
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
	
	public ServiceReqResponse getServiceRequests(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria){
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);	
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ServiceReqResponse serviceReqResponse = new ServiceReqResponse();
		Object response = null;
		response = serviceRequestRepository.getServiceRequests(requestInfo, serviceReqSearchCriteria);
		logger.info("Searcher response: "+response);
		serviceReqResponse = mapper.convertValue(response, ServiceReqResponse.class);
		
		return serviceReqResponse;
	}

}
