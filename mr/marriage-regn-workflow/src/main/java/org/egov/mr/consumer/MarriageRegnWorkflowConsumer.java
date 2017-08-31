package org.egov.mr.consumer;

import java.util.Map;

import org.egov.mr.config.PropertiesManager;
import org.egov.mr.service.MRWorkflowService;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MarriageRegnWorkflowConsumer {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private MRWorkflowService workFlowService;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(topics = { "${kafka.topics.create.workflow}", "${kafka.topics.update.workflow}" })
	public void processMessage(@Payload Map<String, Object> consumerRecord,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {

		log.info("::::inside workflow consumer:::::");

		if (propertiesManager.getCreateWorkflowTopicName().equals(topic))
			workFlowService.startWorkflow(objectMapper.convertValue(consumerRecord, MarriageRegnRequest.class));
		else if (propertiesManager.getKafkaUpdateworkflowTopic().equals(topic))
			workFlowService.updateWorkflow(objectMapper.convertValue(consumerRecord, MarriageRegnRequest.class));
	}

}
