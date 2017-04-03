package org.egov.lams.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.enums.Gender;
import org.egov.lams.model.enums.UserType;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.CreateUserRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.UserSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class AllotteeService {
	
	public static final Logger logger = LoggerFactory.getLogger(Allottee.class);

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private RestTemplate restTemplate;

	public AllotteeResponse getAllottees(AgreementCriteria agreementCriteria,RequestInfo requestInfo) {

		String url = propertiesManager.getAllotteeServiceHostName() + propertiesManager.getAllotteeServiceBasePAth()
				+ propertiesManager.getAllotteeServiceSearchPath();
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(requestInfo);
		List<Long> allotteeId = new ArrayList<>();
		allotteeId.addAll(agreementCriteria.getAllottee());
		userSearchRequest.setId(allotteeId);
		userSearchRequest.setName(agreementCriteria.getAllotteeName());
		if(agreementCriteria.getMobilenumber()!= null)
		userSearchRequest.setMobileNumber(agreementCriteria.getMobilenumber().toString());
		logger.info("url for allottee api post call",url);
		return callAllotteSearch(url,userSearchRequest);
	}
	
	public AllotteeResponse isAllotteeExist(Allottee allottee,RequestInfo requestInfo) {

		String url = propertiesManager.getAllotteeServiceHostName()	+ propertiesManager.getAllotteeServiceBasePAth()
				+ propertiesManager.getAllotteeServiceSearchPath();
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		userSearchRequest.setRequestInfo(requestInfo);
		userSearchRequest.setAadhaarNumber(allottee.getAadhaarNumber());
		userSearchRequest.setPan(allottee.getPan());
		userSearchRequest.setName(allottee.getName());
		userSearchRequest.setEmailId(allottee.getEmailId());
		userSearchRequest.setMobileNumber(allottee.getMobileNumber());
		logger.info("url for allottee api post call",url);
		return callAllotteSearch(url,userSearchRequest);
	}
	
	public AllotteeResponse createAllottee(Allottee allottee,RequestInfo requestInfo) {
		
		 String url = propertiesManager.getAllotteeServiceHostName() + propertiesManager.getAllotteeServiceBasePAth()
			    	+ propertiesManager.getAllotteeServiceCreatePAth();
		 allottee.setUserName(allottee.getName()+allottee.getMobileNumber());
		 allottee.setPassword(allottee.getMobileNumber().toString());
		 allottee.setGender(Gender.FEMALE);
		 allottee.setType(UserType.CITIZEN);
		 allottee.setActive(true); //FIXME set user name and password using any gen service
		 CreateUserRequest createUserRequest = new CreateUserRequest(requestInfo,allottee);
		 logger.info("url for allottee api post call : "+url+"  the user request obj is : "+ createUserRequest);
		 AllotteeResponse allotteeResponse = callAllotteSearch(url,createUserRequest);
		 allottee.setId(allotteeResponse.getAllottee().get(0).getId());
		 Syatem.err.println("the id from alottee ::: "+allotteeResponse.getAllottee().get(0).getId());
		 return allotteeResponse;
	}
	
	public AllotteeResponse callAllotteSearch(String url, Object userRequest) {

		UserSearchRequest userSearchRequest;
		CreateUserRequest createUserRequest;
		AllotteeResponse allotteeResponse;

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
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getMessage() + e);
			}
		}
		logger.info("list of allottes from allotteresponse from allotte api call for get allottees",
				allotteeResponse.getAllottee());
		return allotteeResponse;
	}
}
