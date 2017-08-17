package org.egov.workflow.web.controller;

import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.lang3.StringUtils;
import org.egov.workflow.domain.exception.NoDataFoundException;
import org.egov.workflow.domain.service.ActivitiWorkFlowImpl;
import org.egov.workflow.persistence.repository.UserRepository;
import org.egov.workflow.web.contract.ProcessDefinition;
import org.egov.workflow.web.contract.ProcessDefinitionRequest;
import org.egov.workflow.web.contract.ProcessDefinitionResponse;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.ResponseInfo;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.egov.workflow.web.contract.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


/**
 * 
 * @author Vishnukanth Rachamalla
 *
 */
@RestController
public class ActivitiWorkFlowController {

	public static final Logger LOGGER = LoggerFactory.getLogger(ActivitiWorkFlowController.class);

	@Autowired
	UserRepository userRepository;

	@Autowired 
	RepositoryService repositoryService;

	@Autowired 
	RuntimeService runtimeService;

	@Autowired
	FormService formService;

	@Autowired
	TaskService taskService;

	@Autowired
	HistoryService historyService;

	@Autowired 
	private ActivitiWorkFlowImpl activitiWorkFlowImpl;

	/**
	 * This api used to start process of workflow per process instance 
	 * 
	 * @param processInstanceRequest
	 * @return ProcessInstanceResponse
	 */
	
	@PostMapping(value = "/v2/process/_start")
	public ProcessInstanceResponse startWorkflow(@RequestBody final ProcessInstanceRequest processInstanceRequest) {
		ProcessInstanceResponse response = new ProcessInstanceResponse();		
		ProcessInstance processInstance = activitiWorkFlowImpl.start(processInstanceRequest);
		response.setProcessInstance(processInstance);
		response.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return response;
	}
	
	/**
	 * This api used to get process definition of workflow 
	 * 
	 * @param processDefinitionRequest
	 * @return processDefinitionResponse
	 */
	
	@PostMapping(value = "/v2/processDefinition/_search")
	public ProcessDefinitionResponse getProcessDefinition(@RequestBody final ProcessDefinitionRequest processDefinitionRequest) {
		ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse();
		ProcessDefinition  processDefinition = activitiWorkFlowImpl.getProcessDefinitionForms(processDefinitionRequest);
		processDefinitionResponse.setProcessDefinition(processDefinition);
		processDefinitionResponse.setResponseInfo(getResponseInfo(processDefinitionRequest.getRequestInfo()));
		return processDefinitionResponse;
	}
	
	/**
	 * This api used to get Process Instance details 
	 * @param processInstanceRequest
	 * @return getProcessResponse
	 */
	@PostMapping(value = "/v2/processInstance/_search")
	public ProcessInstanceResponse getProcessInstance(@RequestBody ProcessInstanceRequest processInstanceRequest) {
		ProcessInstanceResponse processInstanceResponse = activitiWorkFlowImpl.getHistoryDetail(processInstanceRequest);
		processInstanceResponse.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return processInstanceResponse;
	}

	/**
	 * This api used to get task details
	 * @param taskRequest
	 * @return taskResponse
	 */
	@PostMapping(value = "/v2/tasks/_search")
	public TaskResponse getTasks(@RequestBody final TaskRequest taskRequest) {
		TaskResponse taskResponse=new TaskResponse();
		List<Task> tasks = activitiWorkFlowImpl.getTasks(taskRequest);
		taskResponse.setTasks(tasks);
		taskResponse.setResponseInfo(getResponseInfo(taskRequest.getRequestInfo()));
		return taskResponse;
	}	

