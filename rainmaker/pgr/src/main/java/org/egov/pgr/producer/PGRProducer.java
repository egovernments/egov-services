package org.egov.pgr.producer;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PGRProducer {
	
	@Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
    public void push(String topic, Object value) {
    	log.info("Value: "+value.toString());
    	log.info("Topic: "+topic);
    		kafkaTemplate.send(topic, value);
    	
    }
}
