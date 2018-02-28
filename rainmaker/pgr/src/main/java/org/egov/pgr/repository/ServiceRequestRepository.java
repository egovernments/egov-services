package org.egov.pgr.repository;

import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class ServiceRequestRepository {
		
	@Autowired
	private LogAwareRestTemplate restTemplate;
	
	@Value("${infra.searcher.host}")
	private String searcherHost;
	
	@Value("${infra.searcher.endpoint}")
	private String searcherEndpoint;
	
	
	/**
	 * Fetches service requests from postgres db based on criteria provided in ServiceReqSearchCriteria
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 * @author vishal
	 */
	public Object getServiceRequests(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		StringBuilder uri = new StringBuilder();
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", "rainmaker-pgr").replace("{searchName}", "serviceRequestSearch");
		uri.append(endPoint);
		log.info("URI: "+uri.toString());
		SearcherRequest searcherRequest = new SearcherRequest();
		searcherRequest.setRequestInfo(requestInfo);
		searcherRequest.setSearchCriteria(serviceReqSearchCriteria);
		try {
			log.info("Request: "+mapper.writeValueAsString(searcherRequest));
			response = restTemplate.postForObject(uri.toString(), searcherRequest, Map.class);
		}catch(HttpClientErrorException e) {
			log.error("Searcher threw aN Exception: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			log.error("Exception while fetching from searcher: ",e);
		}
		
		return response;
		
	}
	
	
	/**
	 * Fetches count of service requests from postgres db based on criteria provided in ServiceReqSearchCriteria
	 * 
	 * @param requestInfo
	 * @param serviceReqSearchCriteria
	 * @return Object
	 * @author vishal
	 */
	public Object getCount(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object response = null;
		StringBuilder uri = new StringBuilder();
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", "rainmaker-pgr").replace("{searchName}", "count");
		uri.append(endPoint);
		log.info("URI: ",uri.toString());
		SearcherRequest searcherRequest = new SearcherRequest();
		searcherRequest.setRequestInfo(requestInfo);
		searcherRequest.setSearchCriteria(serviceReqSearchCriteria);
		try {
			log.info("Request: ",mapper.writeValueAsString(searcherRequest));
			response = restTemplate.postForObject(uri.toString(), searcherRequest, Map.class);
		}catch(HttpClientErrorException e) {
			log.error("Searcher threw aN Exception: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			log.error("Exception while fetching from searcher: ",e);
		}
		
		return response;
		
	}
}
