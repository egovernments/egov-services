package org.egov.lams.workflow.repository;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.lams.common.web.contract.EstateRegister;
import org.egov.lams.common.web.contract.Position;
import org.egov.lams.common.web.contract.ProcessInstance;
import org.egov.lams.common.web.contract.Task;
import org.egov.lams.common.web.contract.TaskRequest;
import org.egov.lams.common.web.contract.TaskResponse;
import org.egov.lams.common.web.contract.WorkFlowDetails;
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
	
	public ProcessInstance startWorkflow(EstateRegisterRequest estateRegisterRequest) {
		
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
		
		saveEstateRegistration(estateRegisterRequest, processInstanceRes.getProcessInstances());
		return processInstanceRes.getProcessInstance();
	}
	
	private void saveEstateRegistration(EstateRegisterRequest estateRegisterRequest, List<ProcessInstance> processInstances){
		log.info("WorkflowRepository estateRegisterRequest"+estateRegisterRequest);
		try {
			kafkaTemplate.send(propertiesManager.getCreateEstateKafkaTopic(), estateRegisterRequest);
		} catch (Exception exception) {
			log.info("Workflow repo : " + exception.getMessage(), exception);
			throw exception;
		}
	}
	
	public TaskResponse updateWorkflow(EstateRegisterRequest estateRegisterRequest) {

		TaskResponse taskResponse = null;
		for (EstateRegister estateRegister : estateRegisterRequest.getLandRegisters()) {
			
			TaskRequest taskRequest = getTaskRequest(estateRegister,estateRegisterRequest.getRequestInfo());

			String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
					+ "/" + estateRegister.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

			log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
			try {
				taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
			} catch (Exception e) {
				log.error(e.toString());
				throw e;
			}
			log.info("the response from workflow update : " + taskResponse.getTask());
			if (estateRegister.getWorkFlowDetails() != null) {
				updateEstateRegister(estateRegisterRequest);
			}
		}
		return taskResponse;
	}
	
	public void updateEstateRegister(EstateRegisterRequest estateRegisterRequest){
		try {
			kafkaTemplate.send(propertiesManager.getUpdateEstateKafkaTopic(), estateRegisterRequest);
		} catch (Exception exception) {
			log.info("Workflow Service : " + exception.getMessage(), exception);
			throw exception;
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
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(estateRegister.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + estateRegister.getEstateNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);
		return processInstanceRequest;
	}
	
	private TaskRequest getTaskRequest(EstateRegister estateRegister, RequestInfo requestInfo) {
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		WorkFlowDetails workflowDetails = estateRegister.getWorkFlowDetails();
		Position assignee = new Position();
		// Boolean isCorporation = isCorporation(agreement.getTenantId());
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(propertiesManager.getWorkflowBusinessKey());
		task.setType(propertiesManager.getWorkflowBusinessKey());
		log.info("task businesskey :" + task.getBusinessKey());
		log.info("task type: " + task.getType());
		task.setId(estateRegister.getStateId());
		if (workflowDetails != null) {
			log.info("workflowDetails::"+workflowDetails.getAction());
			task.setAction(workflowDetails.getAction());
			if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getStatus())) {

				task.setStatus(propertiesManager.getWfStatusRejected());
				log.info("updating the task status for reject-forward case after payment : ", task.getStatus());
			} else
				task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
			task.setComments(workflowDetails.getComments());
		} else {

			log.info("The workflow object before collection update ::: " + estateRegister.getWorkFlowDetails());
			String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceSearchPath() + "?id=" + estateRegister.getStateId()
					+ "&tenantId=" + estateRegister.getTenantId();

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
