package org.egov.workflow.service;

import org.egov.workflow.model.BoundaryResponse;
import org.egov.workflow.model.BoundaryServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {

	@Value("${egov.services.boundary_service.host}")
	private String boundaryServiceHost;

	@Override
	public BoundaryResponse fetchBoundaryById(Long id) {
		String url = boundaryServiceHost + "v1/location/boundarys?boundary.id={id}";
		return getBoundaryServiceResponse(url, id).getBoundary().get(0);
	}

	private BoundaryServiceResponse getBoundaryServiceResponse(final String url, Long id) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, BoundaryServiceResponse.class, id);
	}

}
