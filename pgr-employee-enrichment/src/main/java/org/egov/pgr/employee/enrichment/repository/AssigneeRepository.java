package org.egov.pgr.employee.enrichment.repository;

public interface AssigneeRepository {

    Long assigneeByBoundaryAndComplaintType(Long location, String complaintTypeCode);

}
