package org.egov.web.indexer.service;

import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeServiceImpl implements ComplaintTypeService {

	private final String complaintTypeServiceHostname;
	private RestTemplate restTemplate;

	public ComplaintTypeServiceImpl(RestTemplate restTemplate,
			@Value("${egov.services.pgrrest.host}") String complaintTypeServiceHostname) {
		this.restTemplate = restTemplate;
		this.complaintTypeServiceHostname = complaintTypeServiceHostname;
	}

	@Override
	public ComplaintType fetchComplaintTypeByCode(String code) {
		String url = this.complaintTypeServiceHostname + "pgr/services/{serviceCode}?tenantId={tenatId}";
		return getComplaintTypeServiceResponse(url, code).getComplaintType();
	}

	private ComplaintTypeResponse getComplaintTypeServiceResponse(final String url, String code) {
		return restTemplate.getForObject(url, ComplaintTypeResponse.class, code, "");
	}

}