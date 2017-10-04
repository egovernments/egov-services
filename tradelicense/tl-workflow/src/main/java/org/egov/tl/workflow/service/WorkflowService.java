package org.egov.tl.workflow.service;

import org.egov.tl.commons.web.contract.LicenseApplicationContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.WorkFlowDetails;
import org.egov.tl.workflow.model.Attribute;
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

	public void enrichWorkflow(LicenseApplicationContract application, RequestInfo requestInfo) {

		if (isWorkflowCreate(application.getWorkFlowDetails())) {

			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
			ProcessInstanceRequest request = getProcessInstanceRequest(application.getWorkFlowDetails(),
					application.getTenantId());

			request.setRequestInfo(requestInfo);

			processInstanceResponse = workflowRepository.start(request);

			if (processInstanceResponse != null)
				update(processInstanceResponse, application);

		} else if (isWorkflowUpdate(application.getWorkFlowDetails())) {

			TaskResponse taskResponse = new TaskResponse();
			TaskRequest taskRequest = getTaskRequest(application.getWorkFlowDetails(), application.getTenantId());
			taskRequest.setRequestInfo(requestInfo);

			taskResponse = workflowRepository.update(taskRequest);

			if (taskResponse != null)
				update(taskResponse, application);

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
			processInstance.setInitiatorPosition(workFlowDetails.getAssignee());
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
			if (!"reject".equalsIgnoreCase(workFlowDetails.getAction())) {
				task.getAssignee().setId(workFlowDetails.getAssignee());
				task.getAttributes().put("department",
						Attribute.asStringAttr("department", workFlowDetails.getDepartment()));
			}
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

	private void update(ProcessInstanceResponse processInstanceResponse, LicenseApplicationContract application) {
		if (application != null) {
			application.setState_id(processInstanceResponse.getProcessInstance().getId());
		}
	}

	private void update(TaskResponse taskResponse, LicenseApplicationContract application) {

		if (application != null) {
			application.setState_id(taskResponse.getTask().getId());
		}
	}

}
