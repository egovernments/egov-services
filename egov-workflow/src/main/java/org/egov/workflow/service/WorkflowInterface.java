package org.egov.workflow.service;

import java.util.List;

import org.egov.workflow.entity.ProcessInstance;
import org.egov.workflow.entity.Task;

public interface WorkflowInterface {

    ProcessInstance start(String jurisdiction, ProcessInstance processInstance);

    ProcessInstance getProcess(String jurisdiction, ProcessInstance processInstance);

    List<Task> getTasks(String jurisdiction, ProcessInstance processInstance);

    ProcessInstance update(String jurisdiction, ProcessInstance processInstance);

    Task update(String jurisdiction, Task task);

    List<Task> getHistoryDetail(String workflowId);

  //  List<Designation> getDesignations(Task t, String departmentCode);

    List<Object> getAssignee(String deptCode, String designationName);
    
    Object getAssignee(Long locationId, String complaintTypeId,Long assigneeId);
}