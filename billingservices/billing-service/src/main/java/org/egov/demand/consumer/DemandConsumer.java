package org.egov.demand.consumer;

import java.util.HashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class DemandConsumer {
	
	@KafkaListener(topics = {"${kafka.topics.save.bill}","${kafka.topics.update.bill}"})
    public void processMessage(HashMap<String, Object> consumerRecord,
    		@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		//TODO
    }
}
