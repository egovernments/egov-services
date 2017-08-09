package org.egov.tradelicense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class TlServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlServicesApplication.class, args);
	}
	

	/**
	 * This method will create rest template object
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
