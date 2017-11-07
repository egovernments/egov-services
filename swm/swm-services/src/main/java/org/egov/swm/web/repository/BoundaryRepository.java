package org.egov.swm.web.repository;

import org.egov.swm.domain.model.Boundary;
import org.egov.swm.web.contract.BoundaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {
	private final String boundaryServiceHost;
	private RestTemplate restTemplate;

	public BoundaryRepository(RestTemplate restTemplate,
			@Value("${egov.services.boundary.host}") String boundaryServiceHost,
			@Value("${egov.services.boundary.searchpath}") String boundaryServicePath) {
		this.restTemplate = restTemplate;
		this.boundaryServiceHost = boundaryServiceHost.concat(boundaryServicePath);
	}

	public Boundary fetchBoundaryByCode(String code, String tenantId) {
		return getBoundaryServiceResponse(this.boundaryServiceHost, code, tenantId).getBoundaries().get(0);
	}

	private BoundaryResponse getBoundaryServiceResponse(final String url, String code, String tenantId) {
		return restTemplate.getForObject(url, BoundaryResponse.class, code, tenantId);
	}

}
