package org.egov.mr.broker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.service.RegistrationUnitService;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MarriageRegnConsumer {

	public static final Logger LOGGER = LoggerFactory.getLogger(MarriageRegnConsumer.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RegistrationUnitService registrationUnitService;
	
	@Autowired
	private MarriageRegnService marriageRegnService;

	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {
			"${kafka.topics.create.registrationunit}", "${kafka.topics.update.registrationunit}",
			"${kafka.topics.create.marriageregn}", "${kafka.topics.update.marriageregn}" })
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("Entered kakfaConsumer ");

		if (record.topic().equals(propertiesManager.getCreateMarriageRegnTopicName())) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				LOGGER.info("entering create marriageRegn consumer");
				marriageRegnService.create(objectMapper.readValue(record.value(), MarriageRegnRequest.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (record.topic().equals(propertiesManager.getUpdateMarriageRegnTopicName())) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				LOGGER.info("entering update marriageRegn consumer");
				marriageRegnService.update(objectMapper.readValue(record.value(), MarriageRegnRequest.class));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/*if (record.topic().equals(propertiesManager.getCreateRegistrationUnitTopicName())) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LOGGER.info("entering update_RegistrationUnit consumer");
			registrationUnitService.create(objectMapper.readValue(record.value(), RegnUnitRequest.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else if (record.topic().equals(propertiesManager.getUpdateRegistrationUnitTopicName())) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			LOGGER.info("entering update_RegistrationUnit consumer");
			registrationUnitService.update(objectMapper.readValue(record.value(), RegnUnitRequest.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	}
}
