package org.egov.lams.workflow.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.Agreement;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.Position;
import org.egov.lams.common.web.contract.ProcessInstance;
import org.egov.lams.common.web.contract.Task;
import org.egov.lams.common.web.contract.TaskRequest;
import org.egov.lams.common.web.contract.TaskResponse;
import org.egov.lams.common.web.contract.WorkFlowDetails;
import org.egov.lams.common.web.request.AgreementRequest;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.common.web.request.ProcessInstanceRequest;
import org.egov.lams.common.web.request.ProcessInstanceResponse;
import org.egov.lams.common.web.request.RequestInfoWrapper;
import org.egov.lams.workflow.config.PropertiesManager;
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
	private PropertiesManager propertiesManager;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	public void startWorkflow(EstateRegisterRequest estateRegisterRequest) {
		
		ProcessInstanceResponse processInstanceRes = null;
		
		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();
		
		for (EstateRegister estateRegister : estateRegisterRequest.getLandRegisters()) {
			ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(estateRegister,
					estateRegisterRequest.getRequestInfo());
			try {
				processInstanceRes = restTemplate.postForObject(url, processInstanceRequest,
						ProcessInstanceResponse.class);
			} catch (Exception e) {
				throw e;
			}
			estateRegister.setStateId(processInstanceRes.getProcessInstance().getId());
		}
		
		kafkaTemplate.send(propertiesManager.getCreateEstateKafkaTopic(), estateRegisterRequest);
	}
	
	public void startAgreementWorkflow(AgreementRequest agreementRequest) {
		
		ProcessInstanceResponse processInstanceRes = null;
		
		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();
		
		for (Agreement agreement : agreementRequest.getAgreements()) {
			ProcessInstanceRequest processInstanceRequest = getAgreementProcessInstanceRequest(agreement.getWorkFlowDetails(),agreement.getTenantId(),agreement.getAgreementNumber(),
					agreementRequest.getRequestInfo());
			try {
				processInstanceRes = restTemplate.postForObject(url, processInstanceRequest,
						ProcessInstanceResponse.class);
			} catch (Exception e) {
				throw e;
			}
			agreement.setStateId(processInstanceRes.getProcessInstance().getId());
//			agreement.setWorkflowStatus(processInstanceRes.getProcessInstance().getStatus());
		}
		
		kafkaTemplate.send(propertiesManager.getCreateAgreementKafkaTopic(), agreementRequest);
	}
	
	public void updateWorkflow(EstateRegisterRequest estateRegisterRequest) {

		TaskResponse taskResponse = null;
		for (EstateRegister estateRegister : estateRegisterRequest.getLandRegisters()) {
			
			TaskRequest taskRequest = getTaskRequest(estateRegister.getWorkFlowDetails(),estateRegister.getTenantId(),estateRegister.getStateId(),estateRegisterRequest.getRequestInfo());

			String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
					+ "/" + estateRegister.getStateId() 
					+ propertiesManager.getWorkflowServiceUpdatePath();

			log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
			try {
				taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
			} catch (Exception e) {
				log.error(e.toString());
				throw e;
			}
			log.info("the response from workflow update : " + taskResponse.getTask());
			if (estateRegister.getWorkFlowDetails() != null) {
				kafkaTemplate.send(propertiesManager.getUpdateEstateKafkaTopic(), estateRegisterRequest);
			}
		}
	}
	
	public void updateAgreementWorkflow(AgreementRequest agreementRequest) {

		TaskResponse taskResponse = null;
		for (Agreement agreement : agreementRequest.getAgreements()) {

			TaskRequest taskRequest = getTaskRequest(agreement.getWorkFlowDetails(), agreement.getTenantId(),
					agreement.getStateId(), agreementRequest.getRequestInfo());

			String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
					+ "/" + agreement.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

			log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
			try {
				taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
			} catch (Exception e) {
				log.error(e.toString());
				throw e;
			}
//			agreement.setWorkflowStatus(taskResponse.getTask().getAction());
			log.info("the response from workflow update : " + taskResponse.getTask());
			if (agreement.getWorkFlowDetails() != null) {
				kafkaTemplate.send(propertiesManager.getUpdateAgreementKafkaTopic(), agreementRequest);
			}
		}
	}
	
	private ProcessInstanceRequest getProcessInstanceRequest(EstateRegister estateRegister, RequestInfo requestInfo) {

		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		WorkFlowDetails workFlowDetails = estateRegister.getWorkFlowDetails();
		Position assignee = new Position();
		assignee.setId(workFlowDetails.getAssignee());
		processInstance.setBusinessKey(propertiesManager.getWorkflowBusinessKey());
		processInstance.setType(propertiesManager.getWorkflowBusinessKey());
		log.info("process businesskey :" + processInstance.getBusinessKey());
		log.info("process type: " + processInstance.getType());
		processInstance.setAssignee(assignee);
//		processInstance.setComments(workFlowDetails.get);
//		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(estateRegister.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + estateRegister.getEstateNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);
		return processInstanceRequest;
	}
	
	private ProcessInstanceRequest getAgreementProcessInstanceRequest(WorkFlowDetails workFlowDetails,String tenantId,String number, RequestInfo requestInfo) {

		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		Position assignee = new Position();
		assignee.setId(workFlowDetails.getAssignee());
		processInstance.setBusinessKey(propertiesManager.getWorkflowBusinessKey());
		processInstance.setType(propertiesManager.getWorkflowBusinessKey());
		log.info("process businesskey :" + processInstance.getBusinessKey());
		log.info("process type: " + processInstance.getType());
		processInstance.setAssignee(assignee);
//		processInstance.setComments(workFlowDetails.getComments());
//		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(tenantId);
		processInstance.setDetails("Acknowledgement Number : " +number);
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);
		return processInstanceRequest;
	}
	
	private TaskRequest getTaskRequest(WorkFlowDetails workflowDetails,String tenantId,String stateId, RequestInfo requestInfo) {
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		Position assignee = new Position();
		// Boolean isCorporation = isCorporation(agreement.getTenantId());
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(propertiesManager.getWorkflowBusinessKey());
		task.setType(propertiesManager.getWorkflowBusinessKey());
		log.info("task businesskey :" + task.getBusinessKey());
		log.info("task type: " + task.getType());
		task.setTenantId(tenantId);
		task.setId(stateId);
		if (workflowDetails != null) {
			log.info("workflowDetails::"+workflowDetails.getAction());
			task.setAction(workflowDetails.getAction());
			if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getAction())) {

				task.setStatus(propertiesManager.getWfStatusRejected());
				log.info("updating the task status for reject-forward case after payment : ", task.getStatus());
			} else
				task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
//			task.setComments(workflowDetails.getComments());
		} else {

			log.info("The workflow object before collection update ::: " + workflowDetails);
			String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceSearchPath() + "?id=" + stateId
					+ "&tenantId=" + tenantId;

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
