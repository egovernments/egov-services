package org.egov.user;

import org.egov.otp.sevice.OtpService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is mainly to run the user application with embedded tomcat server. 
 * 
 * @author ghanshyamrawat
 * @version 1.1.0
 */

@SpringBootApplication
public class UserApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}
	
	/**
	 * This method create bean instance for ObjectMapper, 
	 * this will set all the default property of ObjectMapper at application level  
	 * 
	 * @return {@link ObjectMapper} 
	 */
	@Bean
	public ObjectMapper getObjectMapper(){
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}
	
	@Bean
	public OtpService getOtpservice() {
		return new OtpService();
	}
}
