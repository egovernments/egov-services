package org.egov.persistence.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.domain.model.Tenant;
import org.egov.persistence.dto.RequestInfoBody;
import org.egov.persistence.dto.TenantResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
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
        log.info("Fetching tenant with id" + tenantId);
        TenantResponse tenantResponse = restTemplate
            .postForObject(tenantServiceUrl, requestInfoBody, TenantResponse.class, tenantId);
        return tenantResponse.toDomainTenant();
    }
}
