package org.egov.works.estimate.persistence.repository;

import org.egov.works.estimate.web.contract.AssetResponse;
import org.egov.works.estimate.web.contract.Boundary;
import org.egov.works.estimate.web.contract.BoundaryResponse;
import org.egov.works.estimate.web.contract.FileStoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BoundaryRepository {

    @Autowired
    private RestTemplate restTemplate;

    private String searchBoundaryUrl;

    public BoundaryRepository(final RestTemplate restTemplate, @Value("${egov.location.service.host}") final String boundaryHost,
                              @Value("${egov.location.searchboundaryurl}") final String searchBoundaryUrl) {
        this.restTemplate = restTemplate;
        this.searchBoundaryUrl = boundaryHost + searchBoundaryUrl;
    }

    public List<Boundary> searchBoundaries(final String tenantId, final String code) {
        String codes = String.join(",", code);
        return restTemplate.postForObject(searchBoundaryUrl, null, BoundaryResponse.class, codes, tenantId).getBoundarys();
    }
}
