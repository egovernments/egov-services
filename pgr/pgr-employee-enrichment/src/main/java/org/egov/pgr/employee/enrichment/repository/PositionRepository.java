package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.PositionsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private final String url;
    private RestTemplate restTemplate;

    public PositionRepository(RestTemplate restTemplate,
                              @Value("${egov.services.position.host}") String host,
                              @Value("${egov.services.position.designation}") String endpointUrl) {
        this.url = host + endpointUrl;
        this.restTemplate = restTemplate;
    }

    public String getDesignationIdForAssignee(String tenantId, long assigneeId) {
        PositionsResponse response = restTemplate
            .getForObject(this.url, PositionsResponse.class, tenantId, assigneeId);
        return response.getDesignationId();
    }

}


