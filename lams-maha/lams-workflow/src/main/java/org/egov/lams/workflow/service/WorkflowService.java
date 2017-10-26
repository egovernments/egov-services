package org.egov.lams.workflow.service;

import org.egov.lams.common.web.contract.ProcessInstance;
import org.egov.lams.common.web.contract.TaskResponse;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
	
	@Autowired
	private WorkflowRepository workflowRepository;

	public void startWorkflow(EstateRegisterRequest estateRegisterRequest) {

		ProcessInstance processInstanceResponse = workflowRepository.startWorkflow(estateRegisterRequest);
	}

	public void updateWorkflow(EstateRegisterRequest estateRegisterRequest) {
		TaskResponse taskResponse = workflowRepository.updateWorkflow(estateRegisterRequest);
	}
}
