package org.egov.lams.consumers;

import java.io.IOException;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.service.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AgreementWorkflowConsumer {
	
	@Autowired
	private WorkflowService workflowService;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@KafkaListener(topics = {"${kafka.topics.start.workflow}",
			"${kafka.topics.update.workflow}"})
	public void listen(Map<String, Object> consumerRecord, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.debug("key:" + topic + ":" + "value:" + consumerRecord);

		if (topic.equalsIgnoreCase(propertiesManager.getKafkaStartWorkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			AgreementRequest agreementRequest = null;
			try {
				agreementRequest = objectMapper.convertValue(consumerRecord, AgreementRequest.class);
				log.info("SaveAgreementConsumer agreement-worflow: "+agreementRequest);
				System.err.println(agreementRequest);
			} catch (Exception exception) {
				log.debug("processMessage:" + exception);
				throw exception;
			}
			if (agreementRequest!= null) {
				workflowService.startWorkflow(agreementRequest);
			}
		}
		else if(topic.equals(propertiesManager.getKafkaUpdateworkflowTopic())) {

			ObjectMapper objectMapper = new ObjectMapper();
			AgreementRequest agreementRequest = null;
			try {
				log.info("SaveAgreementConsumer agreement-save-db AgreementDao:");
				agreementRequest = objectMapper.convertValue(consumerRecord, AgreementRequest.class);
			}catch (Exception exception) {
				log.debug("processMessage:" + exception);
				throw exception;
			}
			if (agreementRequest!= null) {
				workflowService.updateWorkflow(agreementRequest);
			}
		}

	}
}