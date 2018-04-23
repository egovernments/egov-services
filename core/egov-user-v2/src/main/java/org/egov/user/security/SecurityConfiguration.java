package org.egov.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder managerBuilder ) throws Exception{
		managerBuilder.authenticationProvider(customAuthenticationProvider);
	}
}
