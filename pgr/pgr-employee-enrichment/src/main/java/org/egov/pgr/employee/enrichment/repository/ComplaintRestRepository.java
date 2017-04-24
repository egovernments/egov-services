package org.egov.pgr.employee.enrichment.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.employee.enrichment.repository.contract.ComplaintResponse;
import org.egov.pgr.employee.enrichment.repository.contract.RequestInfoBody;
import org.egov.pgr.employee.enrichment.repository.contract.ServiceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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

    public ServiceRequest getComplaintByCrn(final String tenantId, final String serviceRequestId) {
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        return restTemplate.postForObject(url, request, ComplaintResponse.class, tenantId, serviceRequestId).getServiceRequests().get(0);
    }

}
