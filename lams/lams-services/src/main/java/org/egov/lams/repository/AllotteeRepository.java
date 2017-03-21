package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.AllotteeResponse;
import org.egov.lams.contract.CreateUserRequest;
import org.egov.lams.contract.UserResponse;
import org.egov.lams.contract.UserSearchRequest;
import org.egov.lams.exception.AgreementException;
import org.egov.lams.exception.LamsException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Repository
public class AllotteeRepository {

	@Autowired
	private PropertiesManager propertiesManager;

	public boolean isAllotteeExist(AgreementRequest agreementRequest) {

		RestTemplate restTemplate = new RestTemplate();
		String url = propertiesManager.getAllotteeServiceHostName()
				+ propertiesManager.getAllotteeServiceBasePAth()
				+ propertiesManager.getAllotteeServiceSearchPath();
		
		UserSearchRequest userSearchRequest = new UserSearchRequest();
		// TODO just setting id field, change when they user search with
		// modelattribute rather than request body
		userSearchRequest.setRequestInfo(agreementRequest.getRequestInfo());
		List<Long> allotteeId = new ArrayList<>();
		allotteeId.add(agreementRequest.getAgreement().getAllottee().getId());
		userSearchRequest.setId(allotteeId);
		AllotteeResponse allotteeResponse = restTemplate.postForObject(url, userSearchRequest, AllotteeResponse.class);
		List<Allottee> allottees = allotteeResponse.getAllottee();

		return !allottees.isEmpty();
	}
	
	public void createAllottee(AgreementRequest agreementRequest) {
		
		RestTemplate restTemplate = new RestTemplate();
		 String url = propertiesManager.getAllotteeServiceHostName()
			    	+ propertiesManager.getAllotteeServiceBasePAth()
			    	+ propertiesManager.getAllotteeServiceCreatePAth();
		 //TODO set user name and password using any gen service
		 CreateUserRequest userRequest = new CreateUserRequest(agreementRequest.getRequestInfo(),
				 agreementRequest.getAgreement().getAllottee());
		 try{
		 restTemplate.postForObject(url, userRequest, UserResponse.class);
		 }catch (RestClientException restClientException) {
			 restClientException.printStackTrace();
			 throw new LamsException(restClientException.getMessage());
			 // TODO: handle exception
		}
		
	}

}
