package org.egov.web.indexer.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.indexer.contract.ComplaintType;
import org.egov.web.indexer.contract.ComplaintTypeResponse;
import org.egov.web.indexer.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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

    public ComplaintType fetchComplaintTypeByCode(String code, String tenantId) {
        String url = this.complaintTypeServiceHostname + "pgr/services/{serviceCode}?tenantId={tenantId}";
        return getComplaintTypeServiceResponse(url, code, tenantId).getComplaintTypes().get(0);
    }

    private ComplaintTypeResponse getComplaintTypeServiceResponse(final String url, String code, String tenantId) {
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);
        return restTemplate.postForObject(url, request, ComplaintTypeResponse.class, code, tenantId);
    }

}