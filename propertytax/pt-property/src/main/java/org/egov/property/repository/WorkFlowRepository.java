package org.egov.property.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.models.Position;
import org.egov.models.RequestInfo;
import org.egov.models.Task;
import org.egov.models.TaskRequest;
import org.egov.models.TaskResponse;
import org.egov.models.WorkFlowDetails;
import org.egov.property.config.PropertiesManager;
import org.egov.property.exception.ValidationUrlNotFoundException;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This repository is for updating the workflow details while generating the
 * special notice
 * 
 * @author Walkingtree
 *
 */
@Repository
public class WorkFlowRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	public TaskResponse updateWorkFlowDetails(WorkFlowDetails workFlowDetails, RequestInfo requestInfo, String tenantId,
			String stateId) {

		TaskRequest taskRequest = getTaskRequest(workFlowDetails, requestInfo, tenantId);
		TaskResponse taskResponse = null;
		StringBuilder workFlowUpdateUrl = new StringBuilder();
		workFlowUpdateUrl.append(propertiesManager.getWorkflowHostname());
		workFlowUpdateUrl.append(propertiesManager.getWorkflowBasepath());
		workFlowUpdateUrl.append(propertiesManager.getWorkflowUpdatepath());
		String url = workFlowUpdateUrl.toString();

		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("id", stateId);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<TaskRequest> requestEntity = new HttpEntity<TaskRequest>(taskRequest, headers);

		try {
			ResponseEntity<TaskResponse> responseEntity = restTemplate.exchange(
					builder.buildAndExpand(uriParams).toUri(), HttpMethod.POST, requestEntity, TaskResponse.class);
			taskResponse = responseEntity.getBody();
		} catch (Exception exception) {
			throw new ValidationUrlNotFoundException(propertiesManager.getWorkflowValidation(), exception.getMessage(),
					requestInfo);
		}
		return taskResponse;
	}

	public TaskRequest getTaskRequest(WorkFlowDetails workflowDetails, RequestInfo requestInfo, String tenantId) {

		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(propertiesManager.getWorkflowBusinessKey());
		task.setAction(workflowDetails.getAction());
		task.setStatus(workflowDetails.getStatus());
		task.setTenantId(tenantId);
		assignee.setId((long) workflowDetails.getAssignee());
		task.setAssignee(assignee);
		taskRequest.setTask(task);

		return taskRequest;
	}
}
