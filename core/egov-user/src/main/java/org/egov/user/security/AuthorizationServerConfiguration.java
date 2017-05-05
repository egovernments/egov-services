package org.egov.user.security;

import org.egov.user.security.oauth2.custom.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${access.token.validity.in.minutes}")
	private int accessTokenValidityInMinutes;

	@Value("${refresh.token.validity.in.minutes}")
	private int refreshTokenValidityInMinutes;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomTokenEnhancer customTokenEnhancer;

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		final int accessTokenValidityInSeconds = accessTokenValidityInMinutes * 60;
		final int refreshTokenValidityInSeconds = refreshTokenValidityInMinutes * 60;
		clients.inMemory()
				.withClient("egov-user-client")
				.secret("egov-user-secret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "password")
				.authorities("ROLE_APP", "ROLE_CITIZEN", "ROLE_ADMIN", "ROLE_EMPLOYEE")
				.scopes("read", "write")
				.refreshTokenValiditySeconds(refreshTokenValidityInSeconds)
				.accessTokenValiditySeconds(accessTokenValidityInSeconds);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore)
				.authenticationManager(authenticationManager)
				.tokenEnhancer(customTokenEnhancer);
	}

	@Bean
	public JedisConnectionFactory connectionFactory() throws Exception {
		return new JedisConnectionFactory(new JedisShardInfo(host));
	}

}
