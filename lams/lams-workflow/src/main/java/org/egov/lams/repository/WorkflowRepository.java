package org.egov.lams.repository;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.Position;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.contract.ProcessInstanceRequest;
import org.egov.lams.contract.ProcessInstanceResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.contract.RequestInfoWrapper;
import org.egov.lams.contract.Task;
import org.egov.lams.contract.TaskRequest;
import org.egov.lams.contract.TaskResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.producers.AgreementProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WorkflowRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowRepository.class);
	
	public static final String ACTION = "Forward";

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AgreementProducer agreementProducer;

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
		String agreementRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			agreementRequestMessage = objectMapper.writeValueAsString(agreementRequest);
		} catch (Exception e) {
			LOGGER.info("WorkflowRepositorysaveAgreement : " + e);
			throw new RuntimeException(e);
		}
		agreementProducer.sendMessage(propertiesManager.getKafkaSaveAgreementTopic(), "key", agreementRequestMessage);
	}

	public void updateAgreement(AgreementRequest agreementRequest) {

		String agreementRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			agreementRequestMessage = objectMapper.writeValueAsString(agreementRequest);
		} catch (Exception e) {
			LOGGER.info("WorkflowRepositoryupdateAgreement : " + e);
			throw new RuntimeException(e);
		}
		agreementProducer.sendMessage(propertiesManager.getKafkaUpdateAgreementTopic(), "key", agreementRequestMessage);
	}

	private ProcessInstanceRequest getProcessInstanceRequest(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Position assignee = new Position();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		assignee.setId(workFlowDetails.getAssignee());
		if(agreement.getRenewal()!=null){
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceRenewBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceRenewBusinessKey());
		}else if(agreement.getCancellation()!=null){
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCancelBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceCancelBusinessKey());
		}else{
			processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCreateBusinessKey());
			processInstance.setType(propertiesManager.getWorkflowServiceCreateBusinessKey());
		}
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
		if(agreement.getRenewal()!=null){
			task.setBusinessKey(propertiesManager.getWorkflowServiceRenewBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceRenewBusinessKey());
		}else if(agreement.getCancellation()!=null){
			task.setBusinessKey(propertiesManager.getWorkflowServiceCancelBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceCancelBusinessKey());
		}else{
			task.setBusinessKey(propertiesManager.getWorkflowServiceCreateBusinessKey());
			task.setType(propertiesManager.getWorkflowServiceCreateBusinessKey());
		}
		task.setId(agreement.getStateId());

		if (workflowDetails != null) {

			task.setAction(workflowDetails.getAction());
			task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
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

}
