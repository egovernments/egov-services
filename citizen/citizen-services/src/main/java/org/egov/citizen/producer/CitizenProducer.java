package org.egov.citizen.producer;


import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CitizenProducer {

	public static final Logger logger = LoggerFactory.getLogger(CitizenProducer.class);

	@Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String,Object> template;

    public void producer(String topicName, String key, Object value) {
    	logger.info("Value being pushed to the queue: "+value.toString());
    	ObjectMapper objectMapper = new ObjectMapper();
    	String sendingValue = objectMapper.convertValue(value, String.class);
    	//kafkaTemplate.send(topicName, key, value);
    		template.send(topicName, key, sendingValue);
    }
}
