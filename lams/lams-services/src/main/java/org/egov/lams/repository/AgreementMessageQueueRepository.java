package org.egov.lams.repository;

import org.egov.lams.model.Agreement;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AgreementMessageQueueRepository {

	public static final Logger logger = LoggerFactory.getLogger(AgreementMessageQueueRepository.class);
	public static final String KEY = "save-agreement";
	public static final String START_WORKFLOW = "START_WORKFLOW";
	public static final String UPDATE_WORKFLOW = "UPDATE_WORKFLOW";
	public static final String SAVE = "SAVE";
	public static final String UPDATE = "UPDATE";

	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;

	private String startWorkflowTopic;

	private String updateWorkflowTopic;

	private String saveTopic;

	private String updateTopic;

	public AgreementMessageQueueRepository(LogAwareKafkaTemplate<String, Object> kafkaTemplate,
			@Value("${kafka.topics.start.workflow}") String startWorkflowTopic,
			@Value("${kafka.topics.update.workflow}") String updateWorkflowTopic,
			@Value("${kafka.topics.save.agreement}") String saveTopic,
			@Value("${kafka.topics.update.agreement}") String updateTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.startWorkflowTopic = startWorkflowTopic;
		this.updateWorkflowTopic = updateWorkflowTopic;
		this.saveTopic = saveTopic;
		this.updateTopic = updateTopic;
	}

	public void save(AgreementRequest agreementRequest, String action) {
		try {
			Agreement agreement = agreementRequest.getAgreement();
			logger.info("agreement before sending" + agreement);
			kafkaTemplate.send(getTopicName(action), KEY, agreementRequest);
		} catch (Exception exception) {
			logger.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	private String getTopicName(String action) {
		if (START_WORKFLOW.equals(action)) {
			return startWorkflowTopic;
		} else if (UPDATE_WORKFLOW.equals(action)) {
			return updateWorkflowTopic;
		} else if (SAVE.equals(action)) {
			return saveTopic;
		} else
			return updateTopic;
	}
}
