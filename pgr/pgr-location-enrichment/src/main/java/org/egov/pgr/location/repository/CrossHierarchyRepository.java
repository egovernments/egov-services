package org.egov.pgr.location.repository;

import org.egov.pgr.location.contract.CrossHierarchyResponse;
import org.egov.pgr.location.contract.CrossHierarchySearchRequest;
import org.egov.pgr.location.contract.CrossHierarchyServiceResponse;
import org.egov.pgr.location.contract.CrossHierarchy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrossHierarchyRepository {

    private final String url;
    private RestTemplate restTemplate;

    public CrossHierarchyRepository(@Value("${egov.services.boundary.host}")
                                        String crossHierarchyServiceHost,
                                    @Value("${egov.services.boundary.context.fetch_by_hierarchy_id}")
                                        String crossHierarchyUrl,
                                    RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.url = crossHierarchyServiceHost + crossHierarchyUrl;
    }

    public CrossHierarchyResponse getCrossHierarchy(String crossHierarchyId, String tenantId) {
        CrossHierarchySearchRequest crossHierarchySearchRequest = getCrossHierarchySearchRequest(crossHierarchyId, tenantId);
        CrossHierarchyServiceResponse crossHierarchyServiceResponse = restTemplate.postForObject(url, crossHierarchySearchRequest, CrossHierarchyServiceResponse.class);
        return crossHierarchyServiceResponse.getCrossHierarchy();
    }

    private CrossHierarchySearchRequest getCrossHierarchySearchRequest(String crossHierarchyId, String tenantId) {
        CrossHierarchy crossHierarchy = CrossHierarchy.builder()
            .id(Long.valueOf(crossHierarchyId))
            .tenantId(tenantId)
            .build();

        return CrossHierarchySearchRequest.builder()
            .requestInfo(null)
            .crossHierarchy(crossHierarchy).build();
    }

}
