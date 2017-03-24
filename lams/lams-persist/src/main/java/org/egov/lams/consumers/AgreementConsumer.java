package org.egov.lams.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.dao.AgreementDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AgreementConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementConsumer.class);
	
	@Autowired
	private AgreementDao agreementDao;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@KafkaListener(containerFactory="kafkaListenerContainerFactory",topics ={"${kafka.topics.save.agreement}","${kafka.topics.update.agreement}"})
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("key:"+ record.key() +":"+ "value:" +record.value());
		
		ObjectMapper objectMapper = new ObjectMapper();
		AgreementRequest agreementRequest = null;
		
		try{
			agreementRequest = objectMapper.readValue(record.value(), AgreementRequest.class);
		}catch (Exception e) {
			LOGGER.info(e.getMessage(), e);
		}
		
		if (propertiesManager.getKafkaSaveAgreementTopic().equals(record.topic())) {
			agreementDao.saveAgreement(agreementRequest);
		} else if (propertiesManager.getKafkaUpdateAgreementTopic().equals(record.topic())) {
			LOGGER.info("SaveAgreementConsumer agreement-update-db :" + agreementRequest);
			agreementDao.updateAgreement(agreementRequest);
		}
	}
}
	
	