package org.egov.propertytax;

import org.egov.models.ResponseInfoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@Import({TracerConfiguration.class})
@EnableWebMvc
public class PtTaxEnrichmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtTaxEnrichmentApplication.class, args);
	}

	@Bean
	public ResponseInfoFactory responseInfoFactory(){
		return new ResponseInfoFactory();
	}

	/**
	 * This method will create rest template object
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
