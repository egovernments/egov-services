package org.egov.propertyUser;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({TracerConfiguration.class})
public class PtUserValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtUserValidatorApplication.class, args);
	}
}
