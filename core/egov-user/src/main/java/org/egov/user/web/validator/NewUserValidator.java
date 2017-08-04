package org.egov.user.web.validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.egov.user.model.User;
import org.egov.user.model.UserReq;
import org.egov.user.model.UserRes;
import org.egov.user.model.UserSearchCriteria;
import org.egov.user.service.NewUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NewUserValidator implements Validator{

	@Autowired
	private NewUserService newUserService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		
		return UserReq.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

		UserReq userReq = null;
		if (target instanceof UserReq)
			userReq = (UserReq) target;
		else
			throw new RuntimeException("Invalid Object type for user validator");
		
		validateUsers(userReq,errors);
	}
	
	public void validateUsers(final UserReq userRequest, Errors error) {
		List<User> users=userRequest.getUsers();
		Set<String> userlist=new HashSet<>();
		for(User user:users){
			if(!user.getTenantId().equals(users.get(0).getTenantId()))
				error.rejectValue("Users","","Transaction not allowed for multiple tenant");
			userlist.add(user.getUsername());
		}
		final UserSearchCriteria userCriteria=UserSearchCriteria.builder().tenantId(users.get(0).getTenantId()).userName(userlist).build();
		UserRes userRes = newUserService.search(userCriteria);
		if(!userRes.getUsers().isEmpty()){
			for(User user:userRes.getUsers())
				error.rejectValue("Users","","The User name: "+user.getUsername()+" allready exist");
		}
	}

}
