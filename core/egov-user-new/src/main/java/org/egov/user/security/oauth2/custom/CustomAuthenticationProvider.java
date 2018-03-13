package org.egov.user.security.oauth2.custom;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.egov.common.contract.request.RequestInfo;
import org.egov.user.domain.model.SecureUser;
import org.egov.user.web.contract.User;
import org.egov.user.domain.model.UserSearchCriteria;
import org.egov.user.domain.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	/**
	 * TO-Do:Need to remove this and provide authentication for web, based on
	 * authentication_code.
	 */

	@Value("${user.login.password.otp.enabled}")
	private boolean isUserLoginPasswordOtpEnabled;

	private UserService userService;

	private static String tenantId;

	public CustomAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	public static String getTenantId() {
		return CustomAuthenticationProvider.tenantId;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {

		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();
		CustomAuthenticationProvider.tenantId = getTenantId(authentication);
		User user;
		RequestInfo requestInfo = RequestInfo.builder().action("search").build();

		if (userName.contains("@") && userName.contains(".")) {
			UserSearchCriteria searchCriteria = UserSearchCriteria.builder().emailId(userName).tenantId(tenantId)
					.build();
			user = userService.searchUsers(requestInfo, searchCriteria).get(0);
		} else {
			UserSearchCriteria searchCriteria = UserSearchCriteria.builder().userName(userName).tenantId(tenantId)
					.build();
			user = userService.searchUsers(requestInfo, searchCriteria).get(0);
		}
		if (user == null) {
			throw new OAuth2Exception("Invalid login credentials");
		}

		System.out.println("tenantId in authenticate------->" + user.getTenantId());

		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

		Boolean isPasswordMatch;
		if (isUserLoginPasswordOtpEnabled) {
			Long createdTime = user.getAuditDetails().getLastModifiedTime() / 1000;
			Long currentTime = new Date().getTime() / 1000;
			if (!(currentTime - createdTime <= 60))
				throw new OAuth2Exception("Time Got Expired , Please Generate New Otp And Try to login ");

			isPasswordMatch = password.equals(user.getPassword());
		} else {
			isPasswordMatch = bcrypt.matches(password, user.getPassword());
		}

		if (isPasswordMatch) {

			if (user.getActive() == null || !user.getActive()) {
				throw new OAuth2Exception("Please activate your account");
			}
			/**
			 * We assume that there will be only one type. If it is multiple
			 * then we have change below code Separate by comma or other and
			 * iterate
			 */
			List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));
			final SecureUser secureUser = new SecureUser(getUser(user));
			System.out.println("tenantId in secureUser------->" + secureUser.getTenantId());
			return new UsernamePasswordAuthenticationToken(secureUser, password, grantedAuths);
		} else {
			throw new OAuth2Exception("Invalid login credentials");
		}
	}

	@SuppressWarnings("unchecked")
	private String getTenantId(Authentication authentication) {
		final LinkedHashMap<String, String> details = (LinkedHashMap<String, String>) authentication.getDetails();

		System.out.println("details------->" + details);
		System.out.println("tenantId in CustomAuthenticationProvider------->" + details.get("tenantId"));

		final String tenantId = details.get("tenantId");
		if (isEmpty(tenantId)) {
			throw new OAuth2Exception("TenantId is mandatory");
		}
		return tenantId;
	}

	private org.egov.user.web.contract.auth.User getUser(User user) {
		return org.egov.user.web.contract.auth.User.builder().id(user.getId()).userName(user.getUserName())
				.name(user.getName()).mobileNumber(user.getMobileNumber()).emailId(user.getEmailId())
				.locale(user.getLocale()).active(user.getActive()).type(user.getType())
				.roles(toAuthRole(user.getRoles())).tenantId(user.getTenantId()).build();
	}

	private List<org.egov.user.web.contract.auth.Role> toAuthRole(List<org.egov.user.web.contract.Role> domainRoles) {
		if (domainRoles == null)
			return new ArrayList<>();
		return domainRoles.stream().map(org.egov.user.web.contract.auth.Role::new).collect(Collectors.toList());
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}