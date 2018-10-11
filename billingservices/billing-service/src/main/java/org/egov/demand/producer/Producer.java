package org.egov.demand.producer;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Producer {

	@Autowired
	private LogAwareKafkaTemplate kafkaTemplate;
	
	public void push(String topic, Object value) {
		kafkaTemplate.send(topic, value);
		
	}
}
