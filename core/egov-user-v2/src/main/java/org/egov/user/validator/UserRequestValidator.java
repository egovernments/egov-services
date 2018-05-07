package org.egov.user.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.Type;
import org.egov.common.contract.request.UserInfo;
import org.egov.user.contract.User;
import org.egov.user.contract.UserReq;
import org.egov.user.contract.UserSearchCriteria;
import org.egov.user.service.UserService;
import org.egov.user.utils.UserConfiguration;
import org.egov.user.utils.UserErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserRequestValidator {
	
	@Autowired
	private UserConfiguration userConfiguration;
	
	@Autowired
	private UserService userService;

	private Map<String, String> errors = null;
	//private Set<String> userTypes = null;

	/**
	 * Validate User create request.
	 * 
	 * @param userReq
	 * @return Map<String,String> this will return map of error. 
	 * */
	public Map<String, String> validateCreateRequest(UserReq userReq) {

		errors = new HashMap<>();
		Set<String> userTypes = new HashSet<>();
		RequestInfo requestInfo = userReq.getRequestInfo();
		List<User> users = userReq.getUsers();
		String tenantId = users.get(0).getTenantId();
		Set<String> userNamesForCitizen = new HashSet<>();
		Set<String> userNamesForEmployee = new HashSet<>();

		for (User user : users) {
			
			String userName = user.getUserName()!=null ? user.getUserName() : user.getMobile();
			Type type = user.getType();
			
			if (Type.CITIZEN.equals(type))
				userNamesForCitizen.add(userName);
			else if(Type.EMPLOYEE.equals(type)) 
				userNamesForEmployee.add(userName);
			
			// TODO do we need to throw error back for every item in the list rather than the whole list @once?
			if (!CollectionUtils.isEmpty(errors))
				return errors;

			// TODO why different type of user creation is not allowed?
			validateUserType(user.getType().toString(), userTypes);
			
		}
		
		if(userTypes.contains(Type.EMPLOYEE.toString())) 
			validateUserInfoAndRole(requestInfo.getUserInfo());
		
		validateUserIntegrityForCitizen(errors, tenantId, userNamesForCitizen, userNamesForEmployee, userReq.getRequestInfo());
		
		return errors;
	}
	
	/**
	 * validates the request against the existing data for duplicates.
	 * If the userName from request matches any userName from DB exception will be thrown
	 * 
	 * @param errors
	 * @param tenantId
	 * @param idsForCitizen
	 * @param idsForEmployee
	 */
	private void validateUserIntegrityForCitizen(Map<String, String> errors, String tenantId, Set<String> idsForCitizen,
			Set<String> idsForEmployee, RequestInfo requestInfo) {

		List<String> errorMsgList = new ArrayList<>();
		Set<String> userNamesFromDb = new HashSet<>();

		if (!idsForCitizen.isEmpty()) {

			UserSearchCriteria citizenCriteria = UserSearchCriteria.builder().userNames(idsForCitizen)
					.tenantId(tenantId).build();
			userNamesFromDb.addAll(userService.getUsers(citizenCriteria, requestInfo, true).getUsers().parallelStream()
					.map(User::getUserName).collect(Collectors.toSet()));
		}

		if (!idsForEmployee.isEmpty()) {

			UserSearchCriteria employeeCriteria = UserSearchCriteria.builder().userNames(idsForEmployee)
					.tenantId(tenantId).build();
			userNamesFromDb.addAll(userService.getUsers(employeeCriteria, requestInfo, false).getUsers()
					.parallelStream().map(User::getUserName).collect(Collectors.toSet()));
		}

		if (userNamesFromDb.isEmpty())
			return;

		userNamesFromDb.forEach(userName -> {

			if (idsForCitizen.contains(userName))
				errorMsgList.add(userName);
			if (idsForEmployee.contains(userName))
				errorMsgList.add(userName);
		});
		if (!errorMsgList.isEmpty())
			errors.put(UserErrorConstant.USER_CREATE_DUPLICATE_USER_KEY,
					UserErrorConstant.USER_CREATE_DUPLICATE_USER_MSG + errorMsgList.toString());
	}

	/**
	 * validating if there are multiple user type in single transaction, 
	 * user create api support single type user at a time.
	 *  
	 * @param type pass user type(eg: EMPLOYEE,CITIZEN)
	 * */
	private void validateUserType(String type, Set<String> userTypes) {
		
		userTypes.add(type);
		if (userTypes.size() > 1)
			errors.put(UserErrorConstant.MULTIPLE_USER_TYPE_NOT_SUPPORTED,
					UserErrorConstant.MULTIPLE_USER_TYPE_NOT_SUPPORTED_MSG);
	}
	
	/**
	 * Validating user role to create user as Employee, 
	 * this is the application.properties configuration. 
	 * Only some role can create user as Employee in the system.
	 * 
	 * @param userInfo , user info from RequestInfo.
	 * 
	 * */
	private void validateUserInfoAndRole(UserInfo userInfo) {
		Boolean hasRole = false;
		if(userInfo != null) {
			for(Role role : userInfo.getPrimaryrole()) {
				if(userConfiguration.getRoleForCreateUserAsEmp().contains(role.getCode())){
		        	hasRole = true;
		        	break;
		        }
			}	
		} else {
			errors.put(UserErrorConstant.USER_INFO_NOT_NULL, UserErrorConstant.USER_INFO_NOT_NULL_MSG);
		}
	/*	if(!hasRole)
			errors.put(UserErrorConstant.USER_CREATE_INVALID_USER_ROLE, UserErrorConstant.USER_CREATE_INVALID_USER_ROLE_MSG);*/		
	}
}
