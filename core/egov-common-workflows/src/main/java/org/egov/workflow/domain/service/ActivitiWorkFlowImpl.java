package org.egov.workflow.domain.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.activiti.engine.ActivitiException;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricFormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.TaskQuery;
import org.activiti.form.api.FormRepositoryService;
import org.egov.workflow.domain.exception.InvalidDataException;
import org.egov.workflow.domain.exception.NoDataFoundException;
import org.egov.workflow.domain.model.WorkflowConstants;
import org.egov.workflow.web.contract.ActivitiEnumFormProperty;
import org.egov.workflow.web.contract.ActivitiFormProperty;
import org.egov.workflow.web.contract.ProcessDefinition;
import org.egov.workflow.web.contract.ProcessDefinitionRequest;
import org.egov.workflow.web.contract.Position;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivitiWorkFlowImpl {
	public static final Logger LOGGER = LoggerFactory.getLogger(ActivitiWorkFlowImpl.class);
	RepositoryService repositoryService;
	RuntimeService runtimeService;
	FormService formService;
	TaskService taskService;
	HistoryService historyService;
	FormRepositoryService formRepositoryService;

	@Autowired
	public ActivitiWorkFlowImpl(final FormService formService, final RepositoryService repositoryService, final RuntimeService runtimeService, final TaskService taskService, HistoryService historyService){
		this.taskService = taskService;
		this.formService = formService;
		this.repositoryService = repositoryService;
		this.runtimeService = runtimeService;
		this.historyService = historyService;
	}

	public ProcessInstance start(final ProcessInstanceRequest processInstanceRequest){
		LOGGER.info("start workflow process at ActivitiWorkFlowImpl Class");
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();

		org.activiti.engine.repository.ProcessDefinition processDefinition = null;
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		if(processInstance.getProcessDefinationId() == null) {
			processDefinition =query.processDefinitionKey(processInstance.getBusinessKey()).latestVersion().singleResult();
		}
		if(processDefinition != null) processInstance.setProcessDefinationId(processDefinition.getId());
		if (processInstance.getProcessDefinationId() == null){
			LOGGER.error("ProcessDefinitionId : " + processInstance.getProcessDefinationId() + "." );
			throw new NoDataFoundException("Required Valid 'businessKey' or 'ProcessDefinitionId'.");
		} 
		if(processInstance.getAssignee() == null || processInstance.getAssignee().getId() == null) {
			LOGGER.error("Assignee : " + processInstance.getAssignee() + "." );
			throw new NoDataFoundException("Required 'assignee'.");
		}

		try {			
			//starting process using FormService
			org.activiti.engine.runtime.ProcessInstance activitiProcessInstance;
			if(processInstance.getFormProperties() != null)
				activitiProcessInstance = formService.submitStartFormData(processInstance.getProcessDefinationId(), buildMap(processInstance.getFormProperties()));
			else
				activitiProcessInstance = formService.submitStartFormData(processInstance.getProcessDefinationId(), new HashMap<String, String>());


			List<org.activiti.engine.task.Task> actiTasks = taskService.createTaskQuery().processInstanceId(activitiProcessInstance.getId()).list();

			for(org.activiti.engine.task.Task actiTask : actiTasks) {
				//actiTask.setAssignee(processInstance.getAssignee().getId().toString());
				//taskService.saveTask(actiTask);
				taskService.claim(actiTask.getId(),processInstance.getAssignee().getId().toString());
			}

			//Mapping value ActivitiEngineProcessInstance to processInstance
			processInstance.setId(activitiProcessInstance.getId());
			processInstance.setCreatedDate(activitiProcessInstance.getStartTime());
			processInstance.setName(activitiProcessInstance.getName());
			processInstance.setProcessDefinationId(activitiProcessInstance.getProcessDefinitionId());
			processInstance.setComments(activitiProcessInstance.getDescription());

		}catch(ActivitiException e) {
			LOGGER.error(e.getMessage());
			throw new NoDataFoundException(e.getMessage());
		}
		return processInstance;
	}

	public List<Task> getTasks(TaskRequest taskRequest) {
		Task task = taskRequest.getTask();
		Position assignee = task.getAssignee();
		try {

			final List<Task> tasks = new ArrayList<Task>();
			TaskQuery taskQuery = taskService.createTaskQuery();

			if (task != null && task.getId() !=null) {
				taskQuery.taskId(task.getId());
			}
			if (task.getBusinessKey() != null) {
				taskQuery.processDefinitionKey(task.getBusinessKey());
			}
			if (task.getProcessInstanceId() != null) {
				taskQuery.processInstanceId(task.getProcessInstanceId());
			}
			if (assignee !=null && assignee.getId() != null) {
				taskQuery.taskAssignee(assignee.getId().toString());
			}
			if (task.getCandidateGroup() != null) {
				taskQuery.taskCandidateGroup(task.getCandidateGroup());
			}
			if (task.getDueDate() != null) {
				Date dueDate = null;
				try {
					if(task.getDueDate() != null)
						dueDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(task.getDueDate());
					taskQuery.taskDueDate(dueDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			if (task.getCreatedDate()!= null) {
				taskQuery.taskCreatedOn(task.getCreatedDate());
			}
			if (task.getName() != null) {
				taskQuery.taskName(task.getName());
			}
			if (task.getComments() != null) {
				taskQuery.taskDescriptionLike(task.getComments());
			}

			//add pagination

			List<org.activiti.engine.task.Task> activitiTasks = taskQuery.list();
			for(org.activiti.engine.task.Task activitiTask : activitiTasks){
				String activitiTaskId = activitiTask.getId();
				Task tsk = new Task();
				tsk.setId(activitiTaskId);
				tsk.setName(activitiTask.getName());
				tsk.setCreatedDate(activitiTask.getCreateTime());

				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				String strDueDate = null;
				if(activitiTask.getDueDate() != null)
					strDueDate = df.format(activitiTask.getDueDate());			
				tsk.setDueDate(strDueDate);

				tsk.setComments(activitiTask.getDescription());			
				if(activitiTask.getAssignee() != null) {
					Position p = new Position();
					p.setId(Long.parseLong(activitiTask.getAssignee()));
					tsk.setAssignee(p);
				}
				tsk.setProcessInstanceId(activitiTask.getProcessInstanceId());
				tsk.setProcessDefinitionId(activitiTask.getProcessDefinitionId());
				tsk.setClaimTime(activitiTask.getClaimTime());
				tsk.setPriority(activitiTask.getPriority());
				tsk.setStatus(activitiTask.getName() + " is pending");
				tsk.setTaskDefinitionKey(activitiTask.getTaskDefinitionKey());
				System.out.println("vishnukanth..........................................." + activitiTask.getTaskDefinitionKey());

				if(activitiTask.getOwner() != null) {
					Position p = new Position();
					p.setId(Long.parseLong(activitiTask.getOwner()));
					tsk.setOwner(p);
				}

				TaskFormData taskFormData = formService.getTaskFormData(activitiTaskId);
				List<ActivitiFormProperty> activitiFormPropertys = formPropertyListConverter(taskFormData.getFormProperties());
				tsk.setFormProperties(activitiFormPropertys);


				StartFormData startFormData = formService.getStartFormData(activitiTask.getProcessDefinitionId());	
				tsk.setBusinessKey(startFormData.getProcessDefinition().getKey());
				tasks.add(tsk);
			}

			return tasks;
		}catch(ActivitiException e) {
			LOGGER.error(e.getMessage());
			throw new InvalidDataException(e.getMessage(),"","");
		}
	}

	public ProcessDefinition getProcessDefinitionForms(final ProcessDefinitionRequest processDefinitionRequest) {
		ProcessDefinition processDefinition = processDefinitionRequest.getProcessDefinition();
		try {		
			ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
			if(processDefinition !=null && processDefinition.getId() != null) {
				query.processDefinitionKey(processDefinition.getId());
			}else if(processDefinition !=null && processDefinition.getBusinessKey() != null) {
				query.processDefinitionKey(processDefinition.getBusinessKey());
			}

			org.activiti.engine.repository.ProcessDefinition activitiProcessDefinition = query.latestVersion().singleResult();
			if (activitiProcessDefinition == null){
				throw new NoDataFoundException("Required Valid 'businessKey' or 'ProcessDefinitionId'.");
			} 

			StartFormData startFormData = formService.getStartFormData(activitiProcessDefinition.getId());	
			List<ActivitiFormProperty> activitiFormProperty = formPropertyListConverter(startFormData.getFormProperties());
			processDefinition.setFormProperties(activitiFormProperty);
			processDefinition.setId(startFormData.getProcessDefinition().getId());
			processDefinition.setName(startFormData.getProcessDefinition().getName());
			processDefinition.setBusinessKey(startFormData.getProcessDefinition().getKey());
			processDefinition.setVersion(startFormData.getProcessDefinition().getVersion());
			processDefinition.setDescription(startFormData.getProcessDefinition().getDescription());

			return processDefinition;
		}catch(ActivitiException e) {
			LOGGER.error(e.getMessage());
			throw new NoDataFoundException(e.getMessage());
		}
	}

	public List<ProcessInstance> getProcessInstance(final ProcessInstanceRequest processInstanceRequest) {
		List<ProcessInstance> processInstanceList = new ArrayList<ProcessInstance>();
		ProcessInstance processInstance = processInstanceRequest.getProcessInstance();
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();

		List<org.activiti.engine.runtime.ProcessInstance> activitiProcessInstanceList= null;

		if (processInstance.getId() != null) {
			processInstanceQuery.processInstanceId(processInstance.getId());
		} 
		if(processInstance.getBusinessKey() != null) {
			processInstanceQuery.processDefinitionKey(processInstance.getBusinessKey());
		}
		if(processInstance.getProcessDefinationId() != null) {
			processInstanceQuery.processDefinitionId(processInstance.getProcessDefinationId());
		}

		activitiProcessInstanceList = processInstanceQuery.list();
		if (activitiProcessInstanceList == null) {
			throw new NoDataFoundException("Could not find a process instance with id '" + processInstance.getId() + "'.");
		}

		for(org.activiti.engine.runtime.ProcessInstance activitiProcessInstance : activitiProcessInstanceList) {
			ProcessInstance pi = new ProcessInstance();
			pi.setId(activitiProcessInstance.getId());
			pi.setProcessDefinationId(activitiProcessInstance.getProcessDefinitionId());
			pi.setCreatedDate(activitiProcessInstance.getStartTime());	
			pi.setName(activitiProcessInstance.getName());
			pi.setComments(activitiProcessInstance.getDescription());
			//Getting formPropertis for ProcessInstance
			StartFormData startFormData = formService.getStartFormData(activitiProcessInstance.getProcessDefinitionId());		
			List<FormProperty> formPropertiesList = startFormData.getFormProperties();
			List<ActivitiFormProperty> activitiFormProperties = formPropertyListConverter(formPropertiesList);
			pi.setFormProperties(activitiFormProperties);
			processInstanceList.add(pi);
		}

		return processInstanceList;
	}
	public Task update(final TaskRequest taskRequest) {
		Task task = taskRequest.getTask();
		String action = task.getAction();
		Position assignee = task.getAssignee();
		String assigneeId = null;
		if(assignee != null) {
			assigneeId = assignee.getId().toString();
		}
		org.activiti.engine.task.Task activitiTask = taskService.createTaskQuery().taskId(task.getId()).singleResult();
		if (activitiTask == null) {
			throw new NoDataFoundException("Task not found with id: " + task.getId());
		}		
		if (action.equals(null) || action.isEmpty()) {
			throw new NoDataFoundException("A request body was expected when executing a task action.");
		}
		try {
			if (action.equals(WorkflowConstants.ACTION_COMPLETE)) {

				if(task.getAssignee() == null || task.getAssignee().getId() == null) {
					LOGGER.error("Assignee : " + task.getAssignee() + " =====>> Required assignee." );
					throw new NoDataFoundException("Required 'assignee'.");
				}
				
				activitiTask.setDescription(task.getComments());
				activitiTask.setPriority(task.getPriority());
				taskService.saveTask(activitiTask);
				
				if(task.getFormProperties() != null) 
					formService.submitTaskFormData(activitiTask.getId(), buildMap(task.getFormProperties()));
				else				
					formService.submitTaskFormData(activitiTask.getId(), new HashMap<String, String>());

				List<org.activiti.engine.task.Task> actiTasks = taskService.createTaskQuery().processInstanceId(activitiTask.getProcessInstanceId()).list();
				for(org.activiti.engine.task.Task actiTask : actiTasks) {
					taskService.claim(actiTask.getId(),task.getAssignee().getId().toString());
				}

			} else if (action.equals(WorkflowConstants.ACTION_CLAIM)) {
				taskService.claim(task.getId(),assigneeId);
			} else if (action.equals(WorkflowConstants.ACTION_DELEGATE)) {
				taskService.delegateTask(task.getId(), assigneeId);
			} else if (action.equals(WorkflowConstants.ACTION_RESOLVE)) {
				taskService.resolveTask(task.getId());
			} else if (action.equals(WorkflowConstants.ACTION_UPDATE)) {
				activitiTask.setAssignee(assigneeId);
				activitiTask.setOwner(assigneeId);

				Date dueDate = null;
				try {
					if(task.getDueDate() != null)
						dueDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").parse(task.getDueDate());
					activitiTask.setDueDate(dueDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				activitiTask.setDescription(task.getComments());
				activitiTask.setPriority(task.getPriority());
				if(task.getCandidateGroup() != null)
					taskService.addCandidateGroup(activitiTask.getId(), task.getCandidateGroup());
				taskService.saveTask(activitiTask);
			} else {
				LOGGER.error("Invalid action");
				throw new InvalidDataException("Invalid action","","");
			}
		}catch(ActivitiException e) {
			LOGGER.error(e.getMessage());
			throw new NoDataFoundException(e.getMessage());
		}
		return task;
	}

	public ProcessInstanceResponse getHistoryDetail(final ProcessInstanceRequest historyRequest) {
		ProcessInstanceResponse historyResponse = new ProcessInstanceResponse();
		ProcessInstance processInstance = historyRequest.getProcessInstance();
		List<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();

		//Getting HistoricProcessInstance Details 
		HistoricProcessInstanceQuery query1 = historyService.createHistoricProcessInstanceQuery();

		if (processInstance.getId() != null) {
			query1.processInstanceId(processInstance.getId());
		}
		if (processInstance.getBusinessKey() != null) {
			query1.processDefinitionKey(processInstance.getBusinessKey());
		}
		if (processInstance.getProcessDefinationId() != null) {
			query1.processDefinitionId(processInstance.getProcessDefinationId());
		}

		List<HistoricProcessInstance> historicProcessInstanceList = query1.list();
		//	HistoricProcessInstance historicPI = query1.singleResult();

		for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
			ProcessInstance proInstance = new ProcessInstance();
			proInstance.setId(historicProcessInstance.getId());
			proInstance.setName(historicProcessInstance.getName());
			proInstance.setProcessDefinationId(historicProcessInstance.getProcessDefinitionId());
			proInstance.setBusinessKey(historicProcessInstance.getProcessDefinitionKey());
			proInstance.setDetails(historicProcessInstance.getDescription());
			proInstance.setCreatedDate(historicProcessInstance.getStartTime());
			proInstance.setEndTime(historicProcessInstance.getEndTime());

			if(historicProcessInstance.getEndTime() != null) {
				proInstance.setStatus("Completted");
			}else {
				proInstance.setStatus("Inprogress");
			}

			//Getting HistoricTaskInstance details for Process Instance

			HistoricTaskInstanceQuery query2 = historyService.createHistoricTaskInstanceQuery();
			query2.processInstanceId(historicProcessInstance.getId());

			List<HistoricTaskInstance> historicTaskInstanceList = query2.list();


			for(HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
				Task tsk = new Task();
				tsk.setId(historicTaskInstance.getId());

				tsk.setName(historicTaskInstance.getName());
				tsk.setCreatedDate(historicTaskInstance.getCreateTime());

				DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				String strDueDate = null;
				if(historicTaskInstance.getDueDate() != null)
					strDueDate = df.format(historicTaskInstance.getDueDate());				
				tsk.setDueDate(strDueDate);

				tsk.setComments(historicTaskInstance.getDescription());
				tsk.setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId());
				tsk.setEndTime(historicTaskInstance.getEndTime());
				if(historicTaskInstance.getEndTime() != null) {
					tsk.setStatus(historicTaskInstance.getName() +" is Completted");
				}else {
					tsk.setStatus(historicTaskInstance.getName() + " is Pending");
				}

				if(historicTaskInstance.getAssignee() != null) {
					Position p = new Position();
					p.setId(Long.parseLong(historicTaskInstance.getAssignee()));
					tsk.setAssignee(p);
				}
				
				tsk.setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
				proInstance.getTasks().add(tsk);
			}

			//Getting Form Properties per process Instance and Task

			List<HistoricDetail> list = historyService.createHistoricDetailQuery().processInstanceId(proInstance.getId()).list();

			for (HistoricDetail instance : list) {
				ActivitiFormProperty activitiFormProperty = new ActivitiFormProperty();
				if(instance.getTaskId() == null) {
					if (instance instanceof HistoricFormProperty) {
						HistoricFormProperty formProperty = (HistoricFormProperty) instance;
						activitiFormProperty.setId(formProperty.getPropertyId());
						activitiFormProperty.setValue(formProperty.getPropertyValue());
						proInstance.getFormProperties().add(activitiFormProperty);
					}
				} else {
					for(Task tsk : proInstance.getTasks()) {
						if(tsk.getId().equals(instance.getTaskId())) {
							if (instance instanceof HistoricFormProperty) {
								HistoricFormProperty formProperty = (HistoricFormProperty) instance;
								activitiFormProperty.setId(formProperty.getPropertyId());
								activitiFormProperty.setValue(formProperty.getPropertyValue());
								tsk.getFormProperties().add(activitiFormProperty);
								break;
							}
						}
					}
				}
			}

			processInstances.add(proInstance);
		}
		historyResponse.setProcessInstances(processInstances);
		return historyResponse;
	}

	private Map<String, String> buildMap(List<ActivitiFormProperty> requestFormPropertyList){
		if (requestFormPropertyList == null) 
			throw new NoDataFoundException("Could not find FormProperties.");
		Map<String, String> requestFormPropertiesMap = new HashMap<String, String>();
		for(ActivitiFormProperty requestFormProperty : requestFormPropertyList) {
			requestFormPropertiesMap.put(requestFormProperty.getId(),requestFormProperty.getValue());
		}
		return requestFormPropertiesMap;
	}

	private List<ActivitiFormProperty> formPropertyListConverter(List<FormProperty> formPropertiesList){
		List<ActivitiFormProperty> activitiFormPropertys = new ArrayList<ActivitiFormProperty>();
		for (FormProperty formProp : formPropertiesList) {
			ActivitiFormProperty restFormProp = new ActivitiFormProperty();
			restFormProp.setId(formProp.getId());
			restFormProp.setName(formProp.getName());
			if (formProp.getType() != null) {
				restFormProp.setType(formProp.getType().getName());
			}
			restFormProp.setValue(formProp.getValue());
			restFormProp.setReadable(formProp.isReadable());
			restFormProp.setRequired(formProp.isRequired());
			restFormProp.setWritable(formProp.isWritable());
			if ("enum".equals(restFormProp.getType())) {
				Object values = formProp.getType().getInformation("values");
				if (values != null) {
					@SuppressWarnings("unchecked")
					Map<String, String> enumValues = (Map<String, String>) values;
					for (String enumId : enumValues.keySet()) {
						ActivitiEnumFormProperty enumProperty = new ActivitiEnumFormProperty();
						enumProperty.setId(enumId);
						enumProperty.setName(enumValues.get(enumId));
						restFormProp.addEnumValue(enumProperty);
					}
				}
			} else if ("date".equals(restFormProp.getType())) {
				restFormProp.setDatePattern((String) formProp.getType().getInformation("datePattern"));
			}
			activitiFormPropertys.add(restFormProp);
		}
		return activitiFormPropertys;
	}
}
