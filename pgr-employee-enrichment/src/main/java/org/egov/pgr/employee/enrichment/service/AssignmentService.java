package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.repository.AssigneeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.egov.pgr.employee.enrichment.consumer.contract.ServiceRequest.*;
import static org.egov.pgr.employee.enrichment.consumer.contract.SevaRequest.SERVICE_REQUEST;

@Service
public class AssignmentService {

    private AssigneeRepository assigneeRepository;

    @Autowired
    public AssignmentService(AssigneeRepository assigneeRepository) {
        this.assigneeRepository = assigneeRepository;
    }

    public Map enrichSevaWithAssignee(Map sevaRequestHash) {
        Map serviceRequest = (Map) sevaRequestHash.get(SERVICE_REQUEST);
        Map values = (Map) serviceRequest.get(VALUES);
        Long boundaryId = Long.valueOf((String) values.get(VALUES_LOCATION_ID));
        String complaintType = (String) serviceRequest.get(SERVICE_CODE);
        String assigneeId = String.valueOf(assigneeRepository.assigneeByBoundaryAndComplaintType(boundaryId, complaintType));
        values.put(VALUES_ASSIGNMENT_ID, assigneeId);
        return sevaRequestHash;
    }

}
