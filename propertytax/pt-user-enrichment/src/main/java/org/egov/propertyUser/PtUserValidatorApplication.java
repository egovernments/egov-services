package org.egov.propertyUser;

import org.egov.propertyUser.config.PropertiesManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@Import({TracerConfiguration.class})
public class PtUserValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtUserValidatorApplication.class, args);
	}
	
	@Bean
	public PropertiesManager getPropertiesManager() {
		return new PropertiesManager();
	}
}
