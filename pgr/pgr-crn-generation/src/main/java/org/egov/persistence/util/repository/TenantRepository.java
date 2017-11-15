package org.egov.persistence.util.repository;

import org.egov.common.contract.request.RequestInfo;
import org.egov.web.contract.RequestInfoBody;
import org.egov.web.contract.Tenant;
import org.egov.web.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class TenantRepository {

    private final String tenantServiceHost;
    private RestTemplate restTemplate;

    public TenantRepository(RestTemplate restTemplate, @Value("${egov.services.tenant.host}") String tenantServiceHost) {
        this.restTemplate = restTemplate;
        this.tenantServiceHost = tenantServiceHost;
    }

    public List<Tenant> fetchTenantByCode(String tenant) {
        String url = this.tenantServiceHost + "tenant/v1/tenant/_search?code=" + tenant;

        final RequestInfoBody requestInfoBody = new RequestInfoBody(RequestInfo.builder().build());

        final HttpEntity<RequestInfoBody> request = new HttpEntity<>(requestInfoBody);

        TenantResponse tr = restTemplate.postForObject(url, request, TenantResponse.class);
        if (!CollectionUtils.isEmpty(tr.getTenant()))
            return tr.getTenant();
        else
            return null;
    }
}