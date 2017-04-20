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

    public ComplaintType fetchComplaintTypeByCode(String code, String tenantId) {
        String url = this.complaintTypeServiceHostname + "pgr/services/{serviceCode}?tenantId={tenantId}";
        return getComplaintTypeServiceResponse(url, code, tenantId).getComplaintType();
    }

    private ComplaintTypeResponse getComplaintTypeServiceResponse(final String url, String code, String tenantId) {
        return restTemplate.getForObject(url, ComplaintTypeResponse.class, code, tenantId);
    }

}