package org.egov.pgr.employee.enrichment.repository;

import org.egov.pgr.employee.enrichment.repository.contract.ComplaintTypeResponse;
import org.springframework.beans.factory.annotation.Value;
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
        final ComplaintTypeResponse response =
            restTemplate.getForObject(this.url, ComplaintTypeResponse.class, complaintTypeCode, tenantId);
        return String.valueOf(response.getId());
    }
}

