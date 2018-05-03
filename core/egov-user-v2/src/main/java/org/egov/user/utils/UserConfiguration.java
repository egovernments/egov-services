package org.egov.user.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class UserConfiguration {

	@Value("#{'${allowed.roles_codes.employee.creation}'.split(',')}")
	private List<String> roleForCreateUserAsEmp;

	@Value("${kafka.topics.save.user}")
	private String createUserTopic;

	@Value("${citizen.reg.otp.enable}")
	private Boolean isCitizenRegOtpEnable;

	@Value("${citizen.reg.login.enable}")
	private Boolean isCitizenLoginOtpEnable;

	@Value("${employee.reg.otp.enable}")
	private Boolean isEmployeeRegOtpEnable;

	@Value("${employee.reg.login.enable}")
	private Boolean isEmployeeLoginOtpEnable;
	
	@Value("${enable.fixed.otp}")
	private Boolean isFixedOtp;
	
	@Value("${egov.infra.searcher.host}")
	private String searcherHost;
	
	@Value("${egov.infra.searcher.endpoint}")
	private String searcherEndpoint;

}
