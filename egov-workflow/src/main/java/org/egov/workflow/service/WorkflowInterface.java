package org.egov.workflow.service;

public interface WorkflowInterface {

    Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId);

}