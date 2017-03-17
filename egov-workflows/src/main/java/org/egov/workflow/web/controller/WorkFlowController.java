package org.egov.workflow.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.workflow.domain.model.AuthenticatedUser;
import org.egov.workflow.domain.service.WorkflowMatrixImpl;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Designation;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkFlowController {

	@Autowired
	private WorkflowMatrixImpl workflow;
	
	@Autowired UserRepository userRepository;

	@Autowired
	private WorkflowMatrixImpl matrixWorkflow;

	@PostMapping(value = "/process/_start")
	public ProcessInstance startWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {
		String tenantId = "11";
		
		processInstanceRequest.getRequestInfo().getAuthToken();
		
		final AuthenticatedUser user = userRepository.getUser(processInstanceRequest.getRequestInfo().getAuthToken());
		System.out.println(user);
		
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();

		if (processInstance.getBusinessKey().equals("Complaint")) {

			// processInstance.getRequestInfo().getTenantId();
			return workflow.start(tenantId, processInstance);
		}

		else {
			return matrixWorkflow.start(tenantId, processInstance);
		}
	}
	
	

	@PostMapping(value = "/process/_end")
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
		
		Task task = taskRequest.getTask();
		if (task.getBusinessKey().equals("Complaint")) {

			// processInstance.getRequestInfo().getTenantId();
			return workflow.update(taskRequest.getRequestInfo().getTenantId(), task);
		}

		else {
			return matrixWorkflow.update(taskRequest.getRequestInfo().getTenantId(), task);
		}
	}
	
	
	@PostMapping(value = "/process/{id}/_search")
	public  ProcessInstanceResponse getProcess(@RequestBody RequestInfo requestInfo,
			@RequestParam String tenantId ,@PathVariable String id) {
		
		ProcessInstance p=new ProcessInstance();
		ProcessInstanceResponse pres=new ProcessInstanceResponse();
		
		p= p.builder().id(id).build();
		 
		p=  matrixWorkflow.getProcess(tenantId, p);
		pres.setProcessInstance(p);
		return pres;
		
	}
	

	
	

	@PostMapping(value = "/designations/_search")
	public  List<Designation> getDesignationsByObjectType(@RequestBody RequestInfo requestInfo,
			@RequestParam final String departmentRule , @RequestParam final String currentState,
			@RequestParam final String businessKey,
			@RequestParam final String amountRule, @RequestParam final String additionalRule,
			@RequestParam final String pendingAction, @RequestParam final String approvalDepartmentName,
			@RequestParam String designation) {

		Task t=new Task();
		t=t.builder()
		.action(pendingAction)
		.businessKey(businessKey)
		.attributes(new HashMap<>())
		.state(currentState).build();

		Attribute amountRuleAtt=new Attribute();
		amountRuleAtt .setCode(amountRule);
		t.getAttributes().put("amountRule",amountRuleAtt);
		
		Attribute additionalRuleAtt=new Attribute();
		amountRuleAtt .setCode(additionalRule);
		t.getAttributes().put("additionalRule",additionalRuleAtt);
		
		Attribute designationAtt=new Attribute();
		amountRuleAtt .setCode(designation);
		t.getAttributes().put("designation",designationAtt);

		return matrixWorkflow.getDesignations(t,approvalDepartmentName);

	}
	
	
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        new ResponseInfo();
        return ResponseInfo.builder()
                .apiId(requestInfo.getApiId())
                .ver(requestInfo.getVer())
                .ts(new Date().toLocaleString())
                .resMsgId(requestInfo.getMsgId())
                .resMsgId("placeholder")
                .status("placeholder")
                .build();
    }

}
