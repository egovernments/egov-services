package org.egov.workflow.persistence.repository;

import org.egov.workflow.web.contract.ComplaintTypeResponse;
import org.egov.workflow.web.contract.ComplaintTypeServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ComplaintTypeRepository {

    private final RestTemplate restTemplate;
    private final String url;

    public ComplaintTypeRepository(final RestTemplate restTemplate,
            @Value("${egov.services.pgrrest.host}") final String complaintTypeServiceHost,
            @Value("${egov.services.pgr.complainttypes_by_code}") final String url) {
        this.restTemplate = restTemplate;
        this.url = complaintTypeServiceHost + url;

    }

    public ComplaintTypeResponse fetchComplaintTypeByCode(final String code, final String tenantId) {
        ComplaintTypeServiceResponse complaintTypeResponse = restTemplate.getForObject(url, ComplaintTypeServiceResponse.class, code, tenantId);
        return complaintTypeResponse.getComplaintType();
    }

}
