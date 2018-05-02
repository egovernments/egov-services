package org.egov.user.service;

import java.util.List;
import java.util.Optional;

import org.egov.otp.sevice.OtpService;
import org.egov.user.contract.User;
import org.egov.user.contract.UserReq;
import org.egov.user.contract.UserSearchCriteria;
import org.egov.user.utils.UserConfiguration;
import org.egov.user.validator.UserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private EnrichUserRequestService enrichUserRequestService;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private UserConfiguration userConfiguration;
	
	@Autowired
	private OtpService otpService;
	
	@Autowired
	private UserRequestValidator userRequestValidator;
	
	public Optional<?> create(UserReq userReq) {
		System.err.println(" in create");
		userRequestValidator.validateCreateRequest(userReq);
		List<User> users = userReq.getUsers();
		enrichUserRequestService.enrichUserRequest(users, userReq.getRequestInfo());
		//TODO:
		/*if(userConfiguration.getIsCitizenRegOtpEnable())
		otpService.sendOtp();*/
		kafkaTemplate.send(userConfiguration.getCreateUserTopic(), userReq);
		Optional<List<User>> usersRes = Optional.of(users);
		return usersRes;
	}
	
	public Optional<?> search() {
		return null;
	}
	
	public Optional<?> update(UserReq userReq) {
		
		return null;
	}
	
	/**
	 * how do we manage search when request comes of combination of CITIZEN and EMPLOYEE
	 * @param searchCriteria
	 * @param isUserCitizen
	 * @return
	 */
	public List<User> getUsers(UserSearchCriteria searchCriteria, Boolean isUserCitizen){
		
		
		return null;
	}
	
	/*private Map<String, String> validateCreateUserRequest(UserReq userReq)	{
		return null;
	}*/

}
