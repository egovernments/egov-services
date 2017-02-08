package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.repository.AssigneeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AssignmentService {

    private AssigneeRepository assigneeRepository;

    @Autowired
    public AssignmentService(AssigneeRepository assigneeRepository) {
        this.assigneeRepository = assigneeRepository;
    }

    public Map enrichSevaWithAssignee(Map sevaRequestHash) {
        Map serviceRequest = (Map) sevaRequestHash.get("ServiceRequest");
        Map values = (Map) serviceRequest.get("values");
        Long boundaryId = Long.valueOf((String) values.get("location_id"));
        String complaintType = (String) serviceRequest.get("service_code");
        String assigneeId = String.valueOf(assigneeRepository.assigneeByBoundaryAndComplaintType(boundaryId, complaintType));
        values.put("assignment_id", assigneeId);
        return sevaRequestHash;
    }

}
