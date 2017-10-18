package org.egov.property.consumer;

import java.util.Map;

import org.egov.models.DemolitionRequest;
import org.egov.property.config.PropertiesManager;
import org.egov.property.services.PersisterService;
import org.egov.property.utility.UpicNoGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Prasad
 *
 */
@Service
@EnableKafka
public class DemolitionConsumer {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	PersisterService persisterService;

	@Autowired
	UpicNoGeneration upicNoGeneration;

	@KafkaListener(topics = { "#{propertiesManager.getApproveDemolition()}",
			"#{propertiesManager.getCreateDemolitionWorkflow()}", "#{propertiesManager.getRejectDemolition()}",
			"#{propertiesManager.getUpdateDemolitionWorkflow()}" })
	public void receive(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic)
			throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		DemolitionRequest demolitionRequest = objectMapper.convertValue(consumerRecord, DemolitionRequest.class);

		if (topic.equalsIgnoreCase(propertiesManager.getCreateDemolitionWorkflow())) {
			persisterService.saveDemolition(demolitionRequest);
		}

		else if (topic.equalsIgnoreCase(propertiesManager.getApproveDemolition())) {
			persisterService.updateDemolition(demolitionRequest);
			persisterService.savePropertyTohistoryAndUpdateProperty(demolitionRequest);

		}

		else if (topic.equalsIgnoreCase(propertiesManager.getUpdateDemolitionWorkflow())) {
			persisterService.updateDemolition(demolitionRequest);
		}

	}
}
