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

package org.egov.eis.service;

import org.egov.eis.config.PropertiesManager;
import org.egov.eis.service.helper.WorkFlowSearchURLHelper;
import org.egov.eis.web.contract.LeaveApplicationSingleRequest;
import org.egov.eis.web.contract.Position;
import org.egov.eis.web.contract.ProcessInstance;
import org.egov.eis.web.contract.ProcessInstanceRequest;
import org.egov.eis.web.contract.ProcessInstanceResponse;
import org.egov.eis.web.contract.Task;
import org.egov.eis.web.contract.TaskRequest;
import org.egov.eis.web.contract.TaskResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkFlowService {
    public static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowService.class);
    
    @Autowired
    private WorkFlowSearchURLHelper workFlowSearchURLHelper;
    
    @Autowired
    private PropertiesManager propertiesManager;

    public ProcessInstance start(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final ProcessInstanceRequest processInstanceRequest = new ProcessInstanceRequest();
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
        processInstance.setType(propertiesManager.getWorkflowServiceBusinessKey());
        processInstance.setComments("starting workflow from Leave Application consumer");
        Position assignee = new Position();
        LOGGER.info("leaveApplicationRequest::" + leaveApplicationRequest);
        assignee.setId(leaveApplicationRequest.getLeaveApplication().getWorkFlowDetails().getAssignee());
        processInstance.setAssignee(assignee);
        processInstanceRequest.setRequestInfo(leaveApplicationRequest.getRequestInfo());
        processInstanceRequest.setProcessInstance(processInstance);
        final String url = workFlowSearchURLHelper.startURL();

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<ProcessInstanceRequest> request = new HttpEntity<>(processInstanceRequest);

        final ProcessInstanceResponse processInstanceResponse = restTemplate.postForObject(url, request, ProcessInstanceResponse.class);

        return processInstanceResponse.getProcessInstance();
    }
    
    public Task update(final LeaveApplicationSingleRequest leaveApplicationRequest) {
        final TaskRequest taskRequest = new TaskRequest();
        Task task = new Task();
        task.setId(leaveApplicationRequest.getLeaveApplication().getStateId().toString());
        task.setBusinessKey(propertiesManager.getWorkflowServiceBusinessKey());
        task.setType(propertiesManager.getWorkflowServiceBusinessKey());
        task.setComments("updating workflow from Leave Application consumer");
        task.setAction(leaveApplicationRequest.getLeaveApplication().getWorkFlowDetails().getAction());
        task.setStatus(leaveApplicationRequest.getLeaveApplication().getWorkFlowDetails().getAction());
        Position assignee = new Position();
        assignee.setId(leaveApplicationRequest.getLeaveApplication().getWorkFlowDetails().getAssignee());
        task.setAssignee(assignee);
        taskRequest.setRequestInfo(leaveApplicationRequest.getRequestInfo());
        taskRequest.setTask(task);
        final String url = workFlowSearchURLHelper.updateURL(leaveApplicationRequest.getLeaveApplication().getStateId());

        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        final HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest);

        final TaskResponse taskResponse = restTemplate.postForObject(url, request, TaskResponse.class);

        return taskResponse.getTask();
    }
}