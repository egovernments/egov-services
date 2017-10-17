package org.egov.propertyWorkflow.consumer;

import org.egov.models.WorkFlowDetails;
import org.egov.propertyWorkflow.config.PropertiesManager;
import org.egov.propertyWorkflow.models.Position;
import org.egov.propertyWorkflow.models.ProcessInstance;
import org.egov.propertyWorkflow.models.ProcessInstanceRequest;
import org.egov.propertyWorkflow.models.RequestInfo;
import org.egov.propertyWorkflow.models.Task;
import org.egov.propertyWorkflow.models.TaskRequest;
import org.egov.propertyWorkflow.models.TaskResponse;
import org.egov.propertyWorkflow.models.WorkflowDetailsRequestInfo;
import org.egov.propertyWorkflow.repository.CommonWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkFlowUtil {

	@Autowired
	PropertiesManager propertiesManager;

	@Autowired
	TitleTransferConsumer titleTransferConsumer;
	
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

	/**
	 * This service method will update workflow
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return TaskResponse
	 * @throws Exception
	 */
	public TaskResponse updateWorkflow(WorkflowDetailsRequestInfo workflowDetailsRequestInfo, String stateId, String businessKey)
			throws Exception {

		return commonWorkflowRepository.updateWorkflowRepository(workflowDetailsRequestInfo, stateId, businessKey);
	}

	/**
	 * This method will generate ProcessInstanceRequest from the
	 * WorkflowDetailsRequestInfo
	 * 
	 * @param WorkflowDetailsRequestInfo
	 * @return ProcessInstanceRequest
	 */
	public ProcessInstanceRequest getProcessInstanceRequest(WorkflowDetailsRequestInfo workflowDetailsRequest,
			String businessKey, String type, String comment) {

		WorkFlowDetails workflowDetails = workflowDetailsRequest.getWorkflowDetails();
		RequestInfo requestInfo = workflowDetailsRequest.getRequestInfo();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		Position assignee = new Position();
		assignee.setId((long) workflowDetails.getAssignee());
		// TODO temporary fix for required fields of processInstance and need to
		// replace with actual values
		processInstance.setState(propertiesManager.getState());
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
	public TaskRequest getTaskRequest(WorkflowDetailsRequestInfo workflowDetailsRequest,String businessKey) {
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
		}
		else{
			assignee.setId(null);
		}
		task.setAssignee(assignee);

		taskRequest.setTask(task);

		return taskRequest;
	}
}
