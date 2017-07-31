package org.egov.tradelicense;

import org.egov.models.ResponseInfoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Description : This is initialization class for tl-tradelicense module
 * 
 * @author Pavan Kumar Kamma
 *
 */

@SpringBootApplication
@EnableWebMvc
public class TradeLicenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeLicenseApplication.class, args);

	}

	@Bean
	public ResponseInfoFactory responseInfoFactory() {
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