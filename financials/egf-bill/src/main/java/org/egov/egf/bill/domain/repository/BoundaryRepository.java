package org.egov.egf.bill.domain.repository;

import org.egov.egf.bill.web.contract.Boundary;
import org.egov.egf.bill.web.contract.BoundaryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryRepository {
	private final String boundaryServiceHost;
	private final RestTemplate restTemplate;

	public BoundaryRepository(final RestTemplate restTemplate,
			@Value("${egov.services.boundary.host}") final String boundaryServiceHost) {
		this.restTemplate = restTemplate;
		this.boundaryServiceHost = boundaryServiceHost;
	}

	public Boundary getBoundaryById(final String id, final String tenantId) {
		final String url = boundaryServiceHost
				+ "egov-location/boundarys?boundary.id={id}&boundary.tenantId={tenantId}";
		return getBoundaryServiceResponse(url, id, tenantId).getBoundaries().get(0);
	}

	private BoundaryResponse getBoundaryServiceResponse(final String url, final String id, final String tenantId) {
		return restTemplate.getForObject(url, BoundaryResponse.class, id, tenantId);
	}

	public Boundary findById(Boundary division) {
		return getBoundaryById(division.getId(),division.getTenantId());
	}

}
