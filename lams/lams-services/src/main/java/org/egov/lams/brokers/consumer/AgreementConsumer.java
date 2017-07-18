package org.egov.lams.brokers.consumer;

import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.web.contract.AgreementRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementConsumer {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AgreementRepository agreementRepository;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics ={"${kafka.topics.save.agreement}","${kafka.topics.update.agreement}"})
	public void listen(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		log.info("received message");
		AgreementRequest agreementRequest = null;
		
		try{
			agreementRequest = objectMapper.convertValue(consumerRecord, AgreementRequest.class);
		}catch (Exception exception) {
			log.debug("processMessage:" + exception);
			throw exception;
		}
		
		if (propertiesManager.getSaveAgreementTopic().equals(topic)) {
			agreementRepository.saveAgreement(agreementRequest);
		} else if (propertiesManager.getUpdateAgreementTopic().equals(topic)) {
			log.info("SaveAgreementConsumer agreement-update-db :" + agreementRequest);
			agreementRepository.updateAgreement(agreementRequest);
		}
	}
}
	
	