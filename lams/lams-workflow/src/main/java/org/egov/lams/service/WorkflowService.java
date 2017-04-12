package org.egov.lams.service;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.model.Agreement;
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
		
		
		Agreement agreement = agreementRequest.getAgreement();
		//FIXME ask mani, it should be ProcessInstanceResponse
		ProcessInstance processInstanceResponse = workflowRepository.startWorkflow(agreementRequest);
		LOGGER.info("the processInstance object response from workflow statrt repository call",processInstanceResponse);
		agreement.setStateId(processInstanceResponse.getId());
		saveAgreement(agreementRequest);
	}
	
	public void updateWorkflow(AgreementRequest agreementRequest) {

		workflowRepository.updateWorkflow(agreementRequest);
		if (agreementRequest.getAgreement().getWorkflowDetails() != null)
			updateAgreement(agreementRequest);
		//FIXME Can i call update agreement for collection update
	}

	private void saveAgreement(AgreementRequest agreementRequest) {
		workflowRepository.saveAgreement(agreementRequest);
	}
	
	private void updateAgreement(AgreementRequest agreementRequest) {
		workflowRepository.updateAgreement(agreementRequest);
	}
}
