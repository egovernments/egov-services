package org.egov.workflow.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.workflow.domain.service.CitizenServiceWorkflowImpl;
import org.egov.workflow.domain.service.PgrWorkflowImpl;
import org.egov.workflow.domain.service.WorkflowMatrixImpl;
import org.egov.workflow.web.contract.Attribute;
import org.egov.workflow.web.contract.Designation;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.egov.workflow.web.contract.TaskResponse;
import org.egov.workflow.web.contract.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorkFlowController {

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowController.class);

	@Autowired
	private PgrWorkflowImpl workflow;

	@Autowired
	private WorkflowMatrixImpl matrixWorkflow;

	@Autowired
	private CitizenServiceWorkflowImpl citizenServiceWorkflowImpl;

	@PostMapping(value = "/process/_start")
	public ProcessInstanceResponse startWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {

		User user = processInstanceRequest.getRequestInfo().getUserInfo();
		LOGGER.info("the request info ::: " + processInstanceRequest.getRequestInfo());

		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		processInstance.setSenderName(user.getName());

		ProcessInstanceResponse response = new ProcessInstanceResponse();

		if (processInstance.getBusinessKey().equals("Complaint")) {

			response = workflow.start(processInstanceRequest);

		} else if (processInstance.getType() != null && processInstance.getType().equals("Service Request")) {

			response = citizenServiceWorkflowImpl.start(processInstanceRequest);

		} else {
			response = matrixWorkflow.start(processInstanceRequest);

		}
		response.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return response;
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

	@PostMapping(value = "/history")
	public TaskResponse getHistory(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam final String tenantId, @RequestParam final String workflowId) {

		List<Task> historyDetail = workflow.getHistoryDetail(tenantId, workflowId);
		TaskResponse response = new TaskResponse();
		response.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		response.setTasks(historyDetail);
		return response;
	}

	@PostMapping(value = "/tasks/{id}/_update")
	public TaskResponse updateTask(@RequestBody final TaskRequest taskRequest, @PathVariable String id) {
		LOGGER.info("Update Task request:::::" + taskRequest);
		TaskResponse response = new TaskResponse();
		Task task = taskRequest.getTask();
		task.setId(id);
		if (task.getBusinessKey().equals("Complaint")) {

			response = workflow.update(taskRequest);

		} else if (task.getType() != null && task.getType().equals("Service Request")) {

			response = citizenServiceWorkflowImpl.update(taskRequest);

		} else {

			response = matrixWorkflow.update(taskRequest);

		}

		response.setResponseInfo(getResponseInfo(taskRequest.getRequestInfo()));

		return response;
	}

	@PostMapping(value = "/tasks/_search")
	public TaskResponse getTasks(@RequestBody final RequestInfoWrapper requestInfoWrapper, @ModelAttribute Task task) {
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());
		taskRequest.setTask(task);
		TaskResponse response = matrixWorkflow.getTasks(taskRequest);
		response.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		return response;
	}

	@PostMapping(value = "/process/_search")
	public ProcessInstanceResponse getProcess(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam String tenantId, @RequestParam String id) {

		ProcessInstance p = new ProcessInstance();
		ProcessInstanceResponse pres = new ProcessInstanceResponse();

		p = p.builder().id(id).build();

		p = matrixWorkflow.getProcess(tenantId, p, requestInfoWrapper.getRequestInfo());
		pres.setProcessInstance(p);
		pres.setResponseInfo(getResponseInfo(requestInfoWrapper.getRequestInfo()));
		LOGGER.info("The response  owner value before sending :::" + pres.getProcessInstance().getOwner());
		return pres;

	}

	@PostMapping(value = "/designations/_search")
	public List<Designation> getDesignationsByObjectType(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam final String departmentRule, @RequestParam final String currentStatus,
			@RequestParam final String businessKey, @RequestParam final String amountRule,
			@RequestParam final String additionalRule, @RequestParam final String pendingAction,
			@RequestParam final String approvalDepartmentName, @RequestParam String designation,
			@RequestParam(required=false) String tenantId) {

		Task t = new Task();
		t = t.builder().action(pendingAction).businessKey(businessKey).attributes(new HashMap<>()).status(currentStatus)
			.build();
		t.setTenantId(requestInfoWrapper.getRequestInfo().getTenantId());
		//as of now tenantId is made not mandatory .. But future it will be mandatory
		
		LOGGER.info("TenantId from query Param"+tenantId);
		LOGGER.info("TenantId from Task object"+t.getTenantId());
		
		if(t.getTenantId()==null ||t.getTenantId().isEmpty())
		{
		    //setting tenantId from query params
		    t.setTenantId(tenantId);
		}
		if(t.getTenantId()==null || t.getTenantId().isEmpty())
		{
		    LOGGER.error("tenantId is not found either in task or in query params."
		    	+ "You should never see this error . This needs to be fixed ASAP");
		}

		Attribute amountRuleAtt = new Attribute();
		amountRuleAtt.setCode(amountRule);
		t.getAttributes().put("amountRule", amountRuleAtt);

		Attribute additionalRuleAtt = new Attribute();
		amountRuleAtt.setCode(additionalRule);
		t.getAttributes().put("additionalRule", additionalRuleAtt);

		Attribute designationAtt = new Attribute();
		amountRuleAtt.setCode(designation);
		t.getAttributes().put("designation", designationAtt);

		
		t.setStatus(currentStatus);
		return matrixWorkflow.getDesignations(t, approvalDepartmentName);

	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new Date().toLocaleString()).resMsgId(requestInfo.getMsgId()).resMsgId("placeholder")
				.status("placeholder").build();
	}

}
