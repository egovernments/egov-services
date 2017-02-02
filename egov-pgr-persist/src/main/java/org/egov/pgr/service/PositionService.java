package org.egov.pgr.service;

import org.egov.pgr.contracts.position.PositionsResponse;
import org.springframework.web.client.RestTemplate;

public class PositionService {

    public Long designationIdForAssignee(String tenantId, Long assigneeId) {
        RestTemplate restTemplate = new RestTemplate();
        String designationApiHost = "http://localhost:8081/";
        String designationApiAddress = "eis/position?tenantId={tenant_id}&id={assignee}";
        PositionsResponse response = restTemplate.getForObject(designationApiHost + designationApiAddress, PositionsResponse.class, tenantId, assigneeId);
        return response.getPositions().get(0).getDepartmentDesignation().getDesignation().getId();
    }

}
