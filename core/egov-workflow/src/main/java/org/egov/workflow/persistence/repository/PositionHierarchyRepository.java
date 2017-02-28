package org.egov.workflow.persistence.repository;

import java.util.List;

import org.egov.workflow.web.contract.PositionHierarchyResponse;
import org.egov.workflow.web.contract.PositionHierarchyServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PositionHierarchyRepository {

    private final RestTemplate restTemplate;
    private final String url;

    public PositionHierarchyRepository(final RestTemplate restTemplate,
            @Value("${egov.services.eis.host}") final String eisServiceHostname,
            @Value("${egov.services.eis.positionhierarchys}") final String url) {
        this.restTemplate = restTemplate;
        this.url = eisServiceHostname + url;
    }

    public List<PositionHierarchyResponse> getByObjectTypeObjectSubTypeAndFromPosition(final String objectType,
            final String objectSubType, final Long fromPositionid) {
        final PositionHierarchyServiceResponse positionHierarchy = restTemplate.getForObject(url,
                PositionHierarchyServiceResponse.class, objectType, objectSubType,
                fromPositionid);
        return positionHierarchy.getPositionHierarchy();
    }

}
