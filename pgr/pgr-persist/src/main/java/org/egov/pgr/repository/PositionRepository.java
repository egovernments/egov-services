package org.egov.pgr.repository;

import org.egov.pgr.config.PersistenceProperties;
import org.egov.pgr.contracts.position.PositionsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private RestTemplate restTemplate;
    private PersistenceProperties persistenceProperties;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PositionRepository(RestTemplate restTemplate, PersistenceProperties persistenceProperties) {
        this.restTemplate = restTemplate;
        this.persistenceProperties = persistenceProperties;
    }

    public Long designationIdForAssignee(String tenantId, Long assigneeId) {
        try {
            String positionServiceEndpoint = this.persistenceProperties.getPositionServiceEndpoint();
            PositionsResponse response = restTemplate.getForObject(positionServiceEndpoint, PositionsResponse.class,
                    tenantId, assigneeId);
            return response.getPositions().get(0).getDepartmentDesignation().getDesignation().getId();
        } catch (RestClientException rce) {
            logger.error(rce.getMessage(), rce);
        }
        return null;
    }

    public Long departmentIdForAssignee(String tenantId, Long assigneeId) {
        try {
            String positionServiceEndpoint = this.persistenceProperties.getPositionServiceEndpoint();
            PositionsResponse response = restTemplate.getForObject(positionServiceEndpoint, PositionsResponse.class,
                    tenantId, assigneeId);
            return response.getPositions().get(0).getDepartmentDesignation().getDepartment().getId();
        } catch (RestClientException rce) {
            logger.error(rce.getMessage(), rce);
        }
        return null;
    }

}
