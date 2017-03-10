package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.ComplaintResponse;
import org.egov.pgr.employee.enrichment.repository.contract.ServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintRestRepository {

	private final RestTemplate restTemplate;
	private final String url;

	public ComplaintRestRepository(final RestTemplate restTemplate,
			@Value("${egov.services.pgr.host}") final String pgrRestHost,
			@Value("${egov.services.pgr.complaint_crn}") final String url) {
		this.restTemplate = restTemplate;
		this.url = pgrRestHost + url;
	}

	public ServiceRequest getComplaintByCrn(final Long tenantId, final String serviceRequestId) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("api_id", "org.egov.pgr");
		headers.set("ver","1");
		headers.set("action", "GET");
		headers.set("ts","");
		headers.set("did", "");
		headers.set("key","");
		headers.set("msg_id", "");
		headers.set("requester_id", "");
		headers.set("auth_token", null);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		return restTemplate.exchange(url, HttpMethod.GET, entity, ComplaintResponse.class,tenantId, serviceRequestId).getBody().getServiceRequests().get(0);
	}

}
