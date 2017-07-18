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

package org.egov.wcms.transanction.workflow.service;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.wcms.transanction.config.ConfigurationManager;
import org.egov.wcms.transanction.model.UserInfo;
import org.egov.wcms.transanction.model.WorkflowDetails;
import org.egov.wcms.transanction.model.enums.NewConnectionStatus;
import org.egov.wcms.transanction.request.WorkFlowRequestInfo;
import org.egov.wcms.transanction.web.contract.Position;
import org.egov.wcms.transanction.web.contract.ProcessInstance;
import org.egov.wcms.transanction.web.contract.ProcessInstanceRequest;
import org.egov.wcms.transanction.web.contract.ProcessInstanceResponse;
import org.egov.wcms.transanction.web.contract.Task;
import org.egov.wcms.transanction.web.contract.TaskRequest;
import org.egov.wcms.transanction.web.contract.TaskResponse;
import org.egov.wcms.transanction.web.contract.WaterConnectionReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransanctionWorkFlowService {
    public static final Logger LOGGER = LoggerFactory.getLogger(TransanctionWorkFlowService.class);

    @Autowired
    private ConfigurationManager configurationManager;

    public String startWorkFlowURL() {
        return (configurationManager.getCommonworkflowservicebasepath()
                + configurationManager.getWorkflowServiceStartPath()).toString();
    }

    public String updateWorkFlowURL(final Long id) {
        return (configurationManager.getCommonworkflowservicebasepath()
                + configurationManager.getWorkflowServiceUpdatePath().replaceAll("\\{id\\}", id.toString())).toString();
    }

    public ProcessInstance startWorkFlow(final WaterConnectionReq waterConnectionRequest) {
        final WorkflowDetails workflowdet = waterConnectionRequest.getConnection().getWorkflowDetails();
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        final ProcessInstance processInstance = new ProcessInstance();
        processInstance.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
        processInstance.setType(configurationManager.getWorkflowServiceBusinessKey());
        processInstance.setComments(workflowdet.getComments());
        processInstance.setTenantId(waterConnectionRequest.getConnection().getTenantId());
        final Position assignee = new Position();
        LOGGER.info("waterConnectionRequest workflow start:" + waterConnectionRequest);
        assignee.setId(workflowdet.getAssignee());
        processInstance.setInitiatorPosition(workflowdet.getInitiatorPosition());
        processInstance.setAssignee(assignee);
        final RequestInfo requestin = waterConnectionRequest.getRequestInfo();
        final WorkFlowRequestInfo req = new WorkFlowRequestInfo();
        req.setApiId(requestin.getApiId());
        req.setDid(requestin.getDid());
        req.setKey(requestin.getKey());
        req.setAuthToken(requestin.getAuthToken());
        req.setMsgId(requestin.getMsgId());
        req.setAction(requestin.getAction());
        req.setTenantId(waterConnectionRequest.getConnection().getTenantId());
        final User newuser = requestin.getUserInfo();
        final UserInfo workflowUser = new UserInfo();
        workflowUser.setId(newuser.getId());
        workflowUser.setUserName(newuser.getUserName());
        req.setUserInfo(workflowUser);
        req.setVer(requestin.getVer());
        req.setTs(new Date());
       
        processInstanceRequest.setRequestInfo(req);
        processInstanceRequest.setProcessInstance(processInstance);
        final String url = startWorkFlowURL();

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<ProcessInstanceRequest> request = new HttpEntity<>(processInstanceRequest);

        final ProcessInstanceResponse processInstanceResponse = restTemplate.postForObject(url, request,
                ProcessInstanceResponse.class);

        return processInstanceResponse.getProcessInstance();
    }

    public Task updateWorkFlow(final WaterConnectionReq waterConnectionRequest) {
        final TaskRequest taskRequest = new TaskRequest();
        final Task task = new Task();
        final String workFlowAction = waterConnectionRequest.getConnection().getWorkflowDetails().getAction();
        task.setId(waterConnectionRequest.getConnection().getStateId().toString());
        task.setBusinessKey(configurationManager.getWorkflowServiceBusinessKey());
        task.setType(configurationManager.getWorkflowServiceBusinessKey());
        task.setComments("updating workflow waterconnection");
        task.setAction(workFlowAction);
        if ("Approve".equalsIgnoreCase(workFlowAction))
            task.setStatus(NewConnectionStatus.APPROVED.toString());
        else if ("Reject".equalsIgnoreCase(workFlowAction))
            task.setStatus(NewConnectionStatus.REJECTED.toString());
        else if ("Verify".equalsIgnoreCase(workFlowAction))
            task.setStatus(NewConnectionStatus.VERIFIED.toString());
        task.setTenantId(waterConnectionRequest.getConnection().getTenantId());
        final Position assignee = new Position();
        assignee.setId(waterConnectionRequest.getConnection().getWorkflowDetails().getAssignee());
        task.setAssignee(assignee);
        final RequestInfo requestin = waterConnectionRequest.getRequestInfo();
        final WorkFlowRequestInfo req = new WorkFlowRequestInfo();
        req.setApiId(requestin.getApiId());
        req.setDid(requestin.getDid());
        req.setKey(requestin.getKey());
        req.setAuthToken(requestin.getAuthToken());
        req.setMsgId(requestin.getMsgId());
        req.setAction(requestin.getAction());
        req.setTenantId(waterConnectionRequest.getConnection().getTenantId());
        final User newuser = requestin.getUserInfo();
        final UserInfo workflowUser = new UserInfo();
        workflowUser.setId(newuser.getId());
        workflowUser.setUserName(newuser.getUserName());
        req.setUserInfo(workflowUser);
        req.setVer(requestin.getVer());
        req.setTs(new Date());
        taskRequest.setRequestInfo(req);
        taskRequest.setTask(task);
        final String url = updateWorkFlowURL(waterConnectionRequest.getConnection().getStateId());

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest);

        final TaskResponse taskResponse = restTemplate.postForObject(url, request, TaskResponse.class);

        return taskResponse.getTask();
    }

}