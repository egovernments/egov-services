package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.Attribute;
import org.egov.lams.contract.Position;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.contract.ProcessInstanceRequest;
import org.egov.lams.contract.ProcessInstanceResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.contract.Task;
import org.egov.lams.contract.TaskRequest;
import org.egov.lams.contract.TaskResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.WorkFlowDetails;
import org.egov.lams.producers.AgreementProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WorkflowRepository {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowRepository.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AgreementProducer agreementProducer;

	@Autowired
	private PropertiesManager propertiesManager;

	public ProcessInstance startWorkflow(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		WorkFlowDetails workFlowDetails = agreement.getWorkflowDetails();
		System.err.println(agreement);

		Position assignee = new Position();
		assignee.setId(workFlowDetails.getAssignee());

		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();

		ProcessInstance processInstance = new ProcessInstance();
		processInstance.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
		processInstance.setType(propertiesManager.getWorkflowServiceBusinessKey());
		processInstance.setAssignee(assignee);
		processInstance.setComments("statrting workflow from lams consumer app");
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(agreementRequest.getRequestInfo());
		
		processInstance.setTenantId(agreement.getTenantId());
		// TODO put business  key ina pp.propscomments

		String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceStartPath();
		LOGGER.info("string url of the workflow appp : "+url);
		
		LOGGER.info("WorkflowRepository processInstanceRequest : "+processInstanceRequest);

		ProcessInstanceResponse  processInstanceRes = null;
		try {
			processInstanceRes = restTemplate.postForObject(url, processInstanceRequest,ProcessInstanceResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.toString());
			throw e;
		}
		//FIXME the response should be always processInstanceResponse
		LOGGER.error("the response object from workflow : " +processInstanceRes.getProcessInstance().getId());
		return processInstanceRes.getProcessInstance();
	}

	public TaskResponse updateWorkflow(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		
		TaskRequest taskRequest = new TaskRequest();
		taskRequest.setRequestInfo(requestInfo);
		
		Task task = new Task();
		task.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
		task.setType(propertiesManager.getWorkflowServiceBusinessKey());
		// FIXME business key get confirmed form workflow module
		task.setId(agreement.getStateId());
		
		Position assignee = new Position();
		
		if (agreementRequest.getAgreement().getWorkflowDetails() != null) {
			
			WorkFlowDetails workFlowDetails = agreement.getWorkflowDetails();
			task.setAction(workFlowDetails.getAction());
			task.setStatus(workFlowDetails.getStatus());
			assignee.setId(workFlowDetails.getAssignee());
			task.setAssignee(assignee);
		}else
		{
			LOGGER.info("The workflow object before collection update ::: "+agreement.getWorkflowDetails());
			String url = propertiesManager.getWorkflowServiceHostName()
					   + propertiesManager.getWorkflowServiceSearchPath()
					   + "?id="+agreement.getStateId()
					   + "&tenantId="+requestInfo.getUserInfo().getTenantId();
			
			ProcessInstanceResponse processInstanceResponse = null;
			try{
				processInstanceResponse = restTemplate.postForObject(url, requestInfo, ProcessInstanceResponse.class);
			}catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("exception from search workflow call ::: "+e);
			}
			ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
			//FIXME not sure how to process it 
			List<Attribute> attributes = new ArrayList<>(processInstance.getAttributes().values());
			task.setAction(attributes.get(0).getValues().get(0).getKey());
			task.setStatus(processInstance.getStatus());
			assignee = processInstance.getAssignee();
			task.setAssignee(assignee);
			
		}
			taskRequest.setTask(task);
		
		String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceTaskPAth() 
					+ "/"+agreement.getStateId()
					+ propertiesManager.getWorkflowServiceUpdatePath();
		
		LOGGER.error("the task object :::: "+ task + " ::url:: "+ url);

		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.info(e.toString());
			e.printStackTrace();
		}
			LOGGER.info("the response from task update : "+taskResponse.getTask());
		return taskResponse;
	}

	public void saveAgreement(AgreementRequest agreementRequest) {
		String agreementRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			agreementRequestMessage	= objectMapper.writeValueAsString(agreementRequest);
		}catch (Exception e) {
			LOGGER.info("WorkflowRepositorysaveAgreement : " +e);
			throw new RuntimeException(e);
		}
		agreementProducer.sendMessage(propertiesManager.getKafkaSaveAgreementTopic(), "key", agreementRequestMessage);
	}
	
	public void updateAgreement(AgreementRequest agreementRequest) {
		String agreementRequestMessage = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try{
			agreementRequestMessage	= objectMapper.writeValueAsString(agreementRequest);
		}catch (Exception e) {
			LOGGER.info("WorkflowRepositoryupdateAgreement : " +e);
			throw new RuntimeException(e);
		}
		//FIXME TODO remove the hard coding and place the property manager string
		agreementProducer.sendMessage(propertiesManager.getKafkaUpdateAgreementTopic(), "key", agreementRequestMessage);
	}

}
