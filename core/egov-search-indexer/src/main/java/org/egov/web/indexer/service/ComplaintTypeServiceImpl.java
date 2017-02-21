package org.egov.web.indexer.service;

import org.egov.web.indexer.config.IndexerPropertiesManager;
import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	@Autowired
	private IndexerPropertiesManager propertiesManager;

	@Override
	public ComplaintType fetchComplaintTypeByCode(String code) {
		String url = propertiesManager.getComplaintTypeServiceHostname()
				+ "pgr/services/{serviceCode}?tenantId={tenatId}";
		return getComplaintTypeServiceResponse(url, code).getComplaintType(); 
	}

	private ComplaintTypeResponse getComplaintTypeServiceResponse(final String url, String code) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ComplaintTypeResponse.class, code, "");
	}

}