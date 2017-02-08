package org.egov.workflow.domain.service;

public interface WorkflowInterface {

    Object getAssignee(Long locationId, String complaintTypeId, Long assigneeId);

}