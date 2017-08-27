package org.egov.tl.workflow.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tl.workflow.model.ProcessInstanceRequest;
import org.egov.tl.workflow.model.ProcessInstanceResponse;
import org.egov.tl.workflow.model.TaskResponse;
import org.egov.tl.workflow.model.TradeLicenseContract;
import org.egov.tl.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

	private WorkflowRepository workflowRepository;

	@Autowired
	public WorkflowService(WorkflowRepository workflowRepository) {
		this.workflowRepository = workflowRepository;
	}

	public void enrichWorkflow(TradeLicenseContract tradeLicense, RequestInfo requestInfo) {

		if (tradeLicense.isWorkflowCreate()) {

			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
			ProcessInstanceRequest request = tradeLicense.getProcessInstanceRequest();

			request.setRequestInfo(requestInfo);

			processInstanceResponse = workflowRepository.start(tradeLicense.getProcessInstanceRequest());

			if (processInstanceResponse != null)
				tradeLicense.update(processInstanceResponse);

		} else {

			TaskResponse taskResponse = new TaskResponse();

			taskResponse = workflowRepository.update(tradeLicense.getTaskRequest());

			if (taskResponse != null)
				tradeLicense.update(taskResponse);

		}

	}

}
