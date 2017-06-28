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

    private RestTemplate restTemplate;
    private String crnServiceUrl;

    @Autowired
    public CrnRepository(RestTemplate restTemplate,
                         @Value("${crn.service.url}") String crnServiceUrl) {

        this.restTemplate = restTemplate;
        this.crnServiceUrl = crnServiceUrl;
    }

    public ServiceRequestRegistrationNumber getCrn() {
        RequestInfoBody requestBody = buildRequestInfo();
        return restTemplate.postForObject(crnServiceUrl, requestBody, ServiceRequestRegistrationNumber.class);
    }

    private RequestInfoBody buildRequestInfo() {
        final RequestInfo requestInfo = RequestInfo.builder().build();
        return new RequestInfoBody(requestInfo);
    }
}
