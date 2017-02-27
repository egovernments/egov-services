package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepository {

    private RestTemplate restTemplate;
    private String createWorkflowUrl;
    private String closeWorkflowUrl;

    @Autowired
    public WorkflowRepository(@Value("${egov.services.workflow.hostname}") String workflowHostname,
                              @Value("${egov.services.workflow.create_workflow}") String createPath,
                              @Value("${egov.services.workflow.close_workflow}") String closePath,
                              RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.createWorkflowUrl = workflowHostname + createPath;
        this.closeWorkflowUrl = workflowHostname + closePath;
    }

    public WorkflowResponse create(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(createWorkflowUrl, workflowRequest, WorkflowResponse.class);
    }

    public WorkflowResponse close(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(closeWorkflowUrl, workflowRequest, WorkflowResponse.class);
    }
}
