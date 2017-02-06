package org.egov.pgr.employee.enrichment.service;

public interface EmployeePopulateService {

    Long fetchEmployeeAssignmentByLocation(Long location, String complaintTypeCode);

}
