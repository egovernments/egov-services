package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeRepository {

	private final String complaintTypeServiceHostname;
	private RestTemplate restTemplate;

	public ComplaintTypeRepository(RestTemplate restTemplate,
			@Value("${egov.services.pgrrest.host}") String complaintTypeServiceHostname) {
		this.restTemplate = restTemplate;
		this.complaintTypeServiceHostname = complaintTypeServiceHostname;
	}

	public ComplaintType fetchComplaintTypeByCode(String code) {
		String url = this.complaintTypeServiceHostname + "pgr/services/{serviceCode}?tenantId={tenatId}";
		return getComplaintTypeServiceResponse(url, code).getComplaintType();
	}

	private ComplaintTypeResponse getComplaintTypeServiceResponse(final String url, String code) {
		return restTemplate.getForObject(url, ComplaintTypeResponse.class, code, "");
	}

}