package org.egov.lams.workflow.service;

import org.egov.lams.common.web.request.AgreementRequest;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
	
	@Autowired
	private WorkflowRepository workflowRepository;

	public void startWorkflow(EstateRegisterRequest estateRegisterRequest) {

	workflowRepository.startWorkflow(estateRegisterRequest);
	}

	public void updateWorkflow(EstateRegisterRequest estateRegisterRequest) {
	 workflowRepository.updateWorkflow(estateRegisterRequest);
	}
	
	public void startAgreementWorkflow(AgreementRequest agreementRequest) {

	workflowRepository.startAgreementWorkflow(agreementRequest);
	}

	public void updateAgreementWorkflow(AgreementRequest agreementRequest) {
	workflowRepository.updateAgreementWorkflow(agreementRequest);
	}
}
