package org.egov.collection.persistence.repository;


import static org.junit.Assert.*;


import org.egov.collection.config.ApplicationProperties;
import org.egov.collection.config.CollectionServiceConstants;
import org.egov.collection.model.Task;
import org.egov.collection.model.TaskRequest;
import org.egov.collection.model.TaskResponse;
import org.egov.collection.model.WorkflowDetails;
import org.egov.collection.repository.WorkflowRepository;
import org.egov.collection.web.contract.Position;
import org.egov.collection.web.contract.ProcessInstance;
import org.egov.collection.web.contract.ProcessInstanceRequest;
import org.egov.collection.web.contract.ProcessInstanceResponse;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.RestTemplate;

//@RunWith(MockitoJUnitRunner.class)
public class WorkflowRepositoryTest {
	
	@Mock
	JdbcTemplate jdbcTemplate;

	@Mock
	private ApplicationProperties applicationProperties;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@InjectMocks
	private WorkflowRepository workflowRepository;
	
	/*@Test(expected = Exception.class)
	public void test_should_be_able_to_start_workflow() {
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();
        RequestInfo requestInfo = new RequestInfo();
        final Position assignee = new Position();
        assignee.setId(1l);
        
        RestTemplate restTemplate = new RestTemplate();
        
        processInstance.setBusinessKey(CollectionServiceConstants.BUSINESS_KEY);
        processInstance.setType(CollectionServiceConstants.BUSINESS_KEY);
        processInstance.setComments("Comments");
        processInstance.setAssignee(assignee);
        processInstance.setInitiatorPosition(1L);
        processInstance.setTenantId("default");
        processInstance.setDetails("Receipt Create : ");
        
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);
        
		WorkflowDetails workflowDetails = new WorkflowDetails();
		StringBuilder uri = new StringBuilder();
		String basePath = applicationProperties.getWorkflowServiceHostName();
		String searchPath = applicationProperties.getWorkflowServiceStartPath();
		uri.append(basePath).append(searchPath);
		
		Mockito.when(applicationProperties.getWorkflowServiceHostName()).thenReturn(basePath);
		Mockito.when(applicationProperties.getWorkflowServiceStartPath()).thenReturn(searchPath);
		
	//	Mockito.when(workflowRepository.getProcessInstanceRequest(workflowDetails)).thenReturn(processInstanceRequest);
		Mockito.when(restTemplate.postForObject(uri.toString(), processInstanceRequest, ProcessInstanceResponse.class))
		.thenReturn(processInstanceResponse);
		
		assertTrue(processInstanceResponse == workflowRepository.startWorkflow(workflowDetails));
	}
	
	@Test
	public void test_should_be_unable_to_start_workflow() {
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		WorkflowDetails workflowDetails = new WorkflowDetails();
		
		Mockito.when(restTemplate.postForObject("uri", processInstanceRequest, ProcessInstanceResponse.class))
		.thenReturn(processInstanceResponse);
		
		assertTrue(null == workflowRepository.startWorkflow(workflowDetails));
	}
	
	@Test(expected = Exception.class)
	public void test_should_be_able_to_update_workflow() {
		TaskResponse taskResponse = new TaskResponse();
		TaskRequest taskRequest = new TaskRequest();
        RequestInfo requestInfo = new RequestInfo();
        final Task task = new Task();
        final Position assignee = new Position();
        assignee.setId(1L);
        
        RestTemplate restTemplate = new RestTemplate();
        
        task.setBusinessKey(CollectionServiceConstants.BUSINESS_KEY);
        task.setType(CollectionServiceConstants.BUSINESS_KEY);
        task.setComments("Comments");
        task.setAssignee(assignee);
        task.setTenantId("default");
        task.setDetails("Receipt Create : ");
        
        taskRequest.setTask(task);
        taskRequest.setRequestInfo(requestInfo);
        
		WorkflowDetails workflowDetails = new WorkflowDetails();
		StringBuilder uri = new StringBuilder();
		String basePath = applicationProperties.getWorkflowServiceHostName();
		String searchPath = applicationProperties.getWorkflowServiceStartPath();
		uri.append(basePath).append(searchPath);
		
		Mockito.when(workflowRepository.getTaskRequest(workflowDetails)).thenReturn(taskRequest);
		Mockito.when(restTemplate.postForObject(uri.toString(), taskRequest, TaskResponse.class))
		.thenReturn(taskResponse);
		
		assertTrue(taskResponse == workflowRepository.updateWorkflow(workflowDetails));
	}
	
	@Test
	public void test_should_be_unable_to_update_workflow() {
		TaskResponse taskResponse = new TaskResponse();
		TaskRequest taskRequest = new TaskRequest();
		WorkflowDetails workflowDetails = new WorkflowDetails();
		
		Mockito.when(restTemplate.postForObject("uri", taskRequest, TaskResponse.class))
		.thenReturn(taskResponse);
		
		assertTrue(null == workflowRepository.startWorkflow(workflowDetails));
	}
	
	@Test 
	public void test_should_return_proccessinstance_request(){
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();
        RequestInfo requestInfo = new RequestInfo();
        final Position assignee = new Position();
        assignee.setId(1l);
        
		WorkflowDetails workflowDetails = new WorkflowDetails();
		workflowDetails.setRequestInfo(requestInfo);

		WorkflowRepository workflowRepository = new WorkflowRepository();
                
        processInstance.setBusinessKey(CollectionServiceConstants.BUSINESS_KEY);
        processInstance.setType(CollectionServiceConstants.BUSINESS_KEY);
        processInstance.setComments("Comments");
        processInstance.setAssignee(assignee);
        processInstance.setInitiatorPosition(1L);
        processInstance.setTenantId("default");
        processInstance.setDetails("Receipt Create : ");
        
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);
        
        assertNotNull(workflowRepository.getProcessInstanceRequest(workflowDetails));
        
	}
	
	@Test 
	public void test_should_return_task_request(){
		TaskRequest taskRequest = new TaskRequest();
        RequestInfo requestInfo = new RequestInfo();
        final Task task = new Task();
        final Position assignee = new Position();
        assignee.setId(1L);
        
		WorkflowDetails workflowDetails = new WorkflowDetails();
		workflowDetails.setRequestInfo(requestInfo);
		
		WorkflowRepository workflowRepository = new WorkflowRepository();

        
        task.setBusinessKey(CollectionServiceConstants.BUSINESS_KEY);
        task.setType(CollectionServiceConstants.BUSINESS_KEY);
        task.setComments("Comments");
        task.setAssignee(assignee);
        task.setTenantId("default");
        task.setDetails("Receipt Create : ");
        
        taskRequest.setTask(task);
        taskRequest.setRequestInfo(requestInfo);
        
        assertNotNull(workflowRepository.getTaskRequest(workflowDetails));
        
	}
	
	@Test 
	public void test_should_not_update_stateid(){
		String receiptNumber = "GN-CL-15-000036";
		String proccessInstanceId ="200";
		WorkflowRepository workflowRepository = new WorkflowRepository();
        assertTrue(false == workflowRepository.updateStateId(receiptNumber, proccessInstanceId));
        
	}
	
	@Test(expected = Exception.class)
	public void test_should_update_stateid(){
		String receiptNumber = "GN-CL-15-000036";
		String proccessInstanceId ="200";
    	String query = "UPDATE egcl_receiptheader SET stateId=? WHERE receiptnumber=?";
    	
		WorkflowRepository workflowRepository = new WorkflowRepository();
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		Mockito.when(jdbcTemplate.update(query, new Object[] {Long.valueOf(proccessInstanceId), receiptNumber}))
		.thenReturn(1);

        assertTrue(false == workflowRepository.updateStateId(receiptNumber, proccessInstanceId));
        
	}
	
	@Test
	public void test_should_get_stateId(){
		String receiptNumber = "GN-CL-15-000036";
		WorkflowRepository workflowRepository = new WorkflowRepository();

        assertTrue(0L == workflowRepository.getStateId(receiptNumber));

	}*/
}
