package org.egov.collection.domain.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.Task;
import org.egov.collection.model.TaskResponse;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.ProcessInstance;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

//@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {
	
	@Mock
	private WorkflowRepository workflowRepository;
	
	@InjectMocks
	private WorkflowService workflowService;
	
	@Mock
	@Autowired
	private ApplicationProperties applicationProperties;
	
	/*@Test
	public void test_should_start_workflow(){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setId("100");
		processInstanceResponse.setProcessInstance(processInstance);
		WorkflowDetails workflowDetails = getWorkflowDetails();
		Mockito.when(workflowRepository.startWorkflow(workflowDetails)).thenReturn(processInstanceResponse);
		processInstance = workflowService.startWorkflow(workflowDetails);
		assertNotNull(processInstance);
						
	}
	
	@Test(expected = Exception.class)
	public void test_should_start_workflow_exception(){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setId("A");
		processInstanceResponse.setProcessInstance(processInstance);
		WorkflowDetails workflowDetails = new WorkflowDetails();
		
		Mockito.when(workflowRepository.startWorkflow(workflowDetails)).thenReturn(processInstanceResponse);
		Mockito.when(workflowRepository.updateStateId(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		WorkflowService workflowService = new WorkflowService();
		assertNotNull(workflowService.startWorkflow(workflowDetails));
	}
	
	@Test
	public void test_should_update_workflow(){
		TaskResponse taskResponse = new TaskResponse();
		Task task = new Task();
		task.setId("100");
		taskResponse.setTask(task);
		WorkflowDetails workflowDetails = new WorkflowDetails();
		Mockito.when(workflowRepository.updateWorkflow(workflowDetails)).thenReturn(taskResponse);

		workflowService.updateWorkflow(workflowDetails);
				
		verify(workflowRepository).updateWorkflow(workflowDetails);
		
	}
	
	@Test(expected = Exception.class)
	public void test_should_update_workflow_exception(){
		TaskResponse taskResponse = new TaskResponse();
		
		WorkflowDetails workflowDetails = new WorkflowDetails();
		
		Mockito.when(workflowRepository.updateWorkflow(workflowDetails)).thenReturn(taskResponse);
		Mockito.when(workflowRepository.updateStateId(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		WorkflowService workflowService = new WorkflowService();
		assertNotNull(workflowService.updateWorkflow(workflowDetails));
	}
	
	private WorkflowDetails getWorkflowDetails(){
		WorkflowDetails workflowDetails = new WorkflowDetails();
		workflowDetails = WorkflowDetails.builder()
				.action("Approve").assignee(1L).businessKey(CollectionServiceConstants.BUSINESS_KEY)
				.comments("Comments").department(1L).designation(1L).initiatorPosition(1l).receiptNumber("")
				.state("Receipt Submitted").status("Receipt Submitted").tenantId("default").user(1L).build();
		
		return workflowDetails;
	}*/

}
