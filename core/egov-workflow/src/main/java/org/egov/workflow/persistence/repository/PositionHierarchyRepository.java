package org.egov.workflow.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
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
                                       @Value("${egov.services.hrmasters.host}") final String hrMastersServiceHostname,
                                       @Value("${egov.services.hr_employee.positionhierarchys}") final String url) {
        this.restTemplate = restTemplate;
        this.positionHierarchyUrl = hrMastersServiceHostname + url;
    }

    public PositionHierarchyResponse getByObjectTypeObjectSubTypeAndFromPosition(final String objectType,
                                                                                 final String objectSubType, final Long fromPosition, final String tenantId) {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().RequestInfo(RequestInfo.builder().build()).build();
        return restTemplate.postForObject(positionHierarchyUrl, wrapper, PositionHierarchyResponse.class, fromPosition, objectType, objectSubType, tenantId);
    }

}
