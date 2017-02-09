package org.egov.pgr.services;

import org.egov.pgr.model.RequestInfo;
import org.egov.pgr.transform.CrossHierarchyResponse;
import org.egov.pgr.transform.CrossHierarchyServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrossHierarchyServiceImpl implements CrossHierarchyService {

	@Value("${egov.services.boundary.host}")
	private String crossHierarchyServiceHost;

	@Override
	public CrossHierarchyResponse fetchCrossHierarchyById(RequestInfo requestInfo, String crossHierarchyId) {
		String url = crossHierarchyServiceHost + "v1/location/crosshierarchys?crossHierarchy.id={crossHierarchyId}";
		return getCrossHierarchyServiceResponse(url, "", "", crossHierarchyId).getCrossHierarchys().get(0);
	}

	private CrossHierarchyServiceResponse getCrossHierarchyServiceResponse(final String url, String... args) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, CrossHierarchyServiceResponse.class, args);
	}

}
