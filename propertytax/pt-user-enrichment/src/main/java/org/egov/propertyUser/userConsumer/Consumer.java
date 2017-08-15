package org.egov.propertyUser.userConsumer;

import java.util.Map;

import org.egov.models.Property;
import org.egov.models.PropertyRequest;
import org.egov.models.User;
import org.egov.propertyUser.config.PropertiesManager;
import org.egov.propertyUser.util.UpicNoGeneration;
import org.egov.propertyUser.util.UserUtil;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * Consumer class will use for listing property object from kafka server.
 * Authenticate the user Search the user Create the user If user exist update
 * the user id otherwise create the user
 * 
 * @author: S Anilkumar
 */

@Service
@Slf4j
public class Consumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private UserUtil userUtil;
	
	@Autowired
	private UpicNoGeneration upicNoGeneration;

	/*
	 * This method for creating rest template
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreatePropertyValidator()}",
			"#{propertiesManager.getUpdatePropertyValidator()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + propertyRequest);
		for (Property property : propertyRequest.getProperties()) {
			for (User user : property.getOwners()) {
				user.setUserName(user.getMobileNumber());

				user = userUtil.getUserId(user, propertyRequest.getRequestInfo());

			}

			if (!property.getChannel().toString().equalsIgnoreCase(propertiesManager.getChannelType())) {
				if (topic.equalsIgnoreCase(propertiesManager.getCreatePropertyValidator())) {

					kafkaTemplate.send(propertiesManager.getCreatePropertyUserValidator(), propertyRequest);

				} else if (topic.equalsIgnoreCase(propertiesManager.getUpdatePropertyValidator())) {

					kafkaTemplate.send(propertiesManager.getUpdatePropertyUserValidator(), propertyRequest);

				}

			} else {
				String upicNo = upicNoGeneration.generateUpicNo(property, propertyRequest.getRequestInfo());
				propertyRequest.getProperties().get(0).setUpicNumber(upicNo);
				kafkaTemplate.send(propertiesManager.getCreateWorkflow(), propertyRequest);
			}
		}
	}

}
