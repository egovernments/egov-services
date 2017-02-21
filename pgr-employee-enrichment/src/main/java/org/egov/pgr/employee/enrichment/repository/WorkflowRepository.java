package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepository {

    private final String url;
    private RestTemplate restTemplate;

    @Autowired
    public WorkflowRepository(RestTemplate restTemplate,
                              @Value("${egov.services.workflow.hostname}") String workflowHost,
                              @Value("${egov.services.workflow.create_workflow}") String workflowCreateUrl) {
        this.restTemplate = restTemplate;
        this.url = workflowHost + workflowCreateUrl;
    }

    public WorkflowResponse create(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(url, workflowRequest, WorkflowResponse.class);
    }

}
