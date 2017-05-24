package org.pgr.batch.repository;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.contract.ComplaintTypeServiceResponse;
import org.pgr.batch.repository.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeRepository {

    private final String url;
    private RestTemplate restTemplate;

    public ComplaintTypeRepository(RestTemplate restTemplate,
                                   @Value("${egov.services.pgr.host}") String pgrRestHost,
                                   @Value("${egov.services.pgr.complainttype}") String complaintTypeUrl) {
        this.restTemplate = restTemplate;
        this.url = pgrRestHost + complaintTypeUrl;
    }

    public String getComplaintTypeId(String complaintTypeCode, String tenantId) {
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        return String.valueOf(restTemplate.postForObject(url, request, ComplaintTypeServiceResponse.class, complaintTypeCode, tenantId).getComplaintTypes().get(0).getId());

    }
}

