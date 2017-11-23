package org.egov.swm.web.repository;

import java.util.List;

import org.egov.swm.domain.model.Boundary;
import org.egov.swm.web.contract.BoundaryResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {
    private final String boundaryByCodePath;
    private final String boundaryByCodesPath;
    private final RestTemplate restTemplate;

    public BoundaryRepository(final RestTemplate restTemplate,
            @Value("${egov.services.boundary.host}") final String boundaryServiceHost,
            @Value("${egov.services.boundary.by.code.path}") final String boundaryByCodePath,
            @Value("${egov.services.boundary.by.codes.path}") final String boundaryByCodesPath) {
        this.restTemplate = restTemplate;
        this.boundaryByCodePath = boundaryServiceHost.concat(boundaryByCodePath);
        this.boundaryByCodesPath = boundaryServiceHost.concat(boundaryByCodesPath);
    }

    public Boundary fetchBoundaryByCode(final String code, final String tenantId) {
        try {
            return getBoundaryServiceResponse(boundaryByCodePath, code, tenantId).getBoundaries().get(0);
        } catch (final Exception e) {
            throw new CustomException("Location", "Given Location is invalid: " + code);
        }
    }

    public List<Boundary> fetchBoundaryByCodes(final String codes, final String tenantId) {
        try {
            return getBoundaryServiceResponse(boundaryByCodesPath, codes, tenantId).getBoundaries();
        } catch (final Exception e) {
            throw new CustomException("Location", "Given Location is invalid: " + codes);
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
