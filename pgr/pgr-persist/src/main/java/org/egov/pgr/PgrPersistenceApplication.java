package org.egov.pgr;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableKafka
public class PgrPersistenceApplication extends SpringBootServletInitializer {

	private static final String IST = "Asia/Calcutta";

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(IST));
		SpringApplication.run(PgrPersistenceApplication.class, args);
	}
}