package org.egov.eis.indexer.service;

import java.net.URI;
import java.util.List;

import org.egov.boundary.persistence.entity.Boundary;
import org.egov.boundary.web.contract.BoundaryResponse;
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
	
	public static final Logger logger = LoggerFactory.getLogger(BoundaryService.class);
	
	public static final String BOUNDARY_SEARCH_BY_ID_QUERY_PARAM = "Boundary.id";
	
	public List<Boundary> getBoundary(String ids, RequestInfo requestInfo) {
		URI url = null;
		BoundaryResponse boundaryResponse = null;
		try { //http://egov-micro-dev.egovernments.org/user/_search
			// url = new URI(propertiesManager.getBoundaryServiceHostName() + "?id=" + id);
			url = new URI(propertiesManager.getBoundaryServiceSearchUri() + propertiesManager.getBoundarySearchContextPath() +"?"+ BOUNDARY_SEARCH_BY_ID_QUERY_PARAM + "=" + ids);
			logger.info(url.toString());
			boundaryResponse = restTemplate.getForObject(url, BoundaryResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return boundaryResponse.getBoundarys();
	}
	

	

}
