package org.egov.pgr.notification.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.notification.domain.model.ServiceType;
import org.egov.pgr.notification.persistence.dto.RequestInfoBody;
import org.egov.pgr.notification.persistence.dto.ServiceTypeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceTypeRepository {

    private final String url;
    private RestTemplate restTemplate;

    public ServiceTypeRepository(RestTemplate restTemplate,
                                 @Value("${pgr.host}") String pgrRestHost,
                                 @Value("${get.pgr.servicetype}") String complaintTypeUrl) {
        this.restTemplate = restTemplate;
        this.url = pgrRestHost + complaintTypeUrl;
    }

    public ServiceType getServiceTypeByCode(String serviceTypeCode, String tenantId) {
        final RequestInfoBody request = new RequestInfoBody(RequestInfo.builder().build());
        final ServiceTypeResponse response = restTemplate
            .postForObject(url, request, ServiceTypeResponse.class, serviceTypeCode, tenantId);
        return response.toDomain();
    }
}
