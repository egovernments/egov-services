package org.egov.pgrrest.read.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgrrest.read.domain.model.ServiceRequestRegistrationNumber;
import org.egov.pgrrest.read.web.contract.RequestInfoBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CrnRepository {

    private static final String CREATE_CRN_URL = "crn-generation/crn/v1/_create?tenantId={tenantId}";
    private RestTemplate restTemplate;
    private String crnServiceUrl;

    @Autowired
    public CrnRepository(RestTemplate restTemplate,
                         @Value("${crn.host}") String crnHost) {

        this.restTemplate = restTemplate;
        this.crnServiceUrl = crnHost + CREATE_CRN_URL;
    }

    public ServiceRequestRegistrationNumber getCrn(String tenantId) {
        RequestInfoBody requestBody = buildRequestInfo();
        return restTemplate.postForObject(crnServiceUrl, requestBody, ServiceRequestRegistrationNumber.class,tenantId);
    }

    private RequestInfoBody buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return new RequestInfoBody(requestInfo);
    }
}
