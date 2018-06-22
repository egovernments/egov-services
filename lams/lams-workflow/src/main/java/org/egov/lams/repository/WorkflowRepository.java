package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.Attribute;
import org.egov.lams.contract.Position;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.contract.ProcessInstanceRequest;
import org.egov.lams.contract.ProcessInstanceResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.contract.RequestInfoWrapper;
import org.egov.lams.contract.Task;
import org.egov.lams.contract.TaskRequest;
import org.egov.lams.contract.TaskResponse;
import org.egov.lams.contract.Value;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Status;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class WorkflowRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowRepository.class);
	
	public static final String ACTION = "Forward";
	
	public static final String COMMISSIONER = "Commissioner";
	
	public static final String ACTION_FORWARDED =" Forwarded";
	
	public static final String ACTION_APPROVAL_PENDING =" Approval Pending";
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public ProcessInstance startWorkflow(AgreementRequest agreementRequest) {

		ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(agreementRequest);

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();

		LOGGER.info("string url of the workflow appp : " + url + "WorkflowRepository processInstanceRequest : "
				+ processInstanceRequest);

		ProcessInstanceResponse processInstanceRes = null;
		try {
			processInstanceRes = restTemplate.postForObject(url, processInstanceRequest, ProcessInstanceResponse.class);
		} catch (Exception e) {
			LOGGER.info("the exception from workflow service call : " + e);
			throw e;
		}
		LOGGER.info("the response object from workflow : " + processInstanceRes.getProcessInstance().getId());

		saveAgreement(agreementRequest, processInstanceRes.getProcessInstance().getId());
		return processInstanceRes.getProcessInstance();
	}

	public TaskResponse updateWorkflow(AgreementRequest agreementRequest) {

		TaskRequest taskRequest = getTaskRequest(agreementRequest);
		Agreement agreement = agreementRequest.getAgreement();

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
				+ "/" + agreement.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

		LOGGER.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("the response from workflow update : " + taskResponse.getTask());
		if (agreement.getWorkflowDetails() != null) {
			updateAgreement(agreementRequest);
		}
		return taskResponse;
	}

	public void saveAgreement(AgreementRequest agreementRequest, String stateId) {
		agreementRequest.getAgreement().setStateId(stateId);
		try {
			kafkaTemplate.send(propertiesManager.getKafkaSaveAgreementTopic(), "key", agreementRequest);
		} catch (Exception exception) {
			LOGGER.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	public void updateAgreement(AgreementRequest agreementRequest) {
		try {
			kafkaTemplate.send(propertiesManager.getKafkaUpdateAgreementTopic(), "key", agreementRequest);
		} catch (Exception exception) {
			LOGGER.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	private ProcessInstanceRequest getProcessInstanceRequest(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Position assignee = new Position();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		assignee.setId(workFlowDetails.getAssignee());
		if (Action.JUDGEMENT.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
		} else if (Action.OBJECTION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
		} else if (Action.RENEWAL.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
		} else if (Action.CANCELLATION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
		} else if (Action.EVICTION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
		} else if (Action.REMISSION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
		} else {
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
		}
		LOGGER.info("process businesskey :" + processInstance.getBusinessKey());
		LOGGER.info("process type: " + processInstance.getType());
		processInstance.setAssignee(assignee);
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(agreement.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + agreement.getAcknowledgementNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);

		return processInstanceRequest;
	}

	private TaskRequest getTaskRequest(AgreementRequest agreementRequest) {

		LOGGER.info("isnide update workflow workflow details value ::: "
				+ agreementRequest.getAgreement().getWorkflowDetails());
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		WorkflowDetails workflowDetails = agreement.getWorkflowDetails();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		if (Action.JUDGEMENT.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
		} else if (Action.OBJECTION.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
		} else if (Action.RENEWAL.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
		} else if (Action.CANCELLATION.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
		} else if (Action.EVICTION.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
		} else if (Action.REMISSION.equals(agreement.getAction())) {
			task.setBusinessKey(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
		} else {
			task.setBusinessKey(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
		}
		LOGGER.info("task businesskey :" + task.getBusinessKey());
		LOGGER.info("task type: " + task.getType());
		task.setId(agreement.getStateId());
		task.setTenantId(agreement.getTenantId());
		if (workflowDetails != null) {

			task.setAction(workflowDetails.getAction());
			if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getStatus())
					&& agreement.getIsAdvancePaid() && agreement.getAction().equals(Action.CREATE)) {
				
				task.setStatus(propertiesManager.getWfStatusAssistantApproved());
				LOGGER.info("updating the task status for reject-forward case after payment : ", task.getStatus());
			} else {
				task.setStatus(workflowDetails.getStatus());
			}
			
			if (ACTION.equalsIgnoreCase(workflowDetails.getAction()) && workflowDetails.getDesignation() != null
					&& workflowDetails.getDesignation().endsWith(COMMISSIONER)) {
				Attribute stateAttribute = null;
				Attribute nextActionAttribute = null;
				Attribute designationAttribute = null;
				Map<String, Attribute> attributeMap = new HashMap<>();
				stateAttribute = getNextStateAttribute(workflowDetails.getDesignation());
				nextActionAttribute = getNextActionAttribute(workflowDetails.getNextDesignation());
				designationAttribute = getCurrentDesigantionAttribute(workflowDetails.getDesignation());
				attributeMap.put("nextState", stateAttribute);
				attributeMap.put("nextAction", nextActionAttribute);
				attributeMap.put("currentDesignation", designationAttribute);
				task.setAttributes(attributeMap);

			}
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
			task.setComments(workflowDetails.getComments());
		} else {

			LOGGER.info("The workflow object before collection update ::: " + agreement.getWorkflowDetails());
			String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceSearchPath() + "?id=" + agreement.getStateId() + "&tenantId="
					+ agreement.getTenantId();

			ProcessInstanceResponse processInstanceResponse = null;
			try {
				processInstanceResponse = restTemplate.postForObject(url, requestInfoWrapper,
						ProcessInstanceResponse.class);
			} catch (Exception e) {
				LOGGER.info("exception from search workflow call ::: " + e);
				throw e;
			}

			LOGGER.info("process instance responce ::: from search ::: " + processInstanceResponse);
			ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
			//List<Attribute> attributes = new ArrayList<>(processInstance.getAttributes().values());
			task.setAction(ACTION);
			task.setStatus(processInstance.getStatus());
			assignee = processInstance.getOwner();
			LOGGER.info("the owner object from response is ::: " + processInstance.getOwner());
			task.setAssignee(assignee);
		}
		taskRequest.setTask(task);

		return taskRequest;
	}

	private Attribute getNextStateAttribute(String currentDesignation) {
		List<Value> nextStateValue = new ArrayList<>();
		Attribute stateAttribute = new Attribute();
		Value nextState = new Value();
		nextState.setKey("nextState");
		nextState.setName(currentDesignation.concat(ACTION_FORWARDED));
		nextStateValue.add(nextState);
		stateAttribute.setCode("WFSTATE");
		stateAttribute.setValues(nextStateValue);
		return stateAttribute;
	}

	private Attribute getNextActionAttribute(String nextDesignation) {
		List<Value> nextActionValues = new ArrayList<>();
		Attribute actionAttribute = new Attribute();
		Value nextAction = new Value();
		nextAction.setKey("pendingAction");
		nextAction.setName(nextDesignation.concat(ACTION_APPROVAL_PENDING));
		nextActionValues.add(nextAction);
		actionAttribute.setCode("ACTION");
		actionAttribute.setValues(nextActionValues);
		return actionAttribute;
	}

	private Attribute getCurrentDesigantionAttribute(String currentDesignation) {
		List<Value> designationValues = new ArrayList<>();
		Attribute designationAttribute = new Attribute();
		Value designation = new Value();
		designation.setKey("designation");
		designation.setName(currentDesignation);
		designationValues.add(designation);
		designationAttribute.setCode("DESIG");
		designationAttribute.setValues(designationValues);
		return designationAttribute;
	}

}
