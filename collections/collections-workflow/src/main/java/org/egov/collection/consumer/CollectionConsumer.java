package org.egov.collection.consumer;


import java.util.HashMap;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Service
public class CollectionConsumer {
	
	public static final Logger logger = LoggerFactory.getLogger(CollectionConsumer.class);
	
	@Autowired
	private ApplicationProperties applicationProperties;
		
	@Autowired
	private WorkflowService workflowService;
	
	
	@KafkaListener(topics = {"${kafka.topics.workflow.start.name}", "${kafka.topics.workflow.update.name}"})
	
	public void listen(final HashMap<String, Object> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
	logger.info("Record: "+record.toString());
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
	             if(topic.equals(applicationProperties.getKafkaStartWorkflowTopic())){
					logger.info("Consuming start workflow request");
					workflowService.startWorkflow(objectMapper.convertValue(record, WorkflowDetails.class));
				 }else if(topic.equals(applicationProperties.getKafkaUpdateworkflowTopic())){
					logger.info("Consuming update workflow request");
					workflowService.updateWorkflow(objectMapper.convertValue(record, WorkflowDetails.class));
				 }
			
		} catch (final Exception e) {
			logger.error("Error while listening to value: "+record+" on topic: "+topic+": ", e.getMessage());
		}
	}

}