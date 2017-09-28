package org.egov.notificationConsumer;

import java.util.Map;

import org.egov.models.PropertyRequest;
import org.egov.models.TitleTransfer;
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
			"#{propertiesManager.getRejectPropertyAlteration()}",			
			"#{propertiesManager.getVacancyRemissionAcknowledgementTopic()}",
			"#{propertiesManager.getVacancyRemissionApproveTopic()}",
			"#{propertiesManager.getVacancyRemissionRejectTopic()}",			
			"#{propertiesManager.getTitleTransferAcknowledgementTopic()}",
			"#{propertiesManager.getTitleTransferApproveTopic()}",
			"#{propertiesManager.getTitleTransferRejectTopic()}"})
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		ObjectMapper objectMapper = new ObjectMapper();
		log.info("consumer topic value is: " + topic + " consumer value is" + consumerRecord);
		if (topic.equalsIgnoreCase(propertiesManager.getDemandAcknowledgement())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.demandAcknowledgement(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandApprove())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.demandApprove(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandTransferfee())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.demandTransferFee(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getDemandReject())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.demandReject(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getPropertyAcknowledgement())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.propertyAcknowledgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getApproveProperty())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.propertyApprove(propertyRequest.getProperties(),propertyRequest.getRequestInfo());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRejectProperty())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.propertyReject(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionAcknowledgement())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.revisionPetitionAcknowldgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionHearing())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.revisionPetitionHearing(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRevisionPetitionEndorsement())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.revisionPetitionEndorsement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getPropertyAlterationAcknowledgement())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.alterationAcknowledgement(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getApprovePropertyAlteration())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.approvePropertyAlteration(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getRejectPropertyAlteration())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);
			notificationService.rejectPropertyAlteration(propertyRequest.getProperties());
		} else if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionAcknowledgementTopic())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);			
			notificationService.acknowledgeVacancyRemission(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionApproveTopic())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);			
			notificationService.approveVacancyRemission(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getVacancyRemissionRejectTopic())) {
			
			PropertyRequest propertyRequest = objectMapper.convertValue(consumerRecord, PropertyRequest.class);			
			notificationService.rejectVacancyRemission(propertyRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getTitleTransferAcknowledgementTopic())) {
			
			TitleTransfer titleTransfer = objectMapper.convertValue(consumerRecord, TitleTransfer.class);			
			notificationService.acknowledgeTitleTransfer(titleTransfer);
		} else if (topic.equalsIgnoreCase(propertiesManager.getTitleTransferApproveTopic())) {
			
			TitleTransfer titleTransfer = objectMapper.convertValue(consumerRecord, TitleTransfer.class);			
			notificationService.approveTitleTransfer(titleTransfer);
		} else if (topic.equalsIgnoreCase(propertiesManager.getTitleTransferRejectTopic())) {
			
			TitleTransfer titleTransfer = objectMapper.convertValue(consumerRecord, TitleTransfer.class);			
			notificationService.rejectTitleTransfer(titleTransfer);
		}
	}
}
