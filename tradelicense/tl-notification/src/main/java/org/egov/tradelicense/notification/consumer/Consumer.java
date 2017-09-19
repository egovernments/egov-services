package org.egov.tradelicense.notification.consumer;

import java.util.Map;

import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.notification.config.PropertiesManager;
import org.egov.tradelicense.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Consumer class
 * 
 * @author Shubham
 *
 */
@Service
@Slf4j
public class Consumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NotificationService notificationService;

	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * This is receive method for consuming record from Kafka server
	 * 
	 * @param consumerRecord
	 */

	@KafkaListener(topics = { "#{propertiesManager.getTradeLicenseValidatedTopic()}" })
	public void receive(Map<String, Object> mastersMap) {

		if (mastersMap.get("tradelicense-new-create") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-create"),
					TradeLicenseRequest.class);

			notificationService.licenseNewCreationAcknowledgement(request);
		}
		if (mastersMap.get("tradelicense-new-update") != null) {

			TradeLicenseRequest request = objectMapper.convertValue(mastersMap.get("tradelicense-new-update"),
					TradeLicenseRequest.class);

			notificationService.processNewLicenseUpdateAcknowledgement(request);
		}
	}
}
