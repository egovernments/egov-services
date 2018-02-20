package org.egov.pgr.service;

import org.egov.pgr.contract.ServiceReqRequest;
import org.egov.pgr.contract.ServiceReqResponse;
import org.egov.pgr.utils.ResponseInfoFactory;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

}
