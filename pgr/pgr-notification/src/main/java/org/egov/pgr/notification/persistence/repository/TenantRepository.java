package org.egov.pgr.notification.persistence.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.pgr.notification.domain.model.Tenant;
import org.egov.pgr.notification.persistence.dto.RequestInfoBody;
import org.egov.pgr.notification.persistence.dto.TenantResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TenantRepository {

    private final String tenantServiceUrl;
    private RestTemplate restTemplate;

    public TenantRepository(RestTemplate restTemplate,
                            @Value("${tenant.host}") String tenantServiceHost,
                            @Value("${get.tenant.url}") String tenantServiceUrl
                            ) {
        this.restTemplate = restTemplate;
        this.tenantServiceUrl = tenantServiceHost + tenantServiceUrl;
    }

    public Tenant fetchTenantById(String tenantId) {
        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());
        TenantResponse tenantResponse = restTemplate
            .postForObject(tenantServiceUrl, requestInfoBody, TenantResponse.class, tenantId);
        return tenantResponse.toDomainTenant();
    }
}
