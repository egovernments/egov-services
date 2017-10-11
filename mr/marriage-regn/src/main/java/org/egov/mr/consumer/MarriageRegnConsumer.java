package org.egov.mr.consumer;

import java.util.Map;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.model.Fee;
import org.egov.mr.model.ReissueCertAppl;
import org.egov.mr.service.FeeService;
import org.egov.mr.service.MarriageCertService;
import org.egov.mr.service.MarriageDocumentTypeService;
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.service.RegistrationUnitService;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.MarriageDocTypeRequest;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.RegnUnitRequest;
import org.egov.mr.web.contract.ReissueCertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MarriageRegnConsumer {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RegistrationUnitService registrationUnitService;

	@Autowired
	private MarriageRegnService marriageRegnService;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MarriageCertService marriageCertService;

	@Autowired
	private MarriageDocumentTypeService marriageDocumentTypeService;

	@Autowired
	private FeeService feeService;

	@KafkaListener(topics = { "${kafka.topics.create.fee}", "${kafka.topics.update.fee}",
			"${kafka.topics.create.reissueCertificate}", "${kafka.topics.update.reissueappl}",
			"${kafka.topics.create.reissueappl}", "${kafka.topics.create.registrationunit}",
			"${kafka.topics.update.registrationunit}", 
			 "${kafka.topics.create.marriagedocumenttype}",
			"${kafka.topics.update.marriagedocumenttype}" })
	public void processMessage(@Payload Map<String, Object> consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Entered kakfaConsumer "+consumerRecord);

		if (topic.equals(propertiesManager.getCreateMarriageRegnTopicName())) {
			log.info("entering create marriageRegn consumer");
			marriageRegnService.create(objectMapper.convertValue(consumerRecord, MarriageRegnRequest.class));
		}
		if (topic.equals(propertiesManager.getUpdateMarriageRegnTopicName())) {
			log.info("entering update marriageRegn consumer");
			marriageRegnService.update(objectMapper.convertValue(consumerRecord, MarriageRegnRequest.class));
		}

		if (topic.equals(propertiesManager.getCreateRegistrationUnitTopicName())) {
			log.info("entering update_RegistrationUnit consumer");
			registrationUnitService.create(objectMapper.convertValue(consumerRecord, RegnUnitRequest.class));
		}
		if (topic.equals(propertiesManager.getUpdateRegistrationUnitTopicName())) {
			log.info("entering update_RegistrationUnit consumer");
			registrationUnitService.update(objectMapper.convertValue(consumerRecord, RegnUnitRequest.class));
		}

		if (topic.equals(propertiesManager.getCreateMarriageDocumentTypeTopicName())) {
			marriageDocumentTypeService.create(objectMapper.convertValue(consumerRecord, MarriageDocTypeRequest.class));
		}
		if (topic.equals(propertiesManager.getUpdateMarriageDocumentTypeTopicName())) {
			marriageDocumentTypeService.update(objectMapper.convertValue(consumerRecord, MarriageDocTypeRequest.class));
		}
		if (topic.equals(propertiesManager.getCreateReissueMarriageRegnTopicName()))
			marriageCertService.create(objectMapper.convertValue(consumerRecord, ReissueCertRequest.class));
		if (topic.equals(propertiesManager.getUpdateReissueMarriageRegnTopicName()))
			marriageCertService.update(objectMapper.convertValue(consumerRecord, ReissueCertRequest.class));
		if (topic.equals(propertiesManager.getCreateReissueCertificateTopicName()))
			marriageCertService.createCert(objectMapper.convertValue(consumerRecord, ReissueCertAppl.class));
		if (topic.equals(propertiesManager.getCreateFeeTopicName()))
			feeService.createFee(objectMapper.convertValue(consumerRecord, FeeRequest.class));
		if (topic.equals(propertiesManager.getUpdateFeeTopicName()))
			feeService.updateFee(objectMapper.convertValue(consumerRecord, FeeRequest.class));
	}
}