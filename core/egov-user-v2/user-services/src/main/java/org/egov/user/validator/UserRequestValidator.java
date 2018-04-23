package org.egov.user.validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.Role;
import org.egov.common.contract.request.Type;
import org.egov.common.contract.request.UserInfo;
import org.egov.user.contract.User;
import org.egov.user.contract.UserReq;
import org.egov.user.utils.UserConfiguration;
import org.egov.user.utils.UserErrorConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class UserRequestValidator {
	
	@Autowired
	private UserConfiguration userConfiguration;

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

		for (User user : users) {
			
			// TODO do we need to throw error back for every item in the list rather than the whole list @once?
			if (!CollectionUtils.isEmpty(errors))
				return errors;

			// TODO why different type of user creation is not allowed?
			validateUserType(user.getType().toString(), userTypes);
			
		}
		
		if(userTypes.contains(Type.EMPLOYEE.toString())) 
			validateUserInfoAndRole(requestInfo.getUserInfo());
		
		return errors;
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
		Boolean hasRole = null;
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
		if(!hasRole)
			errors.put(UserErrorConstant.USER_CREATE_INVALID_USER_ROLE, UserErrorConstant.USER_CREATE_INVALID_USER_ROLE_MSG);		
	}
}
