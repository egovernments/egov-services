package org.egov.lams.consumers;

import java.io.IOException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AgreementWorkflowConsumer {
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private PropertiesManager propertiesManager;

	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementWorkflowConsumer.class);
	
	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {"${kafka.topics.start.workflow}",
			"${kafka.topics.update.workflow}"})
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("topic : "+record.topic()+" key:" + record.key() + ":" + "value:" + record.value());

		if (record.topic().equalsIgnoreCase(propertiesManager.getKafkaStartWorkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			AgreementRequest agreementRequest = null;
			try {
				agreementRequest = objectMapper.readValue(record.value(), AgreementRequest.class);
				LOGGER.info("SaveAgreementConsumer agreement-worflow: "+agreementRequest);
				System.err.println(agreementRequest);
			} catch (IOException e) {
				LOGGER.info(e.getMessage(), e);
			}
			if (agreementRequest!= null) {
				workflowService.startWorkflow(agreementRequest);
			}
		}
		else if(record.topic().equals(propertiesManager.getKafkaUpdateworkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			AgreementRequest agreementRequest = null;
			try {
				LOGGER.info("SaveAgreementConsumer agreement-save-db AgreementDao:");
				agreementRequest = objectMapper.readValue(record.value(), AgreementRequest.class);
			} catch (IOException e) {
				LOGGER.info(e.getMessage(), e);
			}
			if (agreementRequest!= null) {
				workflowService.updateWorkflow(agreementRequest);
			}
		}

	}
}