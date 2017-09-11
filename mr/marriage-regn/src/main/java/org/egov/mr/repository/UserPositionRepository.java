package org.egov.mr.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Allottee;
import org.egov.mr.web.contract.AllotteeResponse;
import org.egov.mr.web.contract.CreateUserRequest;
import org.egov.mr.web.contract.User;
import org.egov.mr.web.contract.UserResponse;
import org.egov.mr.web.contract.UserSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class UserPositionRepository {

	public static final Logger logger = LoggerFactory.getLogger(Allottee.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;

	public UserResponse getUser(User allottee, RequestInfo requestInfo) {

		logger.info("inside get user");
		String url = propertiesManager.getUserServiceHostName() + propertiesManager.getAllotteeServiceBasePAth()
				+ propertiesManager.getUserServiceSearchPath();
		List<Long> id=new ArrayList<>();
		id.add(requestInfo.getUserInfo().getId());
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(requestInfo);
//		userSearchRequest.setAadhaarNumber(allottee.getAadhaarNumber());
//		userSearchRequest.setPan(allottee.getPan());
//		userSearchRequest.setName(allottee.getName());
//		userSearchRequest.setEmailId(allottee.getEmailId());
//		userSearchRequest.setMobileNumber(allottee.getMobileNumber());
		userSearchRequest.setTenantId(allottee.getTenantId());
//		userSearchRequest.setId(id);
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
		UserResponse userResponse = callUserSearch(url, userSearchRequest);
		logger.info("response object for isAllotteeExist ::: " + userResponse);
		return userResponse;
	}
	
	public UserResponse callUserSearch(String url, Object userRequest) {

		UserSearchRequest userSearchRequest;
		CreateUserRequest createUserRequest;
		UserResponse userResponse = null;

		if (userRequest instanceof UserSearchRequest) {

			userSearchRequest = (UserSearchRequest) userRequest;
			try {
				userResponse = restTemplate.postForObject(url, userSearchRequest, UserResponse.class);
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getMessage() + e);
			}
		} else {
			createUserRequest = (CreateUserRequest) userRequest;
			try {
				userResponse = restTemplate.postForObject(url, createUserRequest, UserResponse.class);
			} catch (Exception e) {
				logger.error("Following exception occurred: " + e.getMessage());
				throw new RuntimeException(e.getMessage() + e);
			}
			
		}
		return userResponse;
	}
}
