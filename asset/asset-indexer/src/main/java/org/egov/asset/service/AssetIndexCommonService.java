package org.egov.asset.service;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RequestInfo;
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
                + applicationProperties.getAssetServiceSearchPath() + "?&tenantId=" + tenantId + "&id="
                + assetId.toString();
        log.info("asset search url :: " + url);
        final Asset asset = restTemplate.postForObject(url, requestInfo, Asset.class);
        log.info("asset object :: " + asset);
        return asset;
    }

    public SubScheme getSubSchemeData(final RequestInfo requestInfo, final String tenantId, final Long subSchemeId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceSubSchemesSearchPath() + "?&tenantId=" + tenantId + "&id="
                + subSchemeId;
        log.info("subscheme url :: " + url);
        final SubScheme subScheme = restTemplate.postForObject(url, requestInfo, SubScheme.class);
        log.info("subscheme object :: " + subScheme);
        return subScheme;
    }

    public Scheme getSchemeData(final RequestInfo requestInfo, final String tenantId, final Long schemeId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceSchemesSearchPath() + "?&tenantId=" + tenantId + "&id=" + schemeId;
        log.info("scheme url :: " + url);
        final Scheme scheme = restTemplate.postForObject(url, requestInfo, Scheme.class);
        log.info("scheme object :: " + scheme);
        return scheme;
    }

    public Fund getFundData(final RequestInfo requestInfo, final String tenantId, final Long fundId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + tenantId + "&id=" + fundId;
        log.info("fund url :: " + url);
        final Fund fund = restTemplate.postForObject(url, requestInfo, Fund.class);
        log.info("fund object :: " + fund);
        return fund;
    }

    public Function getFunctionData(final RequestInfo requestInfo, final String tenantId, final Long functionId) {
        final String url = applicationProperties.getEgfServiceHostName()
                + applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + tenantId + "&id="
                + functionId;
        log.info("function url :: " + url);
        final Function function = restTemplate.postForObject(url, requestInfo, Function.class);
        log.info("function object :: " + function);
        return function;
    }

    public List<Tenant> getTenantData(final RequestInfo requestInfo, final String tenantId) {
        final String url = applicationProperties.getTenantServiceHostName()
                + applicationProperties.getTenantServiceSearchPath() + "?&tenantId=" + tenantId;
        log.info("Tenant Service URL :: " + url);
        final TenantResponse tenantResponse = restTemplate.postForObject(url, requestInfo, TenantResponse.class);
        log.info("Tenant Response :: " + tenantResponse);
        return tenantResponse.getTenant();
    }
}
