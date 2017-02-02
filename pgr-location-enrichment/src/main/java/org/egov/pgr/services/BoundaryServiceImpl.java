package org.egov.pgr.services;

import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.transform.BoundaryServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {

	@Value("${egov.services.boundary_service.host}")
	private String boundaryServiceHost;

	@Value("${egov.services.crosshierarchy_service.host}")
	private String crossHierarchyServiceHost;

	@Override
	public Long fetchBoundaryByLatLng(RequestInfo requestInfo, Double lat, Double lng) {
		String url = boundaryServiceHost
				+ "v1/location/boundarys?boundary.latitude={latitude}&boundary.longitude={longitude}";
		return getBoundaryServiceResponse(url, String.valueOf(lat), String.valueOf(lng), "").getBoundary().get(0)
				.getId();
	}

	private BoundaryServiceResponse getBoundaryServiceResponse(final String url, String... args) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, BoundaryServiceResponse.class, args);
	}

}
