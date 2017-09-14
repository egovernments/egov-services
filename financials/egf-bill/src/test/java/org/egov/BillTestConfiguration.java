package org.egov;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class BillTestConfiguration {

	@Bean
	@SuppressWarnings("unchecked")
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return mock(KafkaTemplate.class);
	}

}
