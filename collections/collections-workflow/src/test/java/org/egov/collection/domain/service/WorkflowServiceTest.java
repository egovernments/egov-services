package org.egov.collection.domain.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import org.egov.collection.model.TaskResponse;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.service.WorkflowService;
import org.egov.collection.web.contract.ProcessInstance;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkflowServiceTest {
	
	@Mock
	private WorkflowRepository workflowRepository;
	
/*	@Test
	public void test_should_start_workflow(){
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setId("A");
		processInstanceResponse.setProcessInstance(processInstance);
		WorkflowDetails workflowDetails = new WorkflowDetails();
		WorkflowService workflowService = new WorkflowService();
		Mockito.when(workflowRepository.startWorkflow(workflowDetails)).thenReturn(processInstanceResponse);

		workflowService.startWorkflow(workflowDetails);
				
		verify(workflowRepository).startWorkflow(workflowDetails);
		
	} */
	
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
	
/*	@Test
	public void test_should_update_workflow(){
		TaskResponse taskResponse = new TaskResponse();
		WorkflowDetails workflowDetails = new WorkflowDetails();
		WorkflowService workflowService = new WorkflowService();
		Mockito.when(workflowRepository.updateWorkflow(workflowDetails)).thenReturn(taskResponse);

		workflowService.updateWorkflow(workflowDetails);
				
		verify(workflowRepository).updateWorkflow(workflowDetails);
		
	} */
	
	@Test(expected = Exception.class)
	public void test_should_update_workflow_exception(){
		TaskResponse taskResponse = new TaskResponse();
		
		WorkflowDetails workflowDetails = new WorkflowDetails();
		
		Mockito.when(workflowRepository.updateWorkflow(workflowDetails)).thenReturn(taskResponse);
		Mockito.when(workflowRepository.updateStateId(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		WorkflowService workflowService = new WorkflowService();
		assertNotNull(workflowService.updateWorkflow(workflowDetails));
	}

}
