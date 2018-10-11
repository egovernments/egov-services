package org.egov.cosumer;

import java.util.HashMap;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomConsumer {

	@KafkaListener(topics = {"${egov.custom.async.filter.topic}"})
    public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        
		log.info("CustomConsumer received request from topic: "+topic);
		log.info("data: "+record);
		//ObjectMapper mapper = new ObjectMapper();
       
    }
	
}
