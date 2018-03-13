package org.egov.user.repository;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.Otp;
import org.egov.user.web.contract.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.web.contract.OtpResponse;
import org.egov.user.web.contract.SearchRequest;
import org.egov.user.web.contract.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Slf4j;

@Component
@Slf4j
public class SearcherRepository {

	@Value("${egov.searcher.hostname}")
	private String searcherHostName;

	@Value("${egov.searcher.modulename}")
	private String moduleName;

	@Value("${egov.searcher.searchname}")
	private String searchName;

	@Value("${egov.searcher.endpoint}")
	private String searcherEndPoint;

	@Value("${egov.searcher.uri}")
	private String searcherUri;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(SearcherRepository.class);

	public List<User> getAllUsers(RequestInfo requestInfo, UserSearchCriteria searchCriteria) {

		StringBuilder url = new StringBuilder();
		url.append(searcherHostName).append(searcherUri).append(moduleName).append(searchName).append(searcherEndPoint);

		SearchRequest searchRequest = new SearchRequest(requestInfo, searchCriteria);
		LOGGER.info("searchRequest : " + searchRequest);

		Object response = restTemplate.postForObject(url.toString(), searchRequest, Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UserResponse userResponse = mapper.convertValue(response, UserResponse.class);
		return userResponse.getUsers();
	}

	public Otp getOtpByUseridAndOtp(RequestInfo requestInfo, Otp searchCriteria) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder();
		url.append(searcherHostName).append(searcherUri).append(moduleName).append("/userotpsearch").append(searcherEndPoint);

		SearchRequest searchRequest = new SearchRequest(requestInfo, searchCriteria);
		LOGGER.info("searchRequest : " + searchRequest);

		Object response = restTemplate.postForObject(url.toString(), searchRequest, Object.class);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OtpResponse otpResponse = mapper.convertValue(response, OtpResponse.class);
		if(otpResponse.getOtps().size()>0){
			return otpResponse.getOtps().get(0);
		}
		return null;
	}

}
