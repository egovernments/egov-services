package org.egov.propertyWorkflow.repository;

import java.util.HashMap;
import java.util.Map;

import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.consumer.WorkFlowUtil;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.ProcessInstanceRequest;
import org.egov.propertyWorkflow.models.ProcessInstanceResponse;
import org.egov.propertyWorkflow.models.TaskRequest;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.tracer.http.LogAwareRestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 
 * @author Anil
 *
 */

@Repository
public class CommonWorkflowRepository {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	LogAwareRestTemplate restTemplate;

	@Autowired
	WorkFlowUtil workFlowUtil;

	private static final Logger logger = LoggerFactory.getLogger(WorkFlowUtil.class);

	/**
	 * This service method will update workflow using commonworkflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskResponse
	 */
	public ProcessInstance startWorkflowRepository(WorkflowDetailsRequestInfo workflowDetailsRequestInfo,
			String businessKey, String type, String comment) throws Exception {

		StringBuilder workFlowStartUrl = new StringBuilder();
		workFlowStartUrl.append(propertiesManager.getWorkflowHostName());
		workFlowStartUrl.append(propertiesManager.getWorkflowBasepath());
		workFlowStartUrl.append(propertiesManager.getWorkflowStartpath());
		String url = workFlowStartUrl.toString();
		url = url.replace("{tenantId}", workflowDetailsRequestInfo.getTenantId());
		ProcessInstanceRequest processInstanceRequest = workFlowUtil
				.getProcessInstanceRequest(workflowDetailsRequestInfo, businessKey, type, comment);
		ProcessInstanceResponse processInstanceResponse = null;

		logger.info("CommonWorkflowRepository URL ------->> " + url
				+ " \n CommonWorkflowRepository processInstanceRequest -->> " + processInstanceRequest);
		processInstanceResponse = restTemplate.postForObject(url, processInstanceRequest,
				ProcessInstanceResponse.class);

		logger.info("CommonWorkflowRepository processInstanceResponse :" + processInstanceResponse);
		return processInstanceResponse.getProcessInstance();
	}

	/**
	 * This service method will update workflow using common workflow api
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskResponse
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TaskResponse updateWorkflowRepository(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String stateId,
			String businessKey) throws Exception {

		TaskRequest taskRequest = workFlowUtil.getTaskRequest(workflowDetailsRequestInfo, businessKey);
		StringBuilder workFlowUpdateUrl = new StringBuilder();
		workFlowUpdateUrl.append(propertiesManager.getWorkflowHostName());
		workFlowUpdateUrl.append(propertiesManager.getWorkflowBasepath());
		workFlowUpdateUrl.append(propertiesManager.getWorkflowUpdatepath());
		String url = workFlowUpdateUrl.toString();

		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("id", stateId);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity requestEntity = new HttpEntity(taskRequest, headers);

		logger.info("CommonWorkflowRepository Builder uri ------->> " + builder.buildAndExpand(uriParams).toUri()
				+ " \n CommonWorkflowRepository processRequest -->> " + requestEntity);

		ResponseEntity<TaskResponse> responseEntity = restTemplate.exchange(builder.buildAndExpand(uriParams).toUri(),
				HttpMethod.POST, requestEntity, TaskResponse.class);

		logger.info("CommonWorkflowRepository TaskResponse" + responseEntity.getBody());
		TaskResponse taskResponse = responseEntity.getBody();
		return taskResponse;
	}

}
