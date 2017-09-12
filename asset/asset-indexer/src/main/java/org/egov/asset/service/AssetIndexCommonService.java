package org.egov.asset.service;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.FunctionResponse;
import org.egov.asset.contract.FundResponse;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.contract.SchemeResponse;
import org.egov.asset.contract.SubSchemeResponse;
import org.egov.asset.contract.TenantResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Scheme;
import org.egov.asset.model.SubScheme;
import org.egov.asset.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetIndexCommonService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationProperties applicationProperties;

    public Asset getAssetData(final RequestInfo requestInfo, final Long assetId, final String tenantId) {
        final String url = applicationProperties.getAssetServiceHostName()
                + applicationProperties.getAssetServiceSearchPath() + "?&tenantId=" + tenantId + "&id=" + assetId;
        log.info("asset search url :: " + url);
        final AssetResponse assetResponse = restTemplate.postForObject(url, requestInfo, AssetResponse.class);
        log.info("asset Response :: " + assetResponse);
        final List<Asset> assets = assetResponse.getAssets();
        if (!assets.isEmpty())
            return assets.get(0);
        else
            throw new RuntimeException("There is no Asset for tenantId :: " + tenantId);
    }

    public SubScheme getSubSchemeData(final RequestInfo requestInfo, final String tenantId, final Long subSchemeId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceSubSchemesSearchPath() + "?&tenantId=" + tenantId + "&id="
                + subSchemeId;
        log.info("subscheme search url :: " + url);
        final SubSchemeResponse subSchemeResponse = restTemplate.postForObject(url, requestInfo,
                SubSchemeResponse.class);
        log.info("subscheme Response :: " + subSchemeResponse);
        return subSchemeResponse.getSubScheme();
    }

    public Scheme getSchemeData(final RequestInfo requestInfo, final String tenantId, final Long schemeId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceSchemesSearchPath() + "?&tenantId=" + tenantId + "&id=" + schemeId;
        log.info("scheme search url :: " + url);
        final SchemeResponse schemeResponse = restTemplate.postForObject(url, requestInfo, SchemeResponse.class);
        log.info("scheme Response :: " + schemeResponse);
        return schemeResponse.getScheme();
    }

    public Fund getFundData(final RequestInfo requestInfo, final String tenantId, final Long fundId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + tenantId + "&id=" + fundId;
        log.info("fund search url :: " + url);
        final FundResponse fundResponse = restTemplate.postForObject(url, requestInfo, FundResponse.class);
        log.info("fund Response :: " + fundResponse);
        return fundResponse.getFund();
    }

    public Function getFunctionData(final RequestInfo requestInfo, final String tenantId, final Long functionId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + tenantId + "&id="
                + functionId;
        log.info("function search url :: " + url);
        final FunctionResponse functionResponse = restTemplate.postForObject(url, requestInfo, FunctionResponse.class);
        log.info("function Response :: " + functionResponse);
        return functionResponse.getFunction();
    }

    public List<Tenant> getTenantData(final RequestInfo requestInfo, final String tenantId) {
        final String url = applicationProperties.getTenantServiceHostName()
                + applicationProperties.getTenantServiceSearchPath() + "?&code=" + tenantId;
        log.info("Tenant Service URL :: " + url);
        final TenantResponse tenantResponse = restTemplate.postForObject(url, requestInfo, TenantResponse.class);
        log.info("Tenant Response :: " + tenantResponse);
        return tenantResponse.getTenant();
    }
}
