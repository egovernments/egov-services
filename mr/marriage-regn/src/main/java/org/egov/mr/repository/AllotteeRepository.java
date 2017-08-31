package org.egov.mr.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Allottee;
import org.egov.mr.web.contract.AllotteeResponse;
import org.egov.mr.web.contract.CreateUserRequest;
import org.egov.mr.web.contract.UserSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AllotteeRepository {

	public static final Logger logger = LoggerFactory.getLogger(Allottee.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	public AllotteeResponse getAllottees(Allottee allottee, RequestInfo requestInfo) {

		logger.info("inside get allottee");
		String url = propertiesManager.getAllotteeServiceHostName() + propertiesManager.getAllotteeServiceBasePAth()
				+ propertiesManager.getAllotteeServiceSearchPath();
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(requestInfo);
		userSearchRequest.setAadhaarNumber(allottee.getAadhaarNumber());
		userSearchRequest.setPan(allottee.getPan());
		userSearchRequest.setName(allottee.getName());
		userSearchRequest.setEmailId(allottee.getEmailId());
		userSearchRequest.setMobileNumber(allottee.getMobileNumber());
		userSearchRequest.setTenantId(allottee.getTenantId());
		if(allottee.getUserName() != null){
		    userSearchRequest.setUserName(allottee.getUserName());
		}
		else{
			int maxLength = 50;
			final String name;
			String allotteeName = allottee.getName().replaceAll(" ", "");
		    if (allotteeName.length() <= maxLength) {
		    	name = allotteeName;
			} else { 
				name = allotteeName.substring(0, maxLength);
			}
			String userName = name + allottee.getMobileNumber();
		    userSearchRequest.setUserName(userName);
		}
		logger.info("url for allottee api post call :: " + url
				+ "the request object for isAllotteeExist is userSearchRequest ::: " + userSearchRequest);
		AllotteeResponse allotteeResponse = callAllotteSearch(url, userSearchRequest);
		logger.info("response object for isAllotteeExist ::: " + allotteeResponse);
		return allotteeResponse;
	}
	
	public AllotteeResponse callAllotteSearch(String url, Object userRequest) {

		UserSearchRequest userSearchRequest;
		CreateUserRequest createUserRequest;
		AllotteeResponse allotteeResponse = null;

		if (userRequest instanceof UserSearchRequest) {

			userSearchRequest = (UserSearchRequest) userRequest;
			try {
				allotteeResponse = restTemplate.postForObject(url, userSearchRequest, AllotteeResponse.class);
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getMessage() + e);
			}
		} else {
			createUserRequest = (CreateUserRequest) userRequest;
			try {
				allotteeResponse = restTemplate.postForObject(url, createUserRequest, AllotteeResponse.class);
			} catch (Exception e) {
				logger.error("Following exception occurred: " + e.getMessage());
				throw new RuntimeException(e.getMessage() + e);
			}
			
		}
		return allotteeResponse;
	}
}
