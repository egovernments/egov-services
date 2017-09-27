package org.egov.notificationConsumer;

import java.util.Map;

import org.egov.models.PropertyRequest;
import org.egov.notification.config.PropertiesManager;
import org.egov.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Consumer class
 * 
 * @author Yosadhara
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

	/**
	 * This is receive method for consuming record from Kafka server
	 * 
	 * @param consumerRecord
	 */
	@KafkaListener(topics = { "#{propertiesManager.getDemandAcknowledgement()}",
			"#{propertiesManager.getDemandApprove()}", "#{propertiesManager.getDemandTransferfee()}",
			"#{propertiesManager.getDemandReject()}", "#{propertiesManager.getPropertyAcknowledgement()}",
			"#{propertiesManager.getApproveProperty()}", "#{propertiesManager.getRejectProperty()}",
			"#{propertiesManager.getRevisionPetitionAcknowledgement()}",
			"#{propertiesManager.getRevisionPetitionHearing()}",
			"#{propertiesManager.getRevisionPetitionEndorsement()}",
			"#{propertiesManager.getPropertyAlterationAcknowledgement()}",
			"#{propertiesManager.getApprovePropertyAlteration()}",
			"#{propertiesManager.getRejectPropertyAlteration()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper objectMapper = new ObjectMapper();
		PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + consumerRecord);
		if (topic.equalsIgnoreCase(propertiesManager.getDemandAcknowledgement())) {

			notificationService.demandAcknowledgement(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandApprove())) {

			notificationService.demandApprove(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandTransferfee())) {

			notificationService.demandTransferFee(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandReject())) {

			notificationService.demandReject(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getPropertyAcknowledgement())) {

			notificationService.propertyAcknowledgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getApproveProperty())) {

			notificationService.propertyApprove(propertyRequest.getProperties(),propertyRequest.getRequestInfo());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRejectProperty())) {

			notificationService.propertyReject(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionAcknowledgement())) {

			notificationService.revisionPetitionAcknowldgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionHearing())) {

			notificationService.revisionPetitionHearing(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionEndorsement())) {

			notificationService.revisionPetitionEndorsement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getPropertyAlterationAcknowledgement())) {

			notificationService.alterationAcknowledgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getApprovePropertyAlteration())) {

			notificationService.approvePropertyAlteration(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRejectPropertyAlteration())) {

			notificationService.rejectPropertyAlteration(propertyRequest.getProperties());
		}
	}
}
