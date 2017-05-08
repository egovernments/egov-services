package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.contract.TaskResponse;
import org.egov.lams.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowService {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private WorkflowRepository workflowRepository;

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowService.class);

	public void startWorkflow(AgreementRequest agreementRequest) {

		ProcessInstance processInstanceResponse = workflowRepository.startWorkflow(agreementRequest);
		LOGGER.info("the processInstanceresponse from workflow statrt : " + processInstanceResponse);
	}

	public void updateWorkflow(AgreementRequest agreementRequest) {
		TaskResponse taskResponse = workflowRepository.updateWorkflow(agreementRequest);
		LOGGER.info("the taskResponse from workflow update : " + taskResponse);
	}

}