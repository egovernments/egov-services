package org.egov.pgr.employee.enrichment.service;

import org.egov.pgr.employee.enrichment.model.SevaRequest;
import org.egov.pgr.employee.enrichment.repository.WorkflowRepository;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkflowService {

    private WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public SevaRequest enrichWorkflow(SevaRequest sevaRequest) {
        WorkflowRequest request = sevaRequest.getWorkFlowRequest();
        WorkflowResponse workflowResponse = null;
        String status = request.getStatus();
        if (status.toUpperCase().equals("REGISTERED")) {
            workflowResponse = workflowRepository.create(request);
        } else if (status.equalsIgnoreCase("COMPLETED") || status.equalsIgnoreCase("WITHDRAWN") || status.equalsIgnoreCase("REJECTED")) {
            workflowResponse = workflowRepository.close(request);
        }
        sevaRequest.update(workflowResponse);
        return sevaRequest;
    }

}
