package org.egov.user.repository;

import java.util.Map;

import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class UserRepository {
		
	@Autowired
	private RestTemplate restTemplate;
		
	/**
	 * Fetches results from searcher framework based on the uri and request that define what is to be searched.
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 */
	public Object fetchResult(StringBuilder uri, Object request) {
		
		/*
		 * FIXME Get the bean from annotation with secondary name
		 */
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		try {
			response = restTemplate.postForObject(uri.toString(), request, Map.class);
		}catch(HttpClientErrorException e) {
			log.error("External Service threw an Exception: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			log.error("Exception while fetching from searcher: ",e);
		}
		
		return response;
		
	}
}
