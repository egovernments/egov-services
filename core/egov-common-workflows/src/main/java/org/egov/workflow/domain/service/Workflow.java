package org.egov.workflow.domain.service;

import java.util.List;

import org.egov.workflow.web.contract.Designation;
import org.egov.workflow.web.contract.ProcessInstance;
import org.egov.workflow.web.contract.ProcessInstanceRequest;
import org.egov.workflow.web.contract.ProcessInstanceResponse;
import org.egov.workflow.web.contract.RequestInfo;
import org.egov.workflow.web.contract.Task;
import org.egov.workflow.web.contract.TaskRequest;
import org.egov.workflow.web.contract.TaskResponse;

public interface Workflow {

    ProcessInstanceResponse start(ProcessInstanceRequest processInstanceRequest);

    ProcessInstance end(String jurisdiction, ProcessInstance processInstance);

     ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance,final RequestInfo requestInfo);

     TaskResponse getTasks(TaskRequest taskRequest);

     ProcessInstance update(String jurisdiction, ProcessInstance processInstance);

    TaskResponse update(TaskRequest taskRequest);

    List<Task> getHistoryDetail(String tenantId,String workflowId);

    List<Designation> getDesignations(Task t, String departmentName);

    // List<Object> getAssignee(String deptCode, String designationName);

    Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId,RequestInfo requestInfo);
}