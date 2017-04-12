package org.egov.eis.indexer.service;

import java.net.URI;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.web.contract.BoundaryResponse;
import org.egov.boundary.web.contract.BoundaryTypeResponse;
import org.egov.core.web.contract.RequestInfo;
import org.egov.eis.indexer.config.PropertiesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private RestTemplate restTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(BoundaryService.class);

	public static final String BOUNDARY_SEARCH_BY_ID_QUERY_PARAM = "Boundary.id";

	public static final String BOUNDARY_TYPE_SEARCH_BY_ID_QUERY_PARAM = "BoundaryType.id";

	public List<Boundary> getBoundary(String ids, RequestInfo requestInfo) {
		URI url = null;
		BoundaryResponse boundaryResponse = null;
		try {
			url = new URI(propertiesManager.getEgovLocationServiceHost()
					+ propertiesManager.getEgovLocationServiceBasepath()
					+ propertiesManager.getEgovLocationServiceBoundarySearchPath() + "?"
					+ BOUNDARY_SEARCH_BY_ID_QUERY_PARAM + "=" + ids);
			LOGGER.info(url.toString());
			boundaryResponse = restTemplate.getForObject(url, BoundaryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return boundaryResponse.getBoundarys();
	}

	public List<BoundaryType> getBoundaryType(String ids, RequestInfo requestInfo) {
		URI url = null;
		BoundaryTypeResponse boundaryTypeResponse = null;
		try {
			url = new URI(propertiesManager.getEgovLocationServiceHost()
					+ propertiesManager.getEgovLocationServiceBasepath()
					+ propertiesManager.getEgovLocationServiceBoundaryTypeSearchPath() + "?"
					+ BOUNDARY_TYPE_SEARCH_BY_ID_QUERY_PARAM + "=" + ids);
			LOGGER.info(url.toString());
			boundaryTypeResponse = restTemplate.getForObject(url, BoundaryTypeResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return boundaryTypeResponse.getBoundaryTypes();
	}
}
