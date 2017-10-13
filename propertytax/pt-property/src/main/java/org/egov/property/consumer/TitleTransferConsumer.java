package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.TitleTransferRequest;
import org.egov.property.config.PropertiesManager;
import org.egov.property.services.PersisterService;
import org.egov.property.services.PropertyService;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@EnableKafka
@Slf4j
public class TitleTransferConsumer {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PropertyService propertyService;

	@Autowired
	PersisterService persisterService;

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * This method will listen property object from producer and check user
	 * authentication Updating auth token in UserAuthResponseInfo Search user
	 * Create user
	 */
	@KafkaListener(topics = { "#{propertiesManager.getCreatePropertyTitletransferWorkflow()}",
			"#{propertiesManager.getApproveTitletransfer()}",
			"#{propertiesManager.getUpdatePropertyTitletransferWorkflow()}",
			"#{propertiesManager.getCreateTitleTransferUserValidator()}",
			"#{propertiesManager.getUpdateTitleTransferUserValidator()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		TitleTransferRequest titleTransferRequest = objectMapper.convertValue(consumerRecord,
				TitleTransferRequest.class);
		log.info("consumer topic value is: " + topic + " consumer value is" + titleTransferRequest);

		if (topic.equalsIgnoreCase(propertiesManager.getCreatePropertyTitletransferWorkflow())) {
			persisterService.addTitleTransfer(titleTransferRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdatePropertyTitletransferWorkflow())) {
			persisterService.updateTitleTransfer(titleTransferRequest);
		} else if (topic.equalsIgnoreCase(propertiesManager.getCreateTitleTransferUserValidator())) {

			kafkaTemplate.send(propertiesManager.getCreateTitleTransferTaxCalculated(), titleTransferRequest);

		} else if (topic.equalsIgnoreCase(propertiesManager.getUpdateTitleTransferUserValidator())) {

			kafkaTemplate.send(propertiesManager.getUpdateTitleTransferTaxCalculated(), titleTransferRequest);

		}

		else {
			propertyService.savePropertyHistoryandUpdateProperty(titleTransferRequest);

		}
	}

}
