package org.egov.demand.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	private KafkaTemplate kafkaTemplate;
	
	public void push(String topic, Object value) {
		kafkaTemplate.send(topic, value);
		
	}
}
