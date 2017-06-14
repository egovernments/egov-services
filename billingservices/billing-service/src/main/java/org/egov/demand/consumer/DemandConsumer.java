package org.egov.demand.consumer;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DemandConsumer {
	
	@KafkaListener(topics = "test-sp-kaf")
    public void processMessage(ConsumerRecord<String, HashMap<String, Object>> consumerRecord) {
		
    }
}
