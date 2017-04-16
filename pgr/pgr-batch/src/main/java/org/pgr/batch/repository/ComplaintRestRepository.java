package org.pgr.batch.repository;

import org.pgr.batch.repository.contract.ComplaintResponse;
import org.pgr.batch.repository.contract.ServiceRequest;
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

    public List<ServiceRequest> getComplaintsEligibleForEscalation(final Long tenantId) {

        List<String> complaintStatus = Arrays.asList("FORWARDED","REGISTERED","INPROGRESS","REOPENED");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String escalationDate = simpleDateFormat.format(new Date());

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
        return restTemplate.exchange(url, HttpMethod.GET, entity, ComplaintResponse.class,tenantId,complaintStatus,escalationDate).getBody().getServiceRequests();
    }
}
