package org.egov.pgr.repository;

import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.contract.SearcherRequest;
import org.egov.pgr.contract.ServiceReqSearchCriteria;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.egov.tracer.model.ServiceCallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ServiceRequestRepository {
	
	public static final Logger logger = LoggerFactory.getLogger(ServiceRequestRepository.class);
	
	@Autowired
	private LogAwareRestTemplate restTemplate;
	
	@Value("${infra.searcher.host}")
	private String searcherHost;
	
	@Value("${infra.searcher.endpoint}")
	private String searcherEndpoint;
	
	public Object getServiceRequests(RequestInfo requestInfo, ServiceReqSearchCriteria serviceReqSearchCriteria) {
		ObjectMapper mapper = new ObjectMapper();
		Object response = null;
		StringBuilder uri = new StringBuilder();
		uri.append(searcherHost);
		String endPoint = searcherEndpoint.replace("{moduleName}", "rainmaker-pgr").replace("{searchName}", "serviceRequestSearch");
		uri.append(endPoint);
		logger.info("URI: "+uri.toString());
		SearcherRequest searcherRequest = new SearcherRequest();
		searcherRequest.setRequestInfo(requestInfo);
		searcherRequest.setSearchCriteria(serviceReqSearchCriteria);
		try {
			logger.info("Request: "+mapper.writeValueAsString(searcherRequest));
			response = restTemplate.postForObject(uri.toString(), searcherRequest, Map.class);
		}catch(HttpClientErrorException e) {
			logger.error("Searcher threw a BadRequest: ",e);
			throw new ServiceCallException(e.getResponseBodyAsString());
		}catch(Exception e) {
			logger.error("Exception while fetching from searcher: ",e);
		}
		
		return response;
		
	}
}
