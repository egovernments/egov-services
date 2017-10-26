package org.egov.lcms.service;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.factory.ResponseFactory;
import org.egov.lcms.models.SummonRequest;
import org.egov.lcms.models.SummonResponse;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SummonService {
	
	@Autowired
	ResponseFactory responseInfoFactory;
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	
	public SummonResponse createSummon(SummonRequest summonRequest){
		
		return new SummonResponse(responseInfoFactory.getResponseInfo(summonRequest.getRequestInfo(), HttpStatus.CREATED), summonRequest.getSummons());
		
		
		
	}

}
