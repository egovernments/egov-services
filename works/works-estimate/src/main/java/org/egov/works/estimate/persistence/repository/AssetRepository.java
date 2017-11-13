package org.egov.works.estimate.persistence.repository;

import org.egov.works.estimate.web.contract.Asset;
import org.egov.works.estimate.web.contract.AssetResponse;
import org.egov.works.estimate.web.contract.RequestInfo;
import org.egov.works.workflow.contracts.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AssetRepository {

    private RestTemplate restTemplate;

    private String url;

    public AssetRepository(final RestTemplate restTemplate, @Value("${egov.asset.service.host}") final String assetServiceHost,
                           @Value("${egov.asset.searchbycodeurl}") final String url) {
        this.restTemplate = restTemplate;
        this.url = assetServiceHost + url;

    }

    public List<Asset> searchAssets(final String tenantId, final String assetCode, final RequestInfo requestInfo) {
        RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
        requestInfoWrapper.setRequestInfo(requestInfo);
        return restTemplate.postForObject(url,requestInfoWrapper, AssetResponse.class, tenantId, assetCode).getAssets();
    }
}
