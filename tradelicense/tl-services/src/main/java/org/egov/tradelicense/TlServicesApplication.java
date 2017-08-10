package org.egov.tradelicense;

import org.egov.tracer.config.TracerConfiguration;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@Import({ TracerConfiguration.class })
@SpringBootApplication
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

	@Autowired
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

}