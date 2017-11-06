package org.egov.lams.workflow.consumer;

import java.util.Map;

import org.egov.lams.common.web.request.AgreementRequest;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.workflow.config.PropertiesManager;
import org.egov.lams.workflow.service.WorkflowService;
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
public class LamsMahaWorkflowConsumer {

	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@KafkaListener(topics = {"${kafka.topic.update.workflow.agreement}","${kafka.topic.start.workflow.agreement}","${kafka.topic.start.workflow.estate}","${kafka.topic.update.workflow.estate}"})
	public void processMessage(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){

		log.info("::::inside workflow consumer:::::"+topic);

		if (propertiesManager.getStartEstateWorkflowTopic().equals(topic))
			workflowService.startWorkflow(objectMapper.convertValue(consumerRecord, EstateRegisterRequest.class));
		else if (propertiesManager.getUpdateEstateWorkflowTopic().equals(topic))
			workflowService.updateWorkflow(objectMapper.convertValue(consumerRecord, EstateRegisterRequest.class));
		else if (propertiesManager.getStartAgreementWorkflowTopic().equals(topic))
			workflowService.startAgreementWorkflow(objectMapper.convertValue(consumerRecord, AgreementRequest.class));
		else if (propertiesManager.getUpdateAgreementWorkflowTopic().equals(topic))
			workflowService.updateAgreementWorkflow(objectMapper.convertValue(consumerRecord, AgreementRequest.class));
	}
}
