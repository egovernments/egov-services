package org.egov.collection.producer;


import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowProducer {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowProducer.class);

	@Autowired
    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

    public void producer(String topicName, String key, Object value) {
    	logger.info("Value: "+value.toString());
    	logger.info("Topic: "+topicName.toString());
    	logger.info("Key: "+key.toString());
        
    	kafkaTemplate.send(topicName, key, value);
    	
    }
}
