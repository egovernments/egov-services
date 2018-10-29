																																																																																																				package org.egov.pgr.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PGRProducer {
	
	@Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
	
    public void push(String topic, Object value) {
		log.info("Value: " + value.toString());
		log.info("Topic: "+topic);
    		kafkaTemplate.send(topic, value);
    	
    }
}
