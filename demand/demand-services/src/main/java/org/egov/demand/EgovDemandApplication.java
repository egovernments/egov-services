package org.egov.demand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EgovDemandApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgovDemandApplication.class, args);
	}
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
