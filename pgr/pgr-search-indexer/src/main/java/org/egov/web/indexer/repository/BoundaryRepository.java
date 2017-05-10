package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.BoundaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {
    private final String boundaryServiceHost;
    private RestTemplate restTemplate;

    public BoundaryRepository(RestTemplate restTemplate,
                              @Value("${egov.services.boundary.host}") String boundaryServiceHost) {
        this.restTemplate = restTemplate;
        this.boundaryServiceHost = boundaryServiceHost;
    }

    public Boundary fetchBoundaryById(Long id, String tenantId) {
        String url = this.boundaryServiceHost + "egov-location/boundarys?boundary.id={id}&boundary.tenantId={tenantId}";
        return getBoundaryServiceResponse(url, id, tenantId).getBoundaries().get(0);
    }

    private BoundaryResponse getBoundaryServiceResponse(final String url, Long id, String tenantId) {
        return restTemplate.getForObject(url, BoundaryResponse.class, id, tenantId);
    }

}
