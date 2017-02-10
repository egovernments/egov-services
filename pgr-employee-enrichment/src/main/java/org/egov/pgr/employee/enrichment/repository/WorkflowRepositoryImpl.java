package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.config.PropertiesManager;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowRequest;
import org.egov.pgr.employee.enrichment.repository.contract.WorkflowResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowRepositoryImpl implements WorkflowRepository {

    private RestTemplate restTemplate;
    private PropertiesManager propertiesManager;

    @Autowired
    public WorkflowRepositoryImpl(RestTemplate restTemplate, PropertiesManager propertiesManager) {
        this.restTemplate = restTemplate;
        this.propertiesManager = propertiesManager;
    }

    @Override
    public WorkflowResponse triggerWorkflow(WorkflowRequest workflowRequest) {
        String url = propertiesManager.getCreateWorkflowUrl();
        return restTemplate.postForObject(url, workflowRequest, WorkflowResponse.class);
    }

}
