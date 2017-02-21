package org.egov.pgr.repository;

import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.position.PositionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private RestTemplate restTemplate;
    private PersistenceProperties persistenceProperties;

    @Autowired
    public PositionRepository(RestTemplate restTemplate, PersistenceProperties persistenceProperties) {
        this.restTemplate = restTemplate;
        this.persistenceProperties = persistenceProperties;
    }

    public Long designationIdForAssignee(String tenantId, Long assigneeId) {
        String positionServiceEndpoint = this.persistenceProperties.getPositionServiceEndpoint();
        PositionsResponse response = restTemplate.getForObject(positionServiceEndpoint, PositionsResponse.class, tenantId, assigneeId);
        return response.getPositions().get(0).getDepartmentDesignation().getDesignation().getId();
    }

}
