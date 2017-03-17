
package org.egov.user.security.oauth2.custom;

import org.egov.user.domain.model.User;
import org.egov.user.domain.service.UserService;
import org.egov.user.persistence.enums.UserType;
import org.egov.user.web.contract.auth.SecureUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.egov.user.persistence.entity.EnumConverter.toEnumType;

@Component
public class CustomUserDetailService implements UserDetailsService {
	private final UserService userService;

	@Autowired
	public CustomUserDetailService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = this.userService.getUserByUsername(username);
		return new SecureUser(getUser(user));
	}

	private org.egov.user.web.contract.auth.User getUser(User user) {
		return org.egov.user.web.contract.auth.User.builder()
				.id(user.getId())
				.userName(user.getUsername())
				.name(user.getName())
				.mobileNumber(user.getMobileNumber())
				.emailId(user.getEmailId())
				.locale(user.getLocale())
				.active(user.isActive())
				.type(getUserType(user))
				.roles(toAuthRole(user.getRoles()))
				.build();
	}

	private String getUserType(User user) {
		return toEnumType(UserType.class, user.getType()).name();
	}

	private List<org.egov.user.web.contract.auth.Role> toAuthRole(List<org.egov.user.domain.model.Role> domainRoles) {
		if (domainRoles == null) return new ArrayList<>();
		return domainRoles.stream().map(org.egov.user.web.contract.auth.Role::new)
				.collect(Collectors.toList());
	}
}
