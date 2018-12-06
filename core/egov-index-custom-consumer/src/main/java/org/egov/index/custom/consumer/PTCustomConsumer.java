package org.egov.index.custom.consumer;

import java.util.HashMap;

import org.egov.index.custom.models.pt.PropertyRequest;
import org.egov.index.custom.service.PTCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PTCustomConsumer {
	
	@Autowired
	private PTCustomService service;

	@KafkaListener(topics = {"${kafka.topics.pt.update.custom}"})

	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper mapper = new ObjectMapper();
		PropertyRequest propertyRequest = mapper.convertValue(record, PropertyRequest.class);
		service.dataTransformForPTUpdate(propertyRequest);	
	}
}
