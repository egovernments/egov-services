package org.egov.mr.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.config.PropertiesManager;
import org.egov.mr.contract.Position;
import org.egov.mr.contract.ProcessInstance;
import org.egov.mr.contract.ProcessInstanceRequest;
import org.egov.mr.contract.ProcessInstanceResponse;
import org.egov.mr.contract.RequestInfoWrapper;
import org.egov.mr.contract.Task;
import org.egov.mr.contract.TaskRequest;
import org.egov.mr.contract.TaskResponse;
import org.egov.mr.model.ApprovalDetails;
import org.egov.mr.model.MarriageRegn;
import org.egov.mr.model.enums.Action;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class WorkflowRepository {

	public static final String ACTION = "Forward";
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;
	
	public ProcessInstance startWorkflow(MarriageRegnRequest marriageRegnRequest) {

		ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(marriageRegnRequest);

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();

		log.info("string url of the workflow appp : " + url + "WorkflowRepository processInstanceRequest : "
				+ processInstanceRequest);

		ProcessInstanceResponse processInstanceRes = null;
		try {
			processInstanceRes = restTemplate.postForObject(url, processInstanceRequest, ProcessInstanceResponse.class);
		} catch (Exception e) {
			log.info("the exception from workflow service call : " + e);
			throw e;
		}
		System.err.println("::::processInstanceRes:::"+processInstanceRes);
		log.info("the response object from workflow : " + processInstanceRes.getProcessInstance().getId());

		saveMarriageRegn(marriageRegnRequest, processInstanceRes.getProcessInstance().getId());
		return processInstanceRes.getProcessInstance();
	}
	
	private ProcessInstanceRequest getProcessInstanceRequest(MarriageRegnRequest marriageRegnRequest) {

		MarriageRegn mariiageRegn = marriageRegnRequest.getMarriageRegn();
		ApprovalDetails workFlowDetails = mariiageRegn.getApprovalDetails();
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		Position assignee = new Position();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		assignee.setId(workFlowDetails.getAssignee());
		processInstance.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
		processInstance.setType(propertiesManager.getWorkflowServiceBusinessKey());
		processInstance.setAssignee(assignee);
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(mariiageRegn.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + mariiageRegn.getApplicationNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);

		return processInstanceRequest;
	}

	public void saveMarriageRegn(MarriageRegnRequest marriageRegnRequest, String stateId) {
		log.info("WorkflowRepository saveMarriageRegn marriageRegnRequest"+marriageRegnRequest);
		marriageRegnRequest.getMarriageRegn().setStateId(stateId);
		try {
			kafkaTemplate.send(propertiesManager.getCreateMarriageRegnTopicName(), marriageRegnRequest);
		} catch (Exception exception) {
			log.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}
	
	public TaskResponse updateWorkflow(MarriageRegnRequest marriageRegnRequest) {

		TaskRequest taskRequest = getTaskRequest(marriageRegnRequest);
		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
				+ "/" + marriageRegn.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

		log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
		log.info("the response from workflow update : " + taskResponse.getTask());
		if (marriageRegn.getApprovalDetails() != null) {
			updateMarriagRegn(marriageRegnRequest);
		}
		return taskResponse;
	}
	
	public void updateMarriagRegn(MarriageRegnRequest marriageRegnRequest) {
		try {
			kafkaTemplate.send(propertiesManager.getUpdateMarriageRegnTopicName(), marriageRegnRequest);
		} catch (Exception exception) {
			log.info("MarriageRegnService : " + exception.getMessage(), exception);
			throw exception;
		}
	}
	
	private TaskRequest getTaskRequest(MarriageRegnRequest marriageRegnRequest) {

		MarriageRegn marriageRegn = marriageRegnRequest.getMarriageRegn();
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		ApprovalDetails workflowDetails = marriageRegn.getApprovalDetails();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
		task.setType(propertiesManager.getWorkflowServiceBusinessKey());
		task.setId(marriageRegn.getStateId());

		if (workflowDetails != null) {
			task.setAction(workflowDetails.getAction());
			if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getStatus())
					&& marriageRegn.getActions().equals(Action.CREATE)) {
				task.setStatus(propertiesManager.getWfStatusAssistantApproved());
				log.info("updating the task status for reject-forward case after payment : ", task.getStatus());
			} else
				task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
			task.setComments(workflowDetails.getComments());
		} else {

			log.info("The workflow object before collection update ::: " + marriageRegn.getApprovalDetails());
			String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceSearchPath() + "?id=" + marriageRegn.getStateId()
					+ "&tenantId=" + marriageRegn.getTenantId();
			ProcessInstanceResponse processInstanceResponse = null;
			try {
				processInstanceResponse = restTemplate.postForObject(url, requestInfoWrapper,
						ProcessInstanceResponse.class);
			} catch (Exception e) {
				log.info("exception from search workflow call ::: " + e);
				throw e;
			}
			log.info("process instance responce ::: from search ::: " + processInstanceResponse);
			ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
			// List<Attribute> attributes = new
			// ArrayList<>(processInstance.getAttributes().values());
			task.setAction(ACTION);
			task.setStatus(processInstance.getStatus());
			assignee = processInstance.getOwner();
			log.info("the owner object from response is ::: " + processInstance.getOwner());
			task.setAssignee(assignee);
		}
		taskRequest.setTask(task);

		return taskRequest;
	}

}
