package org.egov.swm.web.repository;

import org.egov.swm.domain.model.Boundary;
import org.egov.swm.web.contract.BoundaryResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {
    private final String boundaryServiceHost;
    private final RestTemplate restTemplate;

    public BoundaryRepository(final RestTemplate restTemplate,
            @Value("${egov.services.boundary.host}") final String boundaryServiceHost,
            @Value("${egov.services.boundary.searchpath}") final String boundaryServicePath) {
        this.restTemplate = restTemplate;
        this.boundaryServiceHost = boundaryServiceHost.concat(boundaryServicePath);
    }

    public Boundary fetchBoundaryByCode(final String code, final String tenantId) {
        try {
            return getBoundaryServiceResponse(boundaryServiceHost, code, tenantId).getBoundaries().get(0);
        } catch (final Exception e) {
            throw new CustomException("Location", "Given Location is invalid: " + code);
        }
    }

    private BoundaryResponse getBoundaryServiceResponse(final String url, final String code, final String tenantId) {
        try {
            return restTemplate.getForObject(url, BoundaryResponse.class, code, tenantId);
        } catch (final Exception e) {
            throw new CustomException("Location", "Given Location is invalid: " + code);
        }
    }

}
