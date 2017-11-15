package org.egov.works.workflow.service;

import java.util.HashMap;
import java.util.Map;

import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.estimate.web.contract.WorkFlowDetails;
import org.egov.works.workflow.contracts.Attribute;
import org.egov.works.workflow.contracts.Position;
import org.egov.works.workflow.contracts.ProcessInstance;
import org.egov.works.workflow.contracts.ProcessInstanceRequest;
import org.egov.works.workflow.contracts.ProcessInstanceResponse;
import org.egov.works.workflow.contracts.Task;
import org.egov.works.workflow.contracts.TaskRequest;
import org.egov.works.workflow.contracts.TaskResponse;
import org.egov.works.workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

	private WorkflowRepository workflowRepository;

	@Autowired
	public WorkflowService(WorkflowRepository workflowRepository) {
		this.workflowRepository = workflowRepository;
	}

	public Map<String, String> enrichWorkflow(WorkFlowDetails workFlowDetails, String tenantId, RequestInfo requestInfo) {
		
		Map<String, String> workFlowResponse = new HashMap<>();

		if (isWorkflowCreate(workFlowDetails)) {

			ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
			ProcessInstanceRequest request = getProcessInstanceRequest(workFlowDetails,	tenantId);

			request.setRequestInfo(requestInfo);

			processInstanceResponse = workflowRepository.start(request);
			
			workFlowResponse.put("id", processInstanceResponse.getProcessInstance().getId());
			workFlowResponse.put("status", processInstanceResponse.getProcessInstance().getStatus());
			
			return workFlowResponse;

		} else if (isWorkflowUpdate(workFlowDetails)) {

			TaskResponse taskResponse = new TaskResponse();
			TaskRequest taskRequest = getTaskRequest(workFlowDetails, tenantId);
			taskRequest.setRequestInfo(requestInfo);

			taskResponse = workflowRepository.update(taskRequest);
			
			workFlowResponse.put("id", taskResponse.getTask().getId());
			workFlowResponse.put("status", taskResponse.getTask().getStatus());

			return workFlowResponse;
		}
		return null;
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
}