	/**
	 * This api used to update Task details
	 * @param taskRequest
	 * @param id
	 * @return ResponseInfo
	 */
	@PostMapping(value = "/v2/tasks/{id}/_update")
	public ResponseInfo updateTask(@RequestBody final TaskRequest taskRequest,@PathVariable String id) {
		LOGGER.info("Update Task request : " + taskRequest);
		//TaskResponse response=new TaskResponse();
		Task task = taskRequest.getTask();
		task.setId(id);
		taskRequest.setTask(task);
		ResponseInfo rInfo;
		try {
			task= activitiWorkFlowImpl.update(taskRequest);
			rInfo = getResponseInfo(taskRequest.getRequestInfo());
			rInfo.setStatus("Successfully done '" + task.getAction()+"' action.");
			
		}catch(ActivitiException e) {
			LOGGER.error(e.getMessage());
			throw new NoDataFoundException(e.getMessage());
		}
		//response.setTask(task);
		//response.setResponseInfo(getResponseInfo(taskRequest.getRequestInfo()));
		return rInfo;
	}
	
	/**
	 * This api used to get history details
	 * 
	 * @param historyRequest
	 * @return historyResponse
	 */
	@PostMapping(value = "/v2/history")
	public ProcessInstanceResponse getHistory(@RequestBody ProcessInstanceRequest historyRequest) {
		ProcessInstanceResponse historyResponse = activitiWorkFlowImpl.getHistoryDetail(historyRequest);
		historyResponse.setResponseInfo(getResponseInfo(historyRequest.getRequestInfo()));
		return historyResponse;
	}


	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {
		new ResponseInfo();
		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.ts(new Date().toLocaleString()).resMsgId(requestInfo.getMsgId()).resMsgId("placeholder")
				.status("placeholder").build();
	}
	
	/*@PostMapping(value = "/v2/processInstance/_search")
	public ProcessInstanceResponse getProcessInstance(@RequestBody final ProcessInstanceRequest processInstanceRequest) {
		ProcessInstanceResponse processInstanceResponse = new ProcessInstanceResponse();
		List<ProcessInstance> processInstances = activitiWorkFlowImpl.getProcessInstance(processInstanceRequest);
		processInstanceResponse.setProcessInstances(processInstances);
		processInstanceResponse.setResponseInfo(getResponseInfo(processInstanceRequest.getRequestInfo()));
		return processInstanceResponse;
	}*/
	
	
/*	@PostMapping(value = "/v2/deployments")
	public void uploadDeployment(@RequestParam("file") MultipartFile file,) {

	    if (request instanceof MultipartHttpServletRequest == false) {
	      throw new ActivitiIllegalArgumentException("Multipart request is required");
	    }

	    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

	    if (multipartRequest.getFileMap().size() == 0) {
	      throw new ActivitiIllegalArgumentException("Multipart request with file content is required");
	    }

	    MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

	    try {
	      DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
	      String fileName = file.getOriginalFilename();
	      if (StringUtils.isEmpty(fileName) || !(fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn") || fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip"))) {

	        fileName = file.getName();
	      }

	      if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
	        deploymentBuilder.addInputStream(fileName, file.getInputStream());
	      } else if (fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip")) {
	        deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
	      } else {
	        throw new ActivitiIllegalArgumentException("File must be of type .bpmn20.xml, .bpmn, .bar or .zip");
	      }
	      deploymentBuilder.name(fileName);

	      if (rInfo.getTenantId() != null) {
	        deploymentBuilder.tenantId(rInfo.getTenantId());
	      }
	      
		        deploymentBuilder.tenantId("456");

	      Deployment deployment = deploymentBuilder.deploy();
	      

	      String str = deployment.getTenantId();
	      System.out.println(str);
	      
	    //  ResponseInfo resInfo = getResponseInfo(rInfo);
	      //resInfo.setStatus("Successfully deployed.");
	     // return restResponseFactory.createDeploymentResponse(deployment);

	    } catch (Exception e) {
	      if (e instanceof ActivitiException) {
	        throw (ActivitiException) e;
	      }
	      throw new ActivitiException(e.getMessage(), e);
	    }
	  }
	
	
	
	
	
	@PostMapping(value = "/v2/deploy")
	public void addDeployments(String path) {
		Deployment deploymentId = repositoryService.createDeployment()
				.addClasspathResource("/home/vishnu/Desktop/Activitybackup/water_charges.bpmn")
				.tenantId("11123")
				.deploy();
		String str  = deploymentId.getTenantId();
		System.out.println(str);
		//     /home/vishnu/Desktop/Activitybackup/water_charges.bpmn
	}*/
}
