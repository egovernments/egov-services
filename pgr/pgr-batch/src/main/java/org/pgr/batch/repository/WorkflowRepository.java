package org.pgr.batch.repository;

import org.pgr.batch.repository.contract.WorkflowRequest;
import org.pgr.batch.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepository {

    private RestTemplate restTemplate;
    private String updateWorkflowUrl;

    @Autowired
    public WorkflowRepository(@Value("${egov.services.workflow.hostname}") String workflowHostname,
                              @Value("${egov.services.workflow.update_workflow}") String updatePath,
                              RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.updateWorkflowUrl = workflowHostname + updatePath;
    }
    
    public WorkflowResponse update(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(updateWorkflowUrl, workflowRequest, WorkflowResponse.class);
    }
}
