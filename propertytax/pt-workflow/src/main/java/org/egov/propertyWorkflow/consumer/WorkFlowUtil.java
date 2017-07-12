package org.egov.propertyWorkflow.consumer;

import java.util.HashMap;
import java.util.Map;

import org.egov.models.Property;
import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.models.Position;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.ProcessInstanceRequest;
import org.egov.propertyWorkflow.models.ProcessInstanceResponse;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.Task;
import org.egov.propertyWorkflow.models.TaskRequest;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class WorkFlowUtil {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	Environment environment;

	/**
	 * This service method will start workflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return ProcessInstance
	 */
	public ProcessInstance startWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String businessKey, String type, String comment) {

		StringBuilder workFlowStartUrl = new StringBuilder();
		workFlowStartUrl.append(environment.getProperty("egov.services.workflow_service.baseurl"));
		workFlowStartUrl.append(environment.getProperty("egov.services.workflow_service.basepath"));
		workFlowStartUrl.append(environment.getProperty("egov.services.workflow_service.startpath"));
		String url = workFlowStartUrl.toString();
		url = url.replace("{tenantId}", workflowDetailsRequestInfo.getTenantId());
		ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(workflowDetailsRequestInfo, businessKey, type, comment);
		ProcessInstanceResponse processInstanceResponse = null;

		try {

			processInstanceResponse = restTemplate.postForObject(url, processInstanceRequest,
					ProcessInstanceResponse.class);
		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}

		return processInstanceResponse.getProcessInstance();
	}

	/**
	 * This service method will update workflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskResponse
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TaskResponse updateWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String stateId)
			throws JsonProcessingException {

		TaskRequest taskRequest = getTaskRequest(workflowDetailsRequestInfo);
		StringBuilder workFlowUpdateUrl = new StringBuilder();
		workFlowUpdateUrl.append(environment.getProperty("egov.services.workflow_service.baseurl"));
		workFlowUpdateUrl.append(environment.getProperty("egov.services.workflow_service.basepath"));
		workFlowUpdateUrl.append(environment.getProperty("egov.services.workflow_service.updatepath"));
		String url = workFlowUpdateUrl.toString();

		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("id", stateId);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity requestEntity = new HttpEntity(taskRequest, headers);

		ResponseEntity<TaskResponse> responseEntity = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(),
				HttpMethod.POST, requestEntity, TaskResponse.class);

		TaskResponse taskResponse = responseEntity.getBody();
		return taskResponse;
	}

	/**
	 * This method will generate ProcessInstanceRequest from the
	 * WorkflowDetailsRequestInfo
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return ProcessInstanceRequest
	 */
	private ProcessInstanceRequest getProcessInstanceRequest(WorkflowDetailsRequestInfo workflowDetailsRequest, String businessKey, String type, String comment) {

		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		Position assignee = new Position();
		assignee.setId((long) workflowDetails.getAssignee());
		//TODO temporary fix for required fields of processInstance and need to replace with actual values
		processInstance.setState(environment.getProperty("workflowprocess.startStatus"));
		processInstance.setTenantId(workflowDetailsRequest.getTenantId());
		processInstance.setBusinessKey(businessKey);
		processInstance.setType(type);
		processInstance.setAssignee(assignee);
		processInstance.setComments(comment);
		processInstanceRequest.setRequestInfo(requestInfo);
		processInstanceRequest.setProcessInstance(processInstance);

		return processInstanceRequest;
	}

	/**
	 * This method will generate TaskRequest from the WorkflowDetailsRequestInfo
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskRequest
	 */
	private TaskRequest getTaskRequest(WorkflowDetailsRequestInfo workflowDetailsRequest) {
		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(environment.getProperty("businessKey"));
		task.setAction(workflowDetails.getAction());
		task.setStatus(workflowDetails.getStatus());
		task.setTenantId(workflowDetailsRequest.getTenantId());
		assignee.setId((long) workflowDetails.getAssignee());
		task.setAssignee(assignee);

		taskRequest.setTask(task);

		return taskRequest;
	}
}
