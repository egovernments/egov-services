package org.egov.lcms.workflow.consumer;

import org.egov.lcms.config.PropertiesManager;
import org.egov.lcms.workflow.models.Position;
import org.egov.lcms.workflow.models.ProcessInstance;
import org.egov.lcms.workflow.models.ProcessInstanceRequest;
import org.egov.lcms.workflow.models.RequestInfo;
import org.egov.lcms.workflow.models.Task;
import org.egov.lcms.workflow.models.TaskRequest;
import org.egov.lcms.workflow.models.TaskResponse;
import org.egov.lcms.workflow.models.WorkFlowDetails;
import org.egov.lcms.workflow.models.WorkflowDetailsRequestInfo;
import org.egov.lcms.workflow.repository.CommonWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LcmsWorkflowUtil {
	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	CommonWorkflowRepository commonWorkflowRepository;

	/**
	 * This service method will start workflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return ProcessInstance
	 * @throws Exception
	 */
	public ProcessInstance startWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String businessKey,
			String type, String comment) throws Exception {

		return commonWorkflowRepository.startWorkflowRepository(workflowDetailsRequestInfo, businessKey, type, comment);
	}

	public ProcessInstanceRequest getProcessInstanceRequest(String businessKey, String type, String comment) {

		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		Position assignee = new Position();
		processInstance.setState(propertiesManager.getState());
		processInstance.setBusinessKey(businessKey);
		processInstance.setType(type);
		processInstance.setAssignee(assignee);
		processInstance.setComments(comment);

		processInstanceRequest.setProcessInstance(processInstance);

		return processInstanceRequest;
	}

	/**
	 * This service method will update workflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskResponse
	 * @throws Exception
	 */
	public TaskResponse updateWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String stateId,
			String businessKey) throws Exception {

		return commonWorkflowRepository.updateWorkflowRepository(workflowDetailsRequestInfo, stateId, businessKey);
	}

	public TaskRequest getTaskRequest(WorkflowDetailsRequestInfo workflowDetailsRequest, String businessKey) {
		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		taskRequest.setRequestInfo(requestInfo);
		task.setBusinessKey(businessKey);
		task.setAction(workflowDetails.getAction());
		task.setStatus(workflowDetails.getStatus());
		task.setTenantId(workflowDetailsRequest.getTenantId());
		if (!workflowDetails.getAction().equalsIgnoreCase(propertiesManager.getSpecialNoticeAction())
				&& !workflowDetails.getAction().equalsIgnoreCase(propertiesManager.getCancel())) {
			assignee.setId((long) workflowDetails.getAssignee());
		} else {
			assignee.setId(null);
		}
		task.setAssignee(assignee);

		taskRequest.setTask(task);

		return taskRequest;
	}
}
