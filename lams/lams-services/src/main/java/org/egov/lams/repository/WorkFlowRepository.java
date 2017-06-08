package org.egov.lams.repository;

import java.util.List;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.Task;
import org.egov.lams.web.contract.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class WorkFlowRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowRepository.class);

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public Task getWorkFlowState(String stateId, String tenantId, RequestInfo requestInfo) {

		String url = propertiesManager.getCommonWorkFlowServiceHostName()
				    + propertiesManager.getCommonWorkFlowServiceSearchPath() 
				    + "?tenantId=" + tenantId + "&id=" + stateId;
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);
		TaskResponse taskResponse = null;

		try {
			taskResponse = restTemplate.postForObject(url, requestInfoWrapper, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.error("the exception from workflowhistory api call : " + e);
			throw e;
		}
		LOGGER.info("the response from workflowhistory api call : " + taskResponse);
		return taskResponse.getTasks().get(0);
	}

	public String getCommissionerName(String stateId, String tenantId, RequestInfo requestInfo) {

		String commisionerName = null;
		String url = propertiesManager.getCommonWorkFlowServiceHostName()
				+ propertiesManager.getCommonWorkFlowServiceHistoryPath() + "?tenantId=" + tenantId + "&workflowId="
				+ stateId;

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);
		LOGGER.info("the url for workflowhistory api call : " + url);
		TaskResponse taskResponse = null;

		try {
			taskResponse = restTemplate.postForObject(url, requestInfoWrapper, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.error("the exception from workflowhistory api call : " + e);
			throw e;
		}
		LOGGER.info("the response from workflow search : " + taskResponse);
		List<Task> workFlowHistory = taskResponse.getTasks();
		for (Task task : workFlowHistory) {
			if ("Commissioner Approved".equalsIgnoreCase(task.getStatus())) {
				commisionerName = task.getSenderName();
			}
		}
		return commisionerName;
	}
}
