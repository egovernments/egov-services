package org.egov.user.security.oauth2.custom;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.service.UserServiceVersionv11;
import org.egov.user.domain.v11.model.User;
import org.egov.user.domain.v11.model.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserServiceVersionv11 userService;

	@Autowired
	public CustomUserDetailsService(UserServiceVersionv11 userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		RequestInfo requestInfo = RequestInfo.builder().action("search").build();
		UserSearchCriteria searchCriteria = UserSearchCriteria.builder().userName(username)
				.tenantId(CustomAuthenticationProvider.getTenantId()).build();
		User user = userService.searchUsers(requestInfo, searchCriteria).get(0);

		List<org.egov.user.domain.v11.model.Role> roles = user.getRoles();

		List<org.egov.user.web.contract.auth.Role> roleList = new ArrayList<org.egov.user.web.contract.auth.Role>();

		for (org.egov.user.domain.v11.model.Role role : roles) {
			org.egov.user.web.contract.auth.Role role1 = new org.egov.user.web.contract.auth.Role(role);
			roleList.add(role1);
		}
		org.egov.user.web.contract.auth.User authuser = org.egov.user.web.contract.auth.User.builder().id(user.getId())
				.userName(user.getUserName()).name(user.getName()).mobileNumber(user.getMobileNumber())
				.emailId(user.getEmailId()).locale(user.getLocale()).active(user.getActive()).roles(roleList)
				.tenantId(user.getTenantId()).type(user.getType().toString()).build();

		SecureUser secureUser = new SecureUser(authuser);

		return secureUser;
	}

}
