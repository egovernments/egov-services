package org.egov.user.security.oauth2.custom;

import org.egov.user.domain.model.SecureUser;
import org.egov.user.domain.model.User;
import org.egov.user.domain.service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.isEmpty;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * TO-Do:Need to remove this and provide authentication for web, based on
     * authentication_code.
     */

    private UserService userService;

	public CustomAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
    public Authentication authenticate(Authentication authentication) {

        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        String tenantId = getTenantId(authentication);
        User user;
        if (userName.contains("@") && userName.contains(".")) {
            user = userService.getUserByEmailId(userName, tenantId);
        } else {
            user = userService.getUserByUsername(userName, tenantId);
        }
        if (user == null) {
            throw new OAuth2Exception("Invalid login credentials");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

        if (bcrypt.matches(password, user.getPassword())) {

            if (user.getActive() == null || !user.getActive()) {
                throw new OAuth2Exception("Please activate your account");
            }
            /**
             * We assume that there will be only one type. If it is multimple
             * then we have change below code Seperate by comma or other and
             * iterate
             */
            List<GrantedAuthority> grantedAuths = new ArrayList<>();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + user.getType()));
            final SecureUser secureUser = new SecureUser(getUser(user));
            return new UsernamePasswordAuthenticationToken(secureUser, password,
                    grantedAuths);
        } else {
            throw new OAuth2Exception("Invalid login credentials");
        }
    }

	@SuppressWarnings("unchecked")
	private String getTenantId(Authentication authentication) {
		final LinkedHashMap<String, String> details =
				(LinkedHashMap<String, String>) authentication.getDetails();
		final String tenantId = details.get("tenantId");
		if (isEmpty(tenantId)) {
			throw new OAuth2Exception("TenantId is mandatory");
		}
		return tenantId;
	}

	private org.egov.user.web.contract.auth.User getUser(User user) {
        return org.egov.user.web.contract.auth.User.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .name(user.getName())
                .mobileNumber(user.getMobileNumber())
                .emailId(user.getEmailId())
                .locale(user.getLocale())
                .active(user.getActive())
                .type(user.getType().name())
                .roles(toAuthRole(user.getRoles()))
                .tenantId(user.getTenantId())
                .build();
    }

    private List<org.egov.user.web.contract.auth.Role> toAuthRole(List<org.egov.user.domain.model.Role> domainRoles) {
        if (domainRoles == null) return new ArrayList<>();
        return domainRoles.stream().map(org.egov.user.web.contract.auth.Role::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}