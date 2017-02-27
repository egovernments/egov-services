package org.egov.pgr.repository;

import org.egov.pgr.contract.CrossHierarchyResponse;
import org.egov.pgr.contract.CrossHierarchyServiceResponse;
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

    public CrossHierarchyResponse getCrossHierarchy(String crossHierarchyId) {
        final CrossHierarchyServiceResponse serviceResponse =
                restTemplate.getForObject(this.url, CrossHierarchyServiceResponse.class, crossHierarchyId);
        return serviceResponse.getCrossHierarchy();
    }

}
