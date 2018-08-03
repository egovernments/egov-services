/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) 2016  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.lams.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.Attribute;
import org.egov.lams.web.contract.Position;
import org.egov.lams.web.contract.ProcessInstance;
import org.egov.lams.web.contract.ProcessInstanceRequest;
import org.egov.lams.web.contract.ProcessInstanceResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.Task;
import org.egov.lams.web.contract.TaskRequest;
import org.egov.lams.web.contract.TaskResponse;
import org.egov.lams.web.contract.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class WorkFlowRepository {

    public static final String ACTION = "Forward";

    public static final String COMMISSIONER = "Commissioner";

    public static final String ACTION_FORWARDED = " Forwarded";

    public static final String ACTION_APPROVAL_PENDING = " Approval Pending";

    @Autowired
    private PropertiesManager propertiesManager;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Task getWorkFlowState(final String stateId, final String tenantId, final RequestInfo requestInfo) {

        final String url = propertiesManager.getCommonWorkFlowServiceHostName()
                + propertiesManager.getCommonWorkFlowServiceSearchPath()
                + "?tenantId=" + tenantId + "&id=" + stateId;

        final RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);
        TaskResponse taskResponse = null;

        try {
            taskResponse = restTemplate.postForObject(url, requestInfoWrapper, TaskResponse.class);
        } catch (final Exception e) {
            log.error("the exception from workflowhistory api call : " + e);
            throw e;
        }
        log.info("the response from workflowhistory api call : " + taskResponse);
        return taskResponse.getTasks().get(0);
    }

    public String getCommissionerName(final String stateId, final String tenantId, final RequestInfo requestInfo) {

        String commisionerName = null;
        final String url = propertiesManager.getCommonWorkFlowServiceHostName()
                + propertiesManager.getCommonWorkFlowServiceHistoryPath() + "?tenantId=" + tenantId + "&workflowId="
                + stateId;

        final RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper(requestInfo);
        log.info("the url for workflowhistory api call : " + url);
        TaskResponse taskResponse = null;

        try {
            taskResponse = restTemplate.postForObject(url, requestInfoWrapper, TaskResponse.class);
        } catch (final Exception e) {
            log.error("the exception from workflowhistory api call : " + e);
            throw e;
        }
        log.info("the response from workflow search : " + taskResponse);
        final List<Task> workFlowHistory = taskResponse.getTasks();
        for (final Task task : workFlowHistory)
            if ("Commissioner Approved".equalsIgnoreCase(task.getStatus()))
                commisionerName = task.getSenderName();
        return commisionerName;
    }

    public ProcessInstance startWorkflow(final AgreementRequest agreementRequest) {

        final ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(agreementRequest);

        final String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceStartPath();

        log.info("string url of the workflow appp : " + url + "WorkflowRepository processInstanceRequest : "
                + processInstanceRequest);

        ProcessInstanceResponse processInstanceRes = null;
        try {
            processInstanceRes = restTemplate.postForObject(url, processInstanceRequest, ProcessInstanceResponse.class);
        } catch (final Exception e) {
            log.info("the exception from workflow service call : " + e);
            throw e;
        }
        log.info("the response object from workflow : " + processInstanceRes.getProcessInstance().getId());

        saveAgreement(agreementRequest, processInstanceRes.getProcessInstance().getId());
        return processInstanceRes.getProcessInstance();
    }

    public TaskResponse updateWorkflow(final AgreementRequest agreementRequest) {

        final TaskRequest taskRequest = getTaskRequest(agreementRequest);
        final Agreement agreement = agreementRequest.getAgreement();

        final String url = propertiesManager.getWorkflowServiceHostName() + propertiesManager.getWorkflowServiceTaskPAth()
                + "/" + agreement.getStateId() + propertiesManager.getWorkflowServiceUpdatePath();

        log.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
        TaskResponse taskResponse = null;
        try {
            taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
        } catch (final Exception e) {
            log.error(e.toString());
            throw e;
        }
        log.info("the response from workflow update : " + taskResponse.getTask());
        if (agreement.getWorkflowDetails() != null)
            updateAgreement(agreementRequest);
        return taskResponse;
    }

    public void saveAgreement(final AgreementRequest agreementRequest, final String stateId) {
        agreementRequest.getAgreement().setStateId(stateId);
        agreementRepository.saveAgreement(agreementRequest);
    }

    public void updateAgreement(final AgreementRequest agreementRequest) {
        agreementRepository.updateAgreement(agreementRequest);
    }

    private ProcessInstanceRequest getProcessInstanceRequest(final AgreementRequest agreementRequest) {

        final Agreement agreement = agreementRequest.getAgreement();
        final WorkflowDetails workFlowDetails = agreement.getWorkflowDetails();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final Position assignee = new Position();
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();
        assignee.setId(workFlowDetails.getAssignee());
        if (Action.JUDGEMENT.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
        } else if (Action.OBJECTION.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
        } else if (Action.RENEWAL.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
        } else if (Action.CANCELLATION.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
        } else if (Action.EVICTION.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
        } else if (Action.REMISSION.equals(agreement.getAction())) {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
        } else {
            processInstance.setBusinessKey(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
            processInstance.setType(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
        }
        log.info("process businesskey :" + processInstance.getBusinessKey());
        log.info("process type: " + processInstance.getType());
        processInstance.setAssignee(assignee);
        processInstance.setComments(workFlowDetails.getComments());
        processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
        processInstance.setTenantId(agreement.getTenantId());
        processInstance.setDetails("Acknowledgement Number : " + agreement.getAcknowledgementNumber());
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);

        return processInstanceRequest;
    }

    private TaskRequest getTaskRequest(final AgreementRequest agreementRequest) {

        log.info("isnide update workflow workflow details value ::: "
                + agreementRequest.getAgreement().getWorkflowDetails());
        final Agreement agreement = agreementRequest.getAgreement();
        final RequestInfo requestInfo = agreementRequest.getRequestInfo();
        final RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        final WorkflowDetails workflowDetails = agreement.getWorkflowDetails();
        final TaskRequest taskRequest = new TaskRequest();
        final Task task = new Task();
        Position assignee = new Position();
        taskRequest.setRequestInfo(requestInfo);
        if (Action.JUDGEMENT.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceJudgementAgreementBusinessKey());
        } else if (Action.OBJECTION.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceObjectionAgreementBusinessKey());
        } else if (Action.RENEWAL.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceRenewalAgreementBusinessKey());
        } else if (Action.CANCELLATION.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceCancellationAgreementBusinessKey());
        } else if (Action.EVICTION.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceEvictionAgreementBusinessKey());
        } else if (Action.REMISSION.equals(agreement.getAction())) {
            task.setBusinessKey(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceRemissionAgreementBusinessKey());
        } else {
            task.setBusinessKey(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
            task.setType(propertiesManager.getWorkflowServiceCreateAgreementBusinessKey());
        }
        log.info("task businesskey :" + task.getBusinessKey());
        log.info("task type: " + task.getType());
        task.setId(agreement.getStateId());
        task.setTenantId(agreement.getTenantId());
        if (workflowDetails != null) {

            task.setAction(workflowDetails.getAction());
            if (propertiesManager.getWfStatusRejected().equalsIgnoreCase(workflowDetails.getStatus())
                    && agreement.getIsAdvancePaid() && agreement.getAction().equals(Action.CREATE)) {

                task.setStatus(propertiesManager.getWfStatusAssistantApproved());
                log.info("updating the task status for reject-forward case after payment : ", task.getStatus());
            } else
                task.setStatus(workflowDetails.getStatus());

            if (ACTION.equalsIgnoreCase(workflowDetails.getAction())
                    && (workflowDetails.getDesignation() != null || workflowDetails.getNextDesignation() != null)) {
                Attribute stateAttribute = null;
                Attribute nextActionAttribute = null;
                Attribute designationAttribute = null;
                final Map<String, Attribute> attributeMap = new HashMap<>();

                if (workflowDetails.getDesignation().endsWith(COMMISSIONER)) {
                    stateAttribute = getNextStateAttribute(workflowDetails.getDesignation());
                    nextActionAttribute = getNextActionAttribute(workflowDetails.getNextDesignation());
                    designationAttribute = getCurrentDesigantionAttribute(workflowDetails.getDesignation());
                    attributeMap.put("nextState", stateAttribute);
                    attributeMap.put("nextAction", nextActionAttribute);
                    attributeMap.put("currentDesignation", designationAttribute);
                    task.setAttributes(attributeMap);
                } else if (COMMISSIONER.equalsIgnoreCase(workflowDetails.getNextDesignation())) {

                    nextActionAttribute = getNextActionAttribute(workflowDetails.getNextDesignation());
                    designationAttribute = getCurrentDesigantionAttribute(workflowDetails.getDesignation());
                    attributeMap.put("nextAction", nextActionAttribute);
                    attributeMap.put("currentDesignation", designationAttribute);
                    task.setAttributes(attributeMap);
                }

            }
            assignee.setId(workflowDetails.getAssignee());
            task.setAssignee(assignee);
            task.setComments(workflowDetails.getComments());
        } else {

            log.info("The workflow object before collection update ::: " + agreement.getWorkflowDetails());
            final String url = propertiesManager.getWorkflowServiceHostName()
                    + propertiesManager.getWorkflowServiceSearchPath() + "?id=" + agreement.getStateId() + "&tenantId="
                    + agreement.getTenantId();

            ProcessInstanceResponse processInstanceResponse = null;
            try {
                processInstanceResponse = restTemplate.postForObject(url, requestInfoWrapper,
                        ProcessInstanceResponse.class);
            } catch (final Exception e) {
                log.info("exception from search workflow call ::: " + e);
                throw e;
            }

            log.info("process instance responce ::: from search ::: " + processInstanceResponse);
            final ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
            // List<Attribute> attributes = new ArrayList<>(processInstance.getAttributes().values());
            task.setAction(ACTION);
            task.setStatus(processInstance.getStatus());
            assignee = processInstance.getOwner();
            log.info("the owner object from response is ::: " + processInstance.getOwner());
            task.setAssignee(assignee);
        }
        taskRequest.setTask(task);

        return taskRequest;
    }

    private Attribute getNextStateAttribute(final String currentDesignation) {
        final List<Value> nextStateValue = new ArrayList<>();
        final Attribute stateAttribute = new Attribute();
        final Value nextState = new Value();
        nextState.setKey("nextState");
        nextState.setName(currentDesignation.concat(ACTION_FORWARDED));
        nextStateValue.add(nextState);
        stateAttribute.setCode("WFSTATE");
        stateAttribute.setValues(nextStateValue);
        return stateAttribute;
    }

    private Attribute getNextActionAttribute(final String nextDesignation) {
        final List<Value> nextActionValues = new ArrayList<>();
        final Attribute actionAttribute = new Attribute();
        final Value nextAction = new Value();
        nextAction.setKey("pendingAction");
        nextAction.setName(nextDesignation.concat(ACTION_APPROVAL_PENDING));
        nextActionValues.add(nextAction);
        actionAttribute.setCode("ACTION");
        actionAttribute.setValues(nextActionValues);
        return actionAttribute;
    }

    private Attribute getCurrentDesigantionAttribute(final String currentDesignation) {
        final List<Value> designationValues = new ArrayList<>();
        final Attribute designationAttribute = new Attribute();
        final Value designation = new Value();
        designation.setKey("designation");
        designation.setName(currentDesignation);
        designationValues.add(designation);
        designationAttribute.setCode("DESIG");
        designationAttribute.setValues(designationValues);
        return designationAttribute;
    }

}
