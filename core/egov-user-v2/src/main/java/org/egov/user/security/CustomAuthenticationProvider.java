package org.egov.user.security;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static String tenantId;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		String password = authentication.getCredentials().toString();	
		CustomAuthenticationProvider.tenantId = getTenantId(authentication);
		System.out.println("userName:"+userName+","+"lastName:"+password);
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + "ADMIN"));
			
		return new UsernamePasswordAuthenticationToken(userName, password, grantedAuths);
		
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
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

}
