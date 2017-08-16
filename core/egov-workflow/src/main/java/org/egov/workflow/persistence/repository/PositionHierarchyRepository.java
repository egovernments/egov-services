package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.PositionHierarchyResponse;
import org.egov.workflow.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionHierarchyRepository {

    private final RestTemplate restTemplate;
    private final String positionHierarchyUrl;

    public PositionHierarchyRepository(final RestTemplate restTemplate,
                                       @Value("${egov.services.pgrmaster.host}") final String hrMastersServiceHostname,
                                       @Value("${egov.services.pgrmaster.positionhierarchy}") final String url) {
        this.restTemplate = restTemplate;
        this.positionHierarchyUrl = hrMastersServiceHostname + url;
    }

    public PositionHierarchyResponse getByPositionByComplaintTypeAndFromPosition(final Long fromPosition, final String serviceCode, final String tenantId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(null).build();
        return restTemplate.postForObject(positionHierarchyUrl, wrapper, PositionHierarchyResponse.class, fromPosition, serviceCode, tenantId);
    }

}
