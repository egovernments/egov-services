package org.egov.user.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	
	public void push(String topic, Object value) {
    	try {
    		kafkaTemplate.send(topic, value);
    	} catch (Exception ex) {
    		log.info("Failed to send on kafka"+ ex );
    	}
    }
}
