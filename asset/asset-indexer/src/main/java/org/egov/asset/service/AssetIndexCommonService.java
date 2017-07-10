package org.egov.asset.service;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.contract.TenantResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.Scheme;
import org.egov.asset.model.SubScheme;
import org.egov.asset.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetIndexCommonService {
	public static final Logger LOGGER = LoggerFactory.getLogger(AssetIndexCommonService.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public Asset getAssetData(final Long assetId, final String tenantId) {
		final String url = applicationProperties.getAssetServiceHostName()
				+ applicationProperties.getAssetServiceSearchPath() + "?&tenantId=" + tenantId + "&id="
				+ assetId.toString();
		LOGGER.info("asset search url :: " + url);
		final Asset asset = restTemplate.postForObject(url, new RequestInfo(), Asset.class);
		LOGGER.info("asset object :: " + asset);
		return asset;
	}

	public SubScheme getSubSchemeData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceSubSchemesSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getSubScheme().toString();
		LOGGER.info("subscheme url :: " + url);
		final SubScheme subScheme = restTemplate.postForObject(url, new RequestInfo(), SubScheme.class);
		LOGGER.info("subscheme object :: " + subScheme);
		return subScheme;
	}

	public Scheme getSchemeData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceSchemesSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getScheme().toString();
		LOGGER.info("scheme url :: " + url);
		final Scheme scheme = restTemplate.postForObject(url, new RequestInfo(), Scheme.class);
		LOGGER.info("scheme object :: " + scheme);
		return scheme;
	}

	public Fund getFundData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getFund().toString();
		LOGGER.info("fund url :: " + url);
		final Fund fund = restTemplate.postForObject(url, new RequestInfo(), Fund.class);
		LOGGER.info("fund object :: " + fund);
		return fund;
	}

	public Function getFunctionData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getFunction().toString();
		LOGGER.info("function url :: " + url);
		final Function function = restTemplate.postForObject(url, new RequestInfo(), Function.class);
		LOGGER.info("function object :: " + function);
		return function;
	}

	public List<Tenant> getTenantData(final String code) {
		final String url = applicationProperties.getTenantServiceHostName()
				+ applicationProperties.getTenantServiceSearchPath() + "?code=" + code;
		final TenantResponse tenantResponse = restTemplate.postForObject(url, new RequestInfo(), TenantResponse.class);
		return tenantResponse.getTenant();
	}
}
