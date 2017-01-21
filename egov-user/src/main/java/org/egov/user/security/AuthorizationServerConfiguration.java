package org.egov.user.security;

import org.egov.user.oauth2.provider.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static String REALM = "PGR_REST_OAUTH_REALM";

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomTokenEnhancer customTokenEnhancer;

	/*
	 * @Autowired private CustomAuthenticationProvider
	 * customAuthenticationProvider;
	 * 
	 * @Bean public AuthenticationManager authenticationManager() { return new
	 * ProviderManager(Arrays.asList(customAuthenticationProvider)); }
	 */

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("egov-user-client").secret("egov-user-secret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "password")
				.authorities("ROLE_APP", "ROLE_CITIZEN", "ROLE_ADMIN", "ROLE_EMPLOYEE").scopes("read","write");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager).pathMapping("/oauth/token", "/user/_login")
				.tokenEnhancer(customTokenEnhancer);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.realm(REALM + "/client");
	}

}
