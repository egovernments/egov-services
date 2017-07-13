package org.egov.lams.consumers;

import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementIndex;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.repository.ElasticSearchRepository;
import org.egov.lams.service.AgreementAdaptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LamsIndexerConsumer {

	@Autowired
	private ElasticSearchRepository elasticSearchRepository;

	@Autowired
	private AgreementAdaptorService agreementAdapter;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PropertiesManager PropertiesManager;
	
	@KafkaListener(topics = {"${kafka.topics.save.agreement}","${kafka.topics.update.agreement}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);
		AgreementIndex agreementIndex = new AgreementIndex();
		try {
			log.info("SaveAgreementConsumer agreement-save-db :" + elasticSearchRepository);
			agreementIndex = agreementAdapter.indexOnCreate(objectMapper.convertValue(consumerRecord, AgreementRequest.class));
		} catch (Exception exception) {
			log.debug("processMessage:" + exception);
			throw exception;
		}
		
		if (topic.equals(PropertiesManager.getKafkaSaveAgreementTopic())) {
			elasticSearchRepository.saveAgreement(agreementIndex);
		}
		else if (topic.equals(PropertiesManager.getKafkaUpdateAgreementTopic())) {
			elasticSearchRepository.updateAgreement(agreementIndex);
		}

	}
}