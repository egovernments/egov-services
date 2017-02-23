package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.config.PropertiesManager;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepository {

    private RestTemplate restTemplate;
    private PropertiesManager propertiesManager;

    @Autowired
    public WorkflowRepository(RestTemplate restTemplate, PropertiesManager propertiesManager) {
        this.restTemplate = restTemplate;
        this.propertiesManager = propertiesManager;
    }

    public WorkflowResponse create(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(propertiesManager.workflowCreateUrl(), workflowRequest, WorkflowResponse.class);
    }

    public WorkflowResponse close(WorkflowRequest workflowRequest) {
        return restTemplate.postForObject(propertiesManager.workflowCloseUrl(), workflowRequest, WorkflowResponse.class);
    }
}
