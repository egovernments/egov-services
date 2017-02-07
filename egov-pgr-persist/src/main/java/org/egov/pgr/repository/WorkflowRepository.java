package org.egov.pgr.repository;

import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.workflow.CreatWorkflowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class WorkflowRepository {

    private PersistenceProperties persistenceProperties;
    private RestTemplate restTemplate;

    @Autowired
    public WorkflowRepository(PersistenceProperties persistenceProperties, RestTemplate restTemplate) {
        this.persistenceProperties = persistenceProperties;
        this.restTemplate = restTemplate;
    }

    public void startWorkFlow(CreatWorkflowRequest creatWorkflowRequest) {
        String createWorkflowUrl = persistenceProperties.createWorkflowEndpoint();
        restTemplate.postForObject(createWorkflowUrl, creatWorkflowRequest, HashMap.class);
    }

}
