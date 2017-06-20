package org.egov.id;

import org.egov.models.ResponseInfoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Description : This is initialization class for pt-idGeneration module
 * 
 * @author Pavan Kumar Kamma
 *
 */
@SpringBootApplication
public class PtIdGenerationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtIdGenerationApplication.class, args);
	}

	@Bean
	public ResponseInfoFactory getResponseInfo() {
		return new ResponseInfoFactory();
	}
}
