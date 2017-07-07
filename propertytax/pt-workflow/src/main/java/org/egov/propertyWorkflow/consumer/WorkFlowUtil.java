package org.egov.propertyWorkflow.consumer;

import java.util.HashMap;
import java.util.Map;

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

	public ProcessInstance startWorkflow(ProcessInstanceRequest processInstanceRequest) {

		String url = environment.getProperty("workflowprocess.startUrl");
		ProcessInstanceResponse processInstanceResponse = null;

		try {

			processInstanceResponse = restTemplate.postForObject(url, processInstanceRequest,
					ProcessInstanceResponse.class);
		} catch (Exception ex) {

			System.out.println(ex.getMessage());
		}

		return processInstanceResponse.getProcessInstance();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TaskResponse updateWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo)
			throws JsonProcessingException {

		TaskRequest taskRequest = getTaskRequest(workflowDetailsRequestInfo);
		WorkFlowDetails workflowDetails = workflowDetailsRequestInfo.getWorkflowDetails();
		String url = environment.getProperty("workflow.updateUrl");

		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("id", workflowDetails.getAction());
		uriParams.put("id", "28");

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity requestEntity = new HttpEntity(taskRequest, headers);

		ResponseEntity<TaskResponse> responseEntity = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(),
				HttpMethod.POST, requestEntity, TaskResponse.class);

		return responseEntity.getBody();
	}

	@SuppressWarnings("unused")
	private ProcessInstanceRequest getProcessInstanceRequest(WorkflowDetailsRequestInfo workflowDetailsRequest) {

		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		Position assignee = new Position();
		assignee.setId((long) workflowDetails.getAssignee());
		processInstance.setBusinessKey(environment.getProperty("businessKey"));
		processInstance.setType(environment.getProperty("businessKey"));
		processInstance.setAssignee(assignee);
		processInstance.setComments(environment.getProperty("create.property.comments"));
		processInstanceRequest.setRequestInfo(requestInfo);

		return processInstanceRequest;
	}

	private TaskRequest getTaskRequest(WorkflowDetailsRequestInfo workflowDetailsRequest) {
		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey("Create Property");
		task.setAction(workflowDetails.getAction());
		task.setStatus(workflowDetails.getStatus());
		task.setTenantId("default");
		assignee.setId((long) workflowDetails.getAssignee());
		task.setAssignee(assignee);

		taskRequest.setTask(task);

		return taskRequest;
	}
}
