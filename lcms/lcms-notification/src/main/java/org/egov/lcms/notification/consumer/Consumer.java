package org.egov.lcms.notification.consumer;

import java.util.Map;

import org.egov.lcms.notification.config.PropertiesManager;
import org.egov.lcms.notification.model.AdvocatePaymentRequest;
import org.egov.lcms.notification.model.CaseRequest;
import org.egov.lcms.notification.model.OpinionRequest;
import org.egov.lcms.notification.model.SummonRequest;
import org.egov.lcms.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Consumer {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	KafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	NotificationService notificationService;
	
	/**
	 * Description: For consuming records from Kafka server
	 * 
	 * @param consumerRecord
	 * @param topic
	 * @throws Exception
	 */
	@KafkaListener(topics = { 
			"#{propertiesManager.getSummonTopic()}", 
			"#{propertiesManager.getAssigningAdvocateTopic()}",
			"#{propertiesManager.getCaseRegistrationTopic()}",
			"#{propertiesManager.getVakalatnamaGenerationTopic()}",
			"#{propertiesManager.getParawiseCommentsTopic()}",
			"#{propertiesManager.getHearingProcessdetailsTopic()}",
			"#{propertiesManager.getAdvocatePaymentTopic()}",
			"#{propertiesManager.getUpdateAdvocatePaymentTopic()}",
			"#{propertiesManager.getOpinionTopic()}"})
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		
		log.info("lcms-notification consumer topic is: " + topic + " and value is" + consumerRecord);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		if (topic.equalsIgnoreCase(propertiesManager.getSummonTopic())) {
			
			SummonRequest summonRequest = objectMapper.convertValue(consumerRecord, SummonRequest.class);			
			notificationService.sendEmailAndSmsForSummon(summonRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getAssigningAdvocateTopic())) {
			
			CaseRequest caseRequest = objectMapper.convertValue(consumerRecord, CaseRequest.class);
			notificationService.sendEmailAndSmsForAssignAdvocate(caseRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getCaseRegistrationTopic())) {
			
			CaseRequest caseRequest = objectMapper.convertValue(consumerRecord, CaseRequest.class);
			notificationService.sendEmailAndSmsForCaseRegistration(caseRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getVakalatnamaGenerationTopic())) {
			
			CaseRequest caseRequest = objectMapper.convertValue(consumerRecord, CaseRequest.class);
			notificationService.sendEmailAndSmsForVakalatnama(caseRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getParawiseCommentsTopic())) {
			
			CaseRequest caseRequest = objectMapper.convertValue(consumerRecord, CaseRequest.class);
			notificationService.sendEmailAndSmsForParawiseComments(caseRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getHearingProcessdetailsTopic())) {
			
			CaseRequest caseRequest = objectMapper.convertValue(consumerRecord, CaseRequest.class);
			notificationService.sendEmailAndSmsForHearingProcessDetails(caseRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getAdvocatePaymentTopic())) {
			
			AdvocatePaymentRequest advocatePaymentRequest = objectMapper.convertValue(consumerRecord, AdvocatePaymentRequest.class);
			notificationService.sendEmailAndSmsForAdvocatePayment(advocatePaymentRequest);
			
		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdateAdvocatePaymentTopic())) {
			
			AdvocatePaymentRequest advocatePaymentRequest = objectMapper.convertValue(consumerRecord, AdvocatePaymentRequest.class);
			notificationService.sendEmailAndSmsForUpdateAdvocatePayment(advocatePaymentRequest);

		} else if (topic.equalsIgnoreCase(propertiesManager.getOpinionTopic())) {
			
			OpinionRequest opinionRequest = objectMapper.convertValue(consumerRecord, OpinionRequest.class);
			notificationService.sendEmailAndSmsForOpinion(opinionRequest);
		}
	}
}