package org.egov.cosumer;

import java.util.HashMap;

import org.egov.service.SignOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomConsumer {

	@Autowired
	private SignOutService signOutService;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = { "${egov.custom.async.filter.topic}" })
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

		log.info("CustomConsumer received request from topic: " + topic);
		log.info("data: " + record);
		
		try {
			String inputJson = objectMapper.writeValueAsString(record);
			DocumentContext documentContext = JsonPath.parse(inputJson);
			String sourceUri = documentContext.read("$.sourceUri");
			signOutService.callFinanceForSignOut(documentContext);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// ObjectMapper mapper = new ObjectMapper();

	}

}
