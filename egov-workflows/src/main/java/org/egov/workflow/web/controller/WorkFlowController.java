package org.egov.workflow.web.controller;

import java.util.List;

import org.egov.workflow.domain.service.WorkflowMatrixImpl;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkFlowController {

	@Autowired
	private WorkflowMatrixImpl workflow;

	@Autowired
	private WorkflowMatrixImpl matrixWorkflow;

	@PostMapping(value = "/_start")
	public ProcessInstance startWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {
		String tenantId = "11";
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();

		if (processInstance.getBusinessKey().equals("Complaint")) {

			// processInstance.getRequestInfo().getTenantId();
			return workflow.start(tenantId, processInstance);
		}

		else {
			return matrixWorkflow.start(tenantId, processInstance);
		}
	}

	@PostMapping(value = "/close")
	public ProcessInstance endWorkflow(@RequestBody final ProcessInstance processInstance) {
		String tenantId = "11";

		if (processInstance.getBusinessKey().equals("Complaint")) {

			// processInstance.getRequestInfo().getTenantId();
			return workflow.end(tenantId, processInstance);
		}

		else {
			return matrixWorkflow.end(tenantId, processInstance);
		}
	}

	@GetMapping(value = "/history")
	public List<Task> getHistory(@RequestParam final String tenantId, @RequestParam final String workflowId) {
		return workflow.getHistoryDetail(tenantId, workflowId);
	}

	@PostMapping(value = "/tasks/{id}/_update")
	public Task updateTask(@RequestBody final TaskRequest taskRequest) {
		String tenantId = "11";
		Task task = taskRequest.getTask();
		if (task.getBusinessKey().equals("Complaint")) {

			// processInstance.getRequestInfo().getTenantId();
			return workflow.update(tenantId, task);
		}

		else {
			return matrixWorkflow.update(tenantId, task);
		}
	}

}
