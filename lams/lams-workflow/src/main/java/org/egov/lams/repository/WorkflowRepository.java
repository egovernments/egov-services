package org.egov.lams.repository;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.Position;
import org.egov.lams.contract.ProcessInstance;
import org.egov.lams.contract.ProcessInstanceRequest;
import org.egov.lams.contract.ProcessInstanceResponse;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.contract.RequestInfoWrapper;
import org.egov.lams.contract.Task;
import org.egov.lams.contract.TaskRequest;
import org.egov.lams.contract.TaskResponse;
import org.egov.lams.contract.TenantResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.City;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Repository
public class WorkflowRepository {

	public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowRepository.class);
	
	public static final String ACTION = "Forward";

	@Autowired
	private LogAwareKafkaTemplate<String, Object> kafkaTemplate;
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	public ProcessInstance startWorkflow(AgreementRequest agreementRequest) {

		ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(agreementRequest);

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();

		LOGGER.info("string url of the workflow appp : " + url + "WorkflowRepository processInstanceRequest : "
				+ processInstanceRequest);

		ProcessInstanceResponse processInstanceRes = null;
		try {
			processInstanceRes = restTemplate.postForObject(url, processInstanceRequest, ProcessInstanceResponse.class);
		} catch (Exception e) {
			LOGGER.info("the exception from workflow service call : " + e);
			throw e;
		}
		LOGGER.info("the response object from workflow : " + processInstanceRes.getProcessInstance().getId());

		saveAgreement(agreementRequest, processInstanceRes.getProcessInstance().getId());
		return processInstanceRes.getProcessInstance();
	}

	public TaskResponse updateWorkflow(AgreementRequest agreementRequest) {

		TaskRequest taskRequest = getTaskRequest(agreementRequest);
		Agreement agreement = agreementRequest.getAgreement();

		String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
				+ "/" + agreement.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

		LOGGER.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
		TaskResponse taskResponse = null;
		try {
			taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			throw e;
		}
		LOGGER.info("the response from workflow update : " + taskResponse.getTask());
		if (agreement.getWorkflowDetails() != null) {
			updateAgreement(agreementRequest);
		}
		return taskResponse;
	}

	public void saveAgreement(AgreementRequest agreementRequest, String stateId) {
		agreementRequest.getAgreement().setStateId(stateId);
		try {
			kafkaTemplate.send(propertiesManager.getKafkaSaveAgreementTopic(), "key", agreementRequest);
		} catch (Exception exception) {
			LOGGER.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	public void updateAgreement(AgreementRequest agreementRequest) {
		try {
			kafkaTemplate.send(propertiesManager.getKafkaUpdateAgreementTopic(), "key", agreementRequest);
		} catch (Exception exception) {
			LOGGER.info("AgreementService : " + exception.getMessage(), exception);
			throw exception;
		}
	}

	private ProcessInstanceRequest getProcessInstanceRequest(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();
		WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		Position assignee = new Position();
		ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
		ProcessInstance processInstance = new ProcessInstance();
		Boolean isCorporation = isCorporation(agreement.getTenantId());
		LOGGER.info("isCorporation :" + isCorporation);
		assignee.setId(workFlowDetails.getAssignee());
		if (Action.JUDGEMENT.equals(agreement.getAction())) {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceJudgementCorporationBusinessKey() : propertiesManager.getWorkflowServiceJudgementMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceJudgementCorporationBusinessKey() : propertiesManager.getWorkflowServiceJudgementMunicipalityBusinessKey());
		} else if (Action.OBJECTION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceObjectionCorporationBusinessKey() : propertiesManager.getWorkflowServiceObjectionMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceObjectionCorporationBusinessKey() : propertiesManager.getWorkflowServiceObjectionMunicipalityBusinessKey());
		} else if (Action.RENEWAL.equals(agreement.getAction())) {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceRenewCorporationBusinessKey() : propertiesManager.getWorkflowServiceRenewMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceRenewCorporationBusinessKey() : propertiesManager.getWorkflowServiceRenewMunicipalityBusinessKey());
		} else if (Action.CANCELLATION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceCancelCorporationBusinessKey() : propertiesManager.getWorkflowServiceCancelMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceCancelCorporationBusinessKey() : propertiesManager.getWorkflowServiceCancelMunicipalityBusinessKey());
		} else if (Action.EVICTION.equals(agreement.getAction())) {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceEvictCorporationBusinessKey() : propertiesManager.getWorkflowServiceEvictMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceEvictCorporationBusinessKey() : propertiesManager.getWorkflowServiceEvictMunicipalityBusinessKey());
		} else {
			processInstance.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceCreateCorporationBusinessKey() : propertiesManager.getWorkflowServiceCreateMunicipalityBusinessKey());
			processInstance.setType(isCorporation ? propertiesManager.getWorkflowServiceCreateCorporationBusinessKey() : propertiesManager.getWorkflowServiceCreateMunicipalityBusinessKey());
		}
		LOGGER.info("process businesskey :" + processInstance.getBusinessKey());
		LOGGER.info("process type: " + processInstance.getType());
		processInstance.setAssignee(assignee);
		processInstance.setComments(workFlowDetails.getComments());
		processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
		processInstance.setTenantId(agreement.getTenantId());
		processInstance.setDetails("Acknowledgement Number : " + agreement.getAcknowledgementNumber());
		processInstanceRequest.setProcessInstance(processInstance);
		processInstanceRequest.setRequestInfo(requestInfo);

		return processInstanceRequest;
	}

	private TaskRequest getTaskRequest(AgreementRequest agreementRequest) {

		LOGGER.info("isnide update workflow workflow details value ::: "
				+ agreementRequest.getAgreement().getWorkflowDetails());
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		WorkflowDetails workflowDetails = agreement.getWorkflowDetails();
		TaskRequest taskRequest = new TaskRequest();
		Task task = new Task();
		Position assignee = new Position();
		Boolean isCorporation = isCorporation(agreement.getTenantId());
		LOGGER.info("isCorporation :" + isCorporation);
		taskRequest.setRequestInfo(requestInfo);
		if (Action.JUDGEMENT.equals(agreement.getAction())) {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceJudgementCorporationBusinessKey() : propertiesManager.getWorkflowServiceJudgementMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceJudgementCorporationBusinessKey() : propertiesManager.getWorkflowServiceJudgementMunicipalityBusinessKey());
		} else if (Action.OBJECTION.equals(agreement.getAction())) {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceObjectionCorporationBusinessKey() : propertiesManager.getWorkflowServiceObjectionMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceObjectionCorporationBusinessKey() : propertiesManager.getWorkflowServiceObjectionMunicipalityBusinessKey());
		} else if (Action.RENEWAL.equals(agreement.getAction())) {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceRenewCorporationBusinessKey() : propertiesManager.getWorkflowServiceRenewMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceRenewCorporationBusinessKey() : propertiesManager.getWorkflowServiceRenewMunicipalityBusinessKey());
		} else if (Action.CANCELLATION.equals(agreement.getAction())) {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceCancelCorporationBusinessKey() : propertiesManager.getWorkflowServiceCancelMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceCancelCorporationBusinessKey() : propertiesManager.getWorkflowServiceCancelMunicipalityBusinessKey());
		} else if (Action.EVICTION.equals(agreement.getAction())) {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceEvictCorporationBusinessKey() : propertiesManager.getWorkflowServiceEvictMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceEvictCorporationBusinessKey() : propertiesManager.getWorkflowServiceEvictMunicipalityBusinessKey());
		} else {
			task.setBusinessKey(isCorporation ? propertiesManager.getWorkflowServiceCreateCorporationBusinessKey() : propertiesManager.getWorkflowServiceCreateMunicipalityBusinessKey());
			task.setType(isCorporation ? propertiesManager.getWorkflowServiceCreateCorporationBusinessKey() : propertiesManager.getWorkflowServiceCreateMunicipalityBusinessKey());
		}
		LOGGER.info("task businesskey :" + task.getBusinessKey());
		LOGGER.info("task type: " + task.getType());
		task.setId(agreement.getStateId());
		task.setTenantId(agreement.getTenantId());
		if (workflowDetails != null) {

			task.setAction(workflowDetails.getAction());
			if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getStatus())
					&& agreement.getIsAdvancePaid()
					&& propertiesManager.getAgreementStatusRejected().equalsIgnoreCase(agreement.getStatus().toString())
					&& agreement.getAction().equals(Action.CREATE)) {

				task.setStatus(propertiesManager.getWfStatusAssistantApproved());
				LOGGER.info("updating the task status for reject-forward case after payment : ", task.getStatus());
			} else
				task.setStatus(workflowDetails.getStatus());
			assignee.setId(workflowDetails.getAssignee());
			task.setAssignee(assignee);
			task.setComments(workflowDetails.getComments());
		} else {

			LOGGER.info("The workflow object before collection update ::: " + agreement.getWorkflowDetails());
			String url = propertiesManager.getWorkflowServiceHostName()
					+ propertiesManager.getWorkflowServiceSearchPath() + "?id=" + agreement.getStateId() + "&tenantId="
					+ agreement.getTenantId();

			ProcessInstanceResponse processInstanceResponse = null;
			try {
				processInstanceResponse = restTemplate.postForObject(url, requestInfoWrapper,
						ProcessInstanceResponse.class);
			} catch (Exception e) {
				LOGGER.info("exception from search workflow call ::: " + e);
				throw e;
			}

			LOGGER.info("process instance responce ::: from search ::: " + processInstanceResponse);
			ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
			//List<Attribute> attributes = new ArrayList<>(processInstance.getAttributes().values());
			task.setAction(ACTION);
			task.setStatus(processInstance.getStatus());
			assignee = processInstance.getOwner();
			LOGGER.info("the owner object from response is ::: " + processInstance.getOwner());
			task.setAssignee(assignee);
		}
		taskRequest.setTask(task);

		return taskRequest;
	}

	private Boolean isCorporation(String tenantId) {

		City city;
		Boolean isCorporation = Boolean.FALSE;
		String url = propertiesManager.getTenantServiceHostName() + "tenant/v1/tenant/_search?code=" + tenantId;
		TenantResponse tr = restTemplate.postForObject(url, new RequestInfo(), TenantResponse.class);
		LOGGER.info("Tenant response :" + tr.toString());
		if (!CollectionUtils.isEmpty(tr.getTenant())) {

			city = tr.getTenant().get(0).getCity();
			LOGGER.info("City details :" + city.toString());
			if (propertiesManager.getCityGradeCorp().equalsIgnoreCase(city.getUlbGrade())) {
				isCorporation = Boolean.TRUE;
			}
		}
		return isCorporation;
	}
     
}
