package org.egov.propertyUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Import({TracerConfiguration.class})
public class PtUserValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtUserValidatorApplication.class, args);
	}
}
