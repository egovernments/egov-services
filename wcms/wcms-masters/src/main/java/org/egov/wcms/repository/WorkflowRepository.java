package org.egov.wcms.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.config.ConfigurationManager;
import org.egov.wcms.model.Connection;
import org.egov.wcms.model.WorkflowDetails;
import org.egov.wcms.producers.WaterTransactionProducer;
import org.egov.wcms.web.contract.Position;
import org.egov.wcms.web.contract.ProcessInstance;
import org.egov.wcms.web.contract.ProcessInstanceRequest;
import org.egov.wcms.web.contract.ProcessInstanceResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.Task;
import org.egov.wcms.web.contract.TaskRequest;
import org.egov.wcms.web.contract.TaskResponse;
import org.egov.wcms.web.contract.WaterConnectionReq;
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
	private WaterTransactionProducer waterTransactionProducer;

	@Autowired
	private ConfigurationManager configurationManager;
	
	public ProcessInstance startWorkflow(WaterConnectionReq waterConnectionRequest) {

		ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(waterConnectionRequest);
		String url = configurationManager.getWorkflowServiceHostName()
				+ configurationManager.getWorkflowServiceStartPath();
		LOGGER.info("URL Obtained for Workflow Application is : " + url);
		LOGGER.info("Prepared processInstanceRequest is : " + processInstanceRequest);
		ProcessInstanceResponse processInstanceResponse = null;
		try {
			processInstanceResponse = restTemplate.postForObject(url, processInstanceRequest,
					ProcessInstanceResponse.class);
		} catch (Exception e) {
			LOGGER.info("Description of the Encountered Exception: " + e);
			throw e;
		}
		LOGGER.info("Reponse Object obtained from Workflow : " + processInstanceResponse.getProcessInstance().getId());
		
		return processInstanceResponse.getProcessInstance();
	}
	
	public TaskResponse updateWorkflow(WaterConnectionReq waterConnectionRequest) {

		TaskRequest taskRequest = getTaskRequest(waterConnectionRequest);
		Connection connection = waterConnectionRequest.getConnection();
		String url = configurationManager.getWorkflowServiceHostName() + configurationManager.getWorkflowServiceTaskPAth()
				+ "/" + connection.getConnectionStatus() + configurationManager.getWorkflowServiceUpdatePath();

		LOGGER.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("the response from workflow update : " + taskResponse.getTask());
		if (connection.getWorkflowDetails() != null) {
			updateAgreement(waterConnectionRequest);
		}
		return taskResponse;

	}
	
	private ProcessInstanceRequest getProcessInstanceRequest(WaterConnectionReq waterConnectionRequest) {
		
		Connection connection = new Connection();
		WorkflowDetails workFlowDetails =  connection.getWorkflowDetails();
		RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
		Position assignee = new Position();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();

		assignee.setId(workFlowDetails.getAssignee());
		processInstance.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
		processInstance.setType(configurationManager.getWorkflowServiceBusinessKey());
		processInstance.setAssignee(assignee);
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(connection.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + connection.getAcknowledgementNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);

		return processInstanceRequest;
	}
	
	public void saveConnectionRequest(WaterConnectionReq waterConnectionRequest, String stateId) {

		waterConnectionRequest.getConnection().setConnectionStatus(stateId);
		String wcRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			wcRequestMessage = objectMapper.writeValueAsString(waterConnectionRequest);
		} catch (Exception e) {
			LOGGER.info("Exception Encountered at Save Connection Request : " + e);
			throw new RuntimeException(e);
		}
		waterTransactionProducer.sendMessage(configurationManager.getKafkaSaveWaterConnectionTopic(), "key", wcRequestMessage);
	}
	
	public void updateAgreement(WaterConnectionReq waterConnectionRequest) {

		String wcRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			wcRequestMessage = objectMapper.writeValueAsString(waterConnectionRequest);
		} catch (Exception e) {
			LOGGER.info("WorkflowRepositoryupdateAgreement : " + e);
			throw new RuntimeException(e);
		}
		waterTransactionProducer.sendMessage(configurationManager.getKafkaUpdateWaterConnectionTopic(), "key", wcRequestMessage);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private TaskRequest getTaskRequest(WaterConnectionReq waterConnectionRequest) {

		LOGGER.info("isnide update workflow workflow details value ::: "
				+ waterConnectionRequest.getConnection().getWorkflowDetails());
		Connection connection = waterConnectionRequest.getConnection();
		RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		WorkflowDetails workflowDetails = null;
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();

		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
		task.setType(configurationManager.getWorkflowServiceBusinessKey());
		task.setId(connection.getConnectionStatus());

		if (null != connection.getWorkflowDetails()) {
			workflowDetails = connection.getWorkflowDetails();
			task.setAction(workflowDetails.getAction());
			task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
		} else {
			LOGGER.info("The workflow object before collection update ::: " + connection.getWorkflowDetails());
			String url = configurationManager.getWorkflowServiceHostName()
					+ configurationManager.getWorkflowServiceSearchPath() 
					+ "?id=" + connection.getConnectionStatus()
					+ "&tenantId=" + connection.getTenantId();

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
