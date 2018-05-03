package org.egov.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.otp.sevice.OtpService;
import org.egov.user.contract.SearcherRequest;
import org.egov.user.contract.User;
import org.egov.user.contract.UserReq;
import org.egov.user.contract.UserRes;
import org.egov.user.contract.UserSearchCriteria;
import org.egov.user.repository.UserRepository;
import org.egov.user.utils.UserConfiguration;
import org.egov.user.utils.UserUtils;
import org.egov.user.validator.UserRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

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
	private UserUtils userUtils;
	
	@Autowired
	private UserRequestValidator userRequestValidator;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	@Qualifier("searcher")
	private ObjectMapper objMapper;
	
	public Optional<?> create(UserReq userReq) {
		
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
	
	public UserRes search(UserSearchCriteria userSearchCriteria, RequestInfo requestInfo) {
		/*
		 * FIXME how to hande if search request comes for citizen and employee combined?
		 */
		UserRes userRes = this.getUsers(userSearchCriteria, requestInfo, false);
		Set<User> userSet = this.getUsers(userSearchCriteria, requestInfo, true).getUsers().parallelStream().collect(Collectors.toSet());
		userSet.addAll(userRes.getUsers());
		List<User> userList = new ArrayList<>();
		userList.addAll(userSet);
		userRes.setUsers(userList);
		return userRes;
	}
	
	/**
	 * 
	 * 
	 * @param searchCriteria
	 * @param isUserCitizen
	 * @return
	 */
	public UserRes getUsers(UserSearchCriteria searchCriteria,RequestInfo requestInfo, Boolean isUserCitizen){
		
		if(isUserCitizen) searchCriteria.setTenantId(searchCriteria.getTenantId().split("\\.")[0]);
		
		StringBuilder uri = new StringBuilder();
		SearcherRequest request = userUtils.prepareSearchRequestWithDetails(uri, searchCriteria, requestInfo, isUserCitizen);
		return deserializeResult(userRepo.fetchResult(uri, request));
	}

	
	private UserRes deserializeResult(Object fetchResult) {
		return objMapper.convertValue(fetchResult,UserRes.class);
	}
	
	/*private Map<String, String> validateCreateUserRequest(UserReq userReq)	{
		return null;
	}*/

}
