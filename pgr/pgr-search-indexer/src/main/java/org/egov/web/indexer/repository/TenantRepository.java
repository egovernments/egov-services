package org.egov.web.indexer.repository;

import org.egov.web.indexer.contract.City;
import org.egov.web.indexer.contract.TenantResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class TenantRepository {

    private final String tenantServiceHost;
    private RestTemplate restTemplate;

    public TenantRepository(RestTemplate restTemplate, @Value("${egov.services.tenant.host}") String tenantServiceHost) {
        this.restTemplate = restTemplate;
        this.tenantServiceHost = tenantServiceHost;
    }

    public City fetchTenantByCode(String tenant) {
        String url = this.tenantServiceHost + "v1/tenant/_search?code=" + tenant;

        TenantResponse tr = restTemplate.postForObject(url, null, TenantResponse.class);
        if (!CollectionUtils.isEmpty(tr.getTenant()))
            return tr.getTenant().get(0).getCity();
        else
            return null;
    }

}