package org.pgr.batch.repository;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.contract.ComplaintResponse;
import org.pgr.batch.repository.contract.RequestInfoBody;
import org.pgr.batch.repository.contract.ServiceRequest;
import org.pgr.batch.repository.contract.ServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ComplaintRestRepository {

	private final RestTemplate restTemplate;
	private final String url;

	public ComplaintRestRepository(final RestTemplate restTemplate,
								   @Value("${egov.services.pgr.host}") final String pgrRestHost,
								   @Value("${egov.services.pgr.escalation_complaints}") final String url) {
		this.restTemplate = restTemplate;
		this.url = pgrRestHost + url;
	}

	public ServiceResponse getComplaintsEligibleForEscalation(String tenantId) {

		String status = String.join(",", Arrays.asList("FORWARDED", "REGISTERED", "INPROGRESS", "REOPENED"));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String escalationDate = simpleDateFormat.format(new Date());

		final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

		final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
		return restTemplate.postForObject(url, request, ServiceResponse.class, tenantId, status, escalationDate);
	}
}
