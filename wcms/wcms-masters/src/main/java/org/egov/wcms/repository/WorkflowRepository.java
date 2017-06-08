/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.wcms.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.wcms.config.ConfigurationManager;
import org.egov.wcms.model.Connection;
import org.egov.wcms.model.WorkflowDetails;
import org.egov.wcms.producers.WaterTransactionProducer;
import org.egov.wcms.web.contract.Position;
import org.egov.wcms.web.contract.ProcessInstance;
import org.egov.wcms.web.contract.ProcessInstanceRequest;
import org.egov.wcms.web.contract.ProcessInstanceResponse;
import org.egov.wcms.web.contract.RequestInfoWrapper;
import org.egov.wcms.web.contract.Task;
import org.egov.wcms.web.contract.TaskRequest;
import org.egov.wcms.web.contract.TaskResponse;
import org.egov.wcms.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WorkflowRepository {

    public static final Logger LOGGER = LoggerFactory.getLogger(WorkflowRepository.class);

    public static final String ACTION = "Forward";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WaterTransactionProducer waterTransactionProducer;

    @Autowired
    private ConfigurationManager configurationManager;

    public ProcessInstance startWorkflow(final WaterConnectionReq waterConnectionRequest) {

        final ProcessInstanceRequest processInstanceRequest = getProcessInstanceRequest(waterConnectionRequest);
        final String url = configurationManager.getWorkflowServiceHostName()
                + configurationManager.getWorkflowServiceStartPath();
        LOGGER.info("URL Obtained for Workflow Application is : " + url);
        LOGGER.info("Prepared processInstanceRequest is : " + processInstanceRequest);
        ProcessInstanceResponse processInstanceResponse = null;
        try {
            processInstanceResponse = restTemplate.postForObject(url, processInstanceRequest,
                    ProcessInstanceResponse.class);
        } catch (final Exception e) {
            LOGGER.info("Description of the Encountered Exception: " + e);
            throw e;
        }
        LOGGER.info("Reponse Object obtained from Workflow : " + processInstanceResponse.getProcessInstance().getId());

        return processInstanceResponse.getProcessInstance();
    }

    public TaskResponse updateWorkflow(final WaterConnectionReq waterConnectionRequest) {

        final TaskRequest taskRequest = getTaskRequest(waterConnectionRequest);
        final Connection connection = waterConnectionRequest.getConnection();
        final String url = configurationManager.getWorkflowServiceHostName() + configurationManager.getWorkflowServiceTaskPAth()
                + "/" + connection.getConnectionStatus() + configurationManager.getWorkflowServiceUpdatePath();

        LOGGER.info("task request to update workflow ::: " + taskRequest + " ::url:: " + url);
        TaskResponse taskResponse = null;
        try {
            taskResponse = restTemplate.postForObject(url, taskRequest, TaskResponse.class);
        } catch (final Exception e) {
            LOGGER.error(e.toString());
            throw e;
        }
        LOGGER.info("the response from workflow update : " + taskResponse.getTask());
        if (connection.getWorkflowDetails() != null)
            updateAgreement(waterConnectionRequest);
        return taskResponse;

    }

    private ProcessInstanceRequest getProcessInstanceRequest(final WaterConnectionReq waterConnectionRequest) {

        final Connection connection = new Connection();
        final WorkflowDetails workFlowDetails = connection.getWorkflowDetails();
        final RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
        final Position assignee = new Position();
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();

        assignee.setId(workFlowDetails.getAssignee());
        processInstance.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
        processInstance.setType(configurationManager.getWorkflowServiceBusinessKey());
        processInstance.setAssignee(assignee);
        processInstance.setComments(workFlowDetails.getComments());
        processInstance.setInitiatorPosition(workFlowDetails.getInitiatorPosition());
        processInstance.setTenantId(connection.getTenantId());
        processInstance.setDetails("Acknowledgement Number : " + connection.getAcknowledgementNumber());
        processInstanceRequest.setProcessInstance(processInstance);
        processInstanceRequest.setRequestInfo(requestInfo);

        return processInstanceRequest;
    }

    public void saveConnectionRequest(final WaterConnectionReq waterConnectionRequest, final String stateId) {

        waterConnectionRequest.getConnection().setConnectionStatus(stateId);
        String wcRequestMessage = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            wcRequestMessage = objectMapper.writeValueAsString(waterConnectionRequest);
        } catch (final Exception e) {
            LOGGER.info("Exception Encountered at Save Connection Request : " + e);
            throw new RuntimeException(e);
        }
        waterTransactionProducer.sendMessage(configurationManager.getKafkaSaveWaterConnectionTopic(), "key", wcRequestMessage);
    }

    public void updateAgreement(final WaterConnectionReq waterConnectionRequest) {

        String wcRequestMessage = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            wcRequestMessage = objectMapper.writeValueAsString(waterConnectionRequest);
        } catch (final Exception e) {
            LOGGER.info("WorkflowRepositoryupdateAgreement : " + e);
            throw new RuntimeException(e);
        }
        waterTransactionProducer.sendMessage(configurationManager.getKafkaUpdateWaterConnectionTopic(), "key", wcRequestMessage);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private TaskRequest getTaskRequest(final WaterConnectionReq waterConnectionRequest) {

        LOGGER.info("isnide update workflow workflow details value ::: "
                + waterConnectionRequest.getConnection().getWorkflowDetails());
        final Connection connection = waterConnectionRequest.getConnection();
        final RequestInfo requestInfo = waterConnectionRequest.getRequestInfo();
        final RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        WorkflowDetails workflowDetails = null;
        final TaskRequest taskRequest = new TaskRequest();
        final Task task = new Task();
        Position assignee = new Position();

        taskRequest.setRequestInfo(requestInfo);
        task.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
        task.setType(configurationManager.getWorkflowServiceBusinessKey());
        task.setId(connection.getConnectionStatus());

        if (null != connection.getWorkflowDetails()) {
            workflowDetails = connection.getWorkflowDetails();
            task.setAction(workflowDetails.getAction());
            task.setStatus(workflowDetails.getStatus());
            assignee.setId(workflowDetails.getAssignee());
            task.setAssignee(assignee);
        } else {
            LOGGER.info("The workflow object before collection update ::: " + connection.getWorkflowDetails());
            final String url = configurationManager.getWorkflowServiceHostName()
                    + configurationManager.getWorkflowServiceSearchPath()
                    + "?id=" + connection.getConnectionStatus()
                    + "&tenantId=" + connection.getTenantId();

            ProcessInstanceResponse processInstanceResponse = null;
            try {
                processInstanceResponse = restTemplate.postForObject(url, requestInfoWrapper,
                        ProcessInstanceResponse.class);
            } catch (final Exception e) {
                LOGGER.info("exception from search workflow call ::: " + e);
                throw e;
            }

            LOGGER.info("process instance responce ::: from search ::: " + processInstanceResponse);
            final ProcessInstance processInstance = processInstanceResponse.getProcessInstance();
            task.setAction(ACTION);
            task.setStatus(processInstance.getStatus());
            assignee = processInstance.getOwner();
            LOGGER.info("the owner object from response is ::: " + processInstance.getOwner());
            task.setAssignee(assignee);
        }
        taskRequest.setTask(task);

        return taskRequest;
    }

}
