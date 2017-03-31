package org.egov.pgr.write.repository;

import org.egov.pgr.write.contracts.position.PositionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionRepository {

    private RestTemplate restTemplate;
    private String url;

    @Autowired
    public PositionRepository(RestTemplate restTemplate,
                              @Value("${egov.services.eis.hostname}") String host,
                              @Value("${egov.services.eis.fetch_assignee.context}") String urlSuffix) {
        this.restTemplate = restTemplate;
        this.url = host + urlSuffix;
    }

    public Long departmentIdForAssignee(String tenantId, Long assigneeId) {
        PositionsResponse response = restTemplate
            .getForObject(url, PositionsResponse.class, tenantId, assigneeId);
        return response.getPositions().get(0).getDepartmentDesignation().getDepartment().getId();
    }

}
