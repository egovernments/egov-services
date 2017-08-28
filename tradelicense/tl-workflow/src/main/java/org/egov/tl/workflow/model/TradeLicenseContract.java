package org.egov.tl.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TradeLicenseContract {

	public static final String STATE_ID = "stateId";

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;

	private String tenantId;

	public ProcessInstanceRequest getProcessInstanceRequest() {

		ProcessInstanceRequest request = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		processInstance.setBusinessKey(workFlowDetails.getBusinessKey());
		processInstance.setType(workFlowDetails.getType());
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setTenantId(getTenantId());
		processInstance.setAssignee(new Position());
		processInstance.getAssignee().setId(workFlowDetails.getAssignee());
		processInstance.setSenderName(workFlowDetails.getSenderName());
		processInstance.setDetails(workFlowDetails.getDetails());
		processInstance.setStatus(workFlowDetails.getStatus());

		request.setProcessInstance(processInstance);

		return request;
	}

	public TaskRequest getTaskRequest() {

		TaskRequest request = new TaskRequest();
		Task task = new Task();

		task.setId(workFlowDetails.getStateId());
		task.setBusinessKey(workFlowDetails.getBusinessKey());
		task.setType(workFlowDetails.getType());
		task.setComments(workFlowDetails.getComments());
		task.setAction(workFlowDetails.getAction());
		task.setStatus(workFlowDetails.getStatus());
		task.setTenantId(getTenantId());
		task.setAssignee(new Position());
		task.getAssignee().setId(workFlowDetails.getAssignee());

		request.setTask(task);

		return request;
	}

	public boolean isWorkflowCreate() {
		return workFlowDetails.getAction() != null && workFlowDetails.getAction().equalsIgnoreCase("create");
	}

	public void update(ProcessInstanceResponse processInstanceResponse) {
		workFlowDetails.setAssignee(processInstanceResponse.getProcessInstance().getOwner().getId());
		workFlowDetails.setStateId(processInstanceResponse.getProcessInstance().getValueForKey(STATE_ID));
	}

	public void update(TaskResponse taskResponse) {
		workFlowDetails.setAssignee(taskResponse.getTask().getOwner().getId());
		workFlowDetails.setStateId(taskResponse.getTask().getValueForKey(STATE_ID));
	}
}