package org.egov.user.security.oauth2.custom;

import java.util.ArrayList;
import java.util.List;

import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	private final UserService userService;

	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		org.egov.user.domain.model.User user1=userService.getUserByUsername(username, CustomAuthenticationProvider.getTenantId());
		
		List<org.egov.user.domain.model.Role> roles = user1.getRoles();
		
		List<org.egov.user.web.contract.auth.Role> roleList = new ArrayList<org.egov.user.web.contract.auth.Role>();
		 
		for(org.egov.user.domain.model.Role role : roles){
			org.egov.user.web.contract.auth.Role role1 = new org.egov.user.web.contract.auth.Role(role);
			roleList.add(role1);
		}
		org.egov.user.web.contract.auth.User authuser = org.egov.user.web.contract.auth.User.builder().id(user1.getId()).userName(user1.getUsername()).name(user1.getName()).mobileNumber(user1.getMobileNumber())
				.emailId(user1.getEmailId()).locale(user1.getLocale()).active(user1.getActive()).roles(roleList).tenantId(user1.getTenantId()).type(user1.getType().toString()).build();
		
		SecureUser secureUser = new SecureUser(authuser);
		
		return secureUser;
	}
	
}
