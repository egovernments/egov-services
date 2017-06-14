package org.egov.lams.brokers.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.repository.AgreementRepository;
import org.egov.lams.web.contract.AgreementRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AgreementConsumer {
	
	@Autowired
	private ObjectMapper objectMapper;

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementConsumer.class);
	
	@Autowired
	private AgreementRepository agreementRepository;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics ={"${kafka.topics.save.agreement}","${kafka.topics.update.agreement}"})
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:"+ record.key() +":"+ "value:" +record.value());
		
		AgreementRequest agreementRequest = null;
		
		try{
			agreementRequest = objectMapper.readValue(record.value(), AgreementRequest.class);
		}catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		
		if (propertiesManager.getSaveAgreementTopic().equals(record.topic())) {
			agreementRepository.saveAgreement(agreementRequest);
		} else if (propertiesManager.getUpdateAgreementTopic().equals(record.topic())) {
			LOGGER.info("SaveAgreementConsumer agreement-update-db :" + agreementRequest);
			agreementRepository.updateAgreement(agreementRequest);
		}
	}
}
	
	