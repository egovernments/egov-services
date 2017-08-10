package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.PropertyRequest;
import org.egov.property.config.PropertiesManager;
import org.egov.property.services.PersisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer class will use for listing property object from kafka server to
 * insert data in postgres database
 * 
 * @author: S Anilkumar
 */
@Service
@Slf4j
@EnableKafka
public class PropertyConsumer {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PersisterService persisterService;	
	

	/**
	 * receive method
	 * 
	 * @param PropertyRequest
	 *            This method is listened whenever property is created and
	 *            updated
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreateWorkflow()}",
			"#{propertiesManager.getUpdateWorkflow()}","#{propertiesManager.getApproveWorkflow()}" })
	
	 public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + propertyRequest);
		if (topic
				.equalsIgnoreCase(propertiesManager.getCreateWorkflow())) {
			persisterService.addProperty(propertyRequest);
		}

		else {
			persisterService.updateProperty(propertyRequest);
		}
	}
}