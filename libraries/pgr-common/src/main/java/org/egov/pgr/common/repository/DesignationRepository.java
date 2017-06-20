package org.egov.pgr.common.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.common.model.Designation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DesignationRepository {
    private final RestTemplate restTemplate;
    private final String url;
    private final static String DESIGNATION_BY_ID = "hr-masters/designations/_search?id={id}&tenantId={tenantId}";

    public DesignationRepository(RestTemplate restTemplate,
                              @Value("${hrmaster.host}") final String hrMasterServiceHostname) {
        this.restTemplate = restTemplate;
        this.url = hrMasterServiceHostname + DESIGNATION_BY_ID;
    }

    public Designation getDesignationById(Long id, String tenantId) {
        final DesignationResponse designationResponse = this.restTemplate
            .postForObject(this.url, buildRequestInfo(), DesignationResponse.class, id, tenantId);
        return designationResponse != null ? designationResponse.toDomain() : null;
    }

    private RequestInfoWrapper buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return RequestInfoWrapper.builder()
            .RequestInfo(requestInfo)
            .build();
    }
}
