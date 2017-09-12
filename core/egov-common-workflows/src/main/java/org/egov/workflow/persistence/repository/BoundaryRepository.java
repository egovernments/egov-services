package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.BoundaryResponse;
import org.egov.workflow.web.contract.BoundaryServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {

	private final RestTemplate restTemplate;
	private final String url;

	public BoundaryRepository(final RestTemplate restTemplate,
			@Value("${egov.services.boundary.host}") final String boundaryServiceHost,
			@Value("${egov.services.location.boundaries_by_id}") final String url) {
		this.restTemplate = restTemplate;
		this.url = boundaryServiceHost + url;

	}

	public BoundaryResponse fetchBoundaryById(final Long id, final String tenantId) {
		final BoundaryServiceResponse boudaryServiceResponse = restTemplate.getForObject(url,
				BoundaryServiceResponse.class, id, tenantId);
		return boudaryServiceResponse.getBoundary().get(0);
	}

}
