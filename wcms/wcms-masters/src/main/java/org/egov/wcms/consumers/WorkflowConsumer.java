package org.egov.wcms.consumers;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.wcms.config.ConfigurationManager;
import org.egov.wcms.service.WorkflowService;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WorkflowConsumer {
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private ConfigurationManager configurationManager; 

	
	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowConsumer.class);
	
	@KafkaListener(containerFactory = "kafkaListenerContainerFactory", topics = {"${kafka.topics.start.workflow}",
			"${kafka.topics.update.workflow}"})
	public void listen(ConsumerRecord<String, String> record) {
		LOGGER.info("topic : "+record.topic()+" key:" + record.key() + ":" + "value:" + record.value());

		if (record.topic().equalsIgnoreCase(configurationManager.getKafkaStartWorkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			WaterConnectionReq waterConnectionRequest = null;
			try {
				waterConnectionRequest = objectMapper.readValue(record.value(), WaterConnectionReq.class);
				LOGGER.info("Water Connection Request WorkFlow : "+waterConnectionRequest);
				System.err.println(waterConnectionRequest);
			} catch (IOException e) {
				LOGGER.info(e.getMessage(), e);
			}
			if (waterConnectionRequest!= null) {
				workflowService.startWorkflow(waterConnectionRequest);
			}
		}
		else if (record.topic().equals(configurationManager.getKafkaUpdateworkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			WaterConnectionReq waterConnectionRequest = null;
			try {
				waterConnectionRequest = objectMapper.readValue(record.value(), WaterConnectionReq.class);
				LOGGER.info("Water Connection Request Workflow : " + waterConnectionRequest);
			} catch (IOException e) {
				LOGGER.info(e.getMessage(), e);
			}
			if (waterConnectionRequest != null) {
				workflowService.updateWorkflow(waterConnectionRequest);
			}
		}

	}
	
}
