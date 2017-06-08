package org.pgr.batch.repository;

import org.egov.common.contract.request.RequestInfo;
import org.pgr.batch.repository.contract.PositionsResponse;
import org.pgr.batch.repository.contract.RequestInfoWrapper;
import org.pgr.batch.repository.contract.SearchTenantResponse;
import org.pgr.batch.service.model.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Service
public class TenantRepository {

    private final String url;
    private RestTemplate restTemplate;

    public TenantRepository(RestTemplate restTemplate,
                            @Value("${egov.services.tenant.host}") String host,
                            @Value("${egov.services.tenant.search}") String endpointUrl) {
        this.url = host + endpointUrl;
        this.restTemplate = restTemplate;
    }

    public SearchTenantResponse getAllTenants() {
        RequestInfoWrapper wrapper = RequestInfoWrapper.builder().requestInfo(RequestInfo.builder().apiId("apiId").ver("ver").ts(new Date()).build()).build();
        final HttpEntity<RequestInfoWrapper> request = new HttpEntity<>(wrapper);
        return restTemplate.postForObject(this.url, request,SearchTenantResponse.class);
    }
}
