package org.egov.tl.workflow.service;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.WorkFlowDetails;
import org.egov.tl.workflow.model.Position;
import org.egov.tl.workflow.model.ProcessInstance;
import org.egov.tl.workflow.model.ProcessInstanceRequest;
import org.egov.tl.workflow.model.ProcessInstanceResponse;
import org.egov.tl.workflow.model.Task;
import org.egov.tl.workflow.model.TaskRequest;
import org.egov.tl.workflow.model.TaskResponse;
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

		if (isWorkflowCreate(tradeLicense.getWorkFlowDetails())) {

			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
			ProcessInstanceRequest request = getProcessInstanceRequest(tradeLicense.getWorkFlowDetails(),
					tradeLicense.getTenantId());

			request.setRequestInfo(requestInfo);

			processInstanceResponse = workflowRepository.start(request);

			if (processInstanceResponse != null)
				update(processInstanceResponse, tradeLicense);

		} else if (isWorkflowUpdate(tradeLicense.getWorkFlowDetails())) {

			TaskResponse taskResponse = new TaskResponse();
			TaskRequest taskRequest = getTaskRequest(tradeLicense.getWorkFlowDetails(), tradeLicense.getTenantId());
			taskRequest.setRequestInfo(requestInfo);

			taskResponse = workflowRepository.update(taskRequest);

			if (taskResponse != null)
				update(taskResponse, tradeLicense);

		}

	}

	private ProcessInstanceRequest getProcessInstanceRequest(WorkFlowDetails workFlowDetails, String tenantId) {

		ProcessInstanceRequest request = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		if (workFlowDetails != null) {
			processInstance.setBusinessKey(workFlowDetails.getBusinessKey());
			processInstance.setType(workFlowDetails.getType());
			processInstance.setComments(workFlowDetails.getComments());
			processInstance.setTenantId(tenantId);
			processInstance.setAssignee(new Position());
			processInstance.getAssignee().setId(workFlowDetails.getAssignee());
			processInstance.setSenderName(workFlowDetails.getSenderName());
			processInstance.setDetails(workFlowDetails.getDetails());
			processInstance.setStatus(workFlowDetails.getStatus());
		}
		request.setProcessInstance(processInstance);

		return request;
	}

	private TaskRequest getTaskRequest(WorkFlowDetails workFlowDetails, String tenantId) {

		TaskRequest request = new TaskRequest();
		Task task = new Task();

		if (workFlowDetails != null) {
			task.setId(workFlowDetails.getStateId());
			task.setBusinessKey(workFlowDetails.getBusinessKey());
			task.setType(workFlowDetails.getType());
			task.setComments(workFlowDetails.getComments());
			task.setAction(workFlowDetails.getAction());
			task.setStatus(workFlowDetails.getStatus());
			task.setTenantId(tenantId);
			task.setAssignee(new Position());
			task.getAssignee().setId(workFlowDetails.getAssignee());
		}
		request.setTask(task);

		return request;
	}

	private boolean isWorkflowCreate(WorkFlowDetails workFlowDetails) {
		return workFlowDetails != null && workFlowDetails.getAction() != null
				&& workFlowDetails.getAction().equalsIgnoreCase("create");
	}

	private boolean isWorkflowUpdate(WorkFlowDetails workFlowDetails) {
		return workFlowDetails != null && workFlowDetails.getAction() != null && !workFlowDetails.getAction().isEmpty();
	}

	private void update(ProcessInstanceResponse processInstanceResponse, TradeLicenseContract tradeLicense) {
		if (tradeLicense != null && tradeLicense.getApplication() != null) {
			tradeLicense.getApplication().setState_id(processInstanceResponse.getProcessInstance().getId());
		}
	}

	private void update(TaskResponse taskResponse, TradeLicenseContract tradeLicense) {

		if (tradeLicense != null && tradeLicense.getApplication() != null) {
			tradeLicense.getApplication().setState_id(taskResponse.getTask().getId());
		}
	}

}
