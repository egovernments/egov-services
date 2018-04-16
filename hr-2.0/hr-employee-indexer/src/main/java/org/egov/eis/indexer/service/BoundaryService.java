package org.egov.eis.indexer.service;

import lombok.extern.slf4j.Slf4j;
import org.egov.boundary.web.contract.Boundary;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.eis.indexer.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class BoundaryService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public Boundary getBoundary(Long id, String tenantId) {
		BoundaryResponse boundaryResponse;
		try {
			URI url = new URI(propertiesManager.getEgovLocationServiceHost()
					+ propertiesManager.getEgovLocationServiceBasepath()
					+ propertiesManager.getEgovLocationServiceBoundarySearchPath()
					+ "?Boundary.tenantId=" + tenantId + "&Boundary.id=" + id);
			log.info(url.toString());
			boundaryResponse = restTemplate.getForObject(url, BoundaryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return isEmpty(boundaryResponse.getBoundarys()) ? null : boundaryResponse.getBoundarys().get(0);
	}
}
