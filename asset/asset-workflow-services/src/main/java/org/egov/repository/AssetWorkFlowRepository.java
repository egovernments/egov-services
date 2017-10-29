package org.egov.repository;

import org.egov.config.ApplicationProperties;
import org.egov.contract.ProcessInstance;
import org.egov.contract.ProcessInstanceRequest;
import org.egov.contract.ProcessInstanceResponse;
import org.egov.contract.TaskRequest;
import org.egov.contract.TaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class AssetWorkFlowRepository {
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@Autowired
	private RestTemplate restTemplate;

	
	public String startWorkflow(ProcessInstanceRequest processInstanceRequest) {

		String url = applicationProperties.getWorkflowServiceHostName() + applicationProperties.getWorkflowServiceStartPath();

		log.info("string url of the workflow appp : " + url + "WorkflowRepository processInstanceRequest : "
				+ processInstanceRequest);

		ProcessInstanceResponse processInstanceRes = null;
		try {
			processInstanceRes = restTemplate.postForObject(url, processInstanceRequest, ProcessInstanceResponse.class);
		} catch (Exception e) {
			log.info("the exception from workflow service call : " + e);
			throw e;
		}
		log.info("the response object from workflow : " + processInstanceRes.getProcessInstance().getId());

		return processInstanceRes.getProcessInstance().getId();
	}

	public String updateWorkflow(TaskRequest taskRequest) {

		
		String url = applicationProperties.getWorkflowServiceHostName() + applicationProperties.getWorkflowServiceTaskPAth()
				+ "/" + taskRequest.getTask().getId() + applicationProperties.getWorkflowServiceUpdatePath();

		log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
		log.info("the response from workflow update : " + taskResponse.getTask());
		
		return taskResponse.getTask().getId();
	}
}
