package org.egov.web.indexer.service;

import org.egov.web.indexer.config.IndexerPropertiesManager;
import org.egov.web.indexer.contract.Boundary;
import org.egov.web.indexer.contract.BoundaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BoundaryServiceImpl implements BoundaryService {
	
	@Autowired
	private IndexerPropertiesManager propertiesManager;

	@Override
	public Boundary fetchBoundaryById(Long id) {
		String url = propertiesManager.getBoundaryServiceHostname() + "v1/location/boundarys?boundary.id={id}";
		return getBoundaryServiceResponse(url, id).getBoundaries().get(0);
	}

	private BoundaryResponse getBoundaryServiceResponse(final String url, Long id) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, BoundaryResponse.class, id);
	}

}
