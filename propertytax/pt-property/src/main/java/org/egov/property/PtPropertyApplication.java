package org.egov.property;

import org.egov.models.ResponseInfoFactory;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description : This is initialization  class for  pt-property module
 * @author narendra
 *
 */

@SpringBootApplication
@Import({TracerConfiguration.class})
@EnableWebMvc 
public class PtPropertyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PtPropertyApplication.class, args);

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