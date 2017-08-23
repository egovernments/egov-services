package org.egov.tl.indexer.web.repository;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.indexer.config.PropertiesManager;
import org.egov.tl.indexer.web.response.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BoundaryContractRepository {

	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManger;

	public BoundaryContractRepository(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public BoundaryResponse findByBoundaryIds(String tenantId, String ids, RequestInfoWrapper requestInfoWrapper) {

		String hostUrl = propertiesManger.getLocationServiceHostName() + propertiesManger.getLocationServiceBasePath();
		String searchUrl = propertiesManger.getLocationServiceSearchPath();
		String url = String.format("%s%s", hostUrl, searchUrl);
		StringBuffer content = new StringBuffer();
		if (ids != null) {
			content.append("boundaryIds=" + ids);
		}

		if (tenantId != null) {
			content.append("&tenantId=" + tenantId);
		}
		url = url + content.toString();
		BoundaryResponse boundaryResponse = null;
		try {

			boundaryResponse = restTemplate.postForObject(url, requestInfoWrapper, BoundaryResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the location end point");
		}

		if (boundaryResponse != null && boundaryResponse.getBoundarys() != null
				&& boundaryResponse.getBoundarys().size() > 0) {
			return boundaryResponse;
		} else {
			return null;
		}
	}
}