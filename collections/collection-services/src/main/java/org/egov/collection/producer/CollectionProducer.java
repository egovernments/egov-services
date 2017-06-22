package org.egov.collection.producer;


import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CollectionProducer {

	public static final Logger logger = LoggerFactory.getLogger(CollectionProducer.class);

    private LogAwareKafkaTemplate<String, Object> kafkaTemplate;


    public void producer(String topicName, String key, Object value) {
    	logger.info("Value being pushed to the queue: "+value.toString());
        kafkaTemplate.send(topicName, key, value);
    }
}
