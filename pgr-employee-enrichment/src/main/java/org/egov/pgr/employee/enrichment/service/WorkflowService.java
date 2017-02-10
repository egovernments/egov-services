package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.egov.pgr.employee.enrichment.consumer.contract.ServiceRequest.*;
import static org.egov.pgr.employee.enrichment.consumer.contract.SevaRequest.SERVICE_REQUEST;

@Service
public class WorkflowService {

    private WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public Map enrichWorkflowDetails(Map complaintRequestHash) {
        Map serviceRequest = (Map) complaintRequestHash.get(SERVICE_REQUEST);
        Map values = (Map) serviceRequest.get(VALUES);
        WorkflowRequest request = WorkflowRequest.fromComplaintHash(complaintRequestHash);
        WorkflowResponse workflowResponse = workflowRepository.triggerWorkflow(request);
        values.put(VALUES_ASSIGNEE_ID, workflowResponse.getAssignee());
        values.put(VALUES_STATE_ID, workflowResponse.getStateId());
        return complaintRequestHash;
    }

}
