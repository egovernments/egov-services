package org.egov.asset.service;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationIndex;
import org.egov.asset.model.Scheme;
import org.egov.asset.model.SubScheme;
import org.egov.asset.repository.RevaluationIndexRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RevaluationIndexService {

	public static final Logger LOGGER = LoggerFactory.getLogger(RevaluationIndexService.class);

	@Autowired
	private RevaluationIndexRepository revaluationIndexRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ApplicationProperties applicationProperties;

	public void postAssetRevaluation(final RevaluationRequest revaluationRequest) {
		if (revaluationRequest != null) {
			final RevaluationIndex revaluationIndex = prepareRevaluationIndex(revaluationRequest);
			LOGGER.info("the logged value of revaluationIndex ::" + revaluationIndex);
			revaluationIndexRepository.saveOrUpdateAssetRevaluation(revaluationIndex);
		} else
			LOGGER.info("RevaluationRequest object is null");
	}

	private RevaluationIndex prepareRevaluationIndex(final RevaluationRequest revaluationRequest) {

		final RevaluationIndex revaluationIndex = new RevaluationIndex();
		final Revaluation revaluation = revaluationRequest.getRevaluation();
		revaluationIndex.setRevaluationData(revaluation);
		setAssetData(revaluationIndex, revaluation);
		setSubSchemeData(revaluationIndex, revaluation);
		setSchemeData(revaluationIndex, revaluation);
		setFundData(revaluationIndex, revaluation);
		setFunctionData(revaluationIndex, revaluation);
		setAuditDetails(revaluationIndex, revaluation);
		return revaluationIndex;
	}

	private void setFunctionData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final Function function = getFunctionData(revaluation);
		if (function != null) {
			revaluationIndex.setFunctionId(function.getId());
			revaluationIndex.setFunctionCode(function.getCode());
			revaluationIndex.setFunctionName(function.getName());
		}
	}

	private void setFundData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final Fund fund = getFundData(revaluation);
		if (fund != null) {
			revaluationIndex.setFundId(fund.getId());
			revaluationIndex.setFundCode(fund.getCode());
			revaluationIndex.setFundName(fund.getName());
		}
	}

	private void setSchemeData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final Scheme scheme = getSchemeData(revaluation);
		if (scheme != null) {
			revaluationIndex.setSchemeId(scheme.getId());
			revaluationIndex.setSchemeCode(scheme.getCode());
			revaluationIndex.setSchemeName(scheme.getName());
		}
	}

	private void setSubSchemeData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final SubScheme subscheme = getSubSchemeData(revaluation);
		if (subscheme != null) {
			revaluationIndex.setSubSchemeId(subscheme.getId());
			revaluationIndex.setSubSchemeCode(subscheme.getCode());
			revaluationIndex.setSubSchemeName(subscheme.getName());
		}
	}

	private void setAssetData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final Asset asset = getAssetData(revaluation.getAssetId(), revaluation.getTenantId());
		if (asset != null) {
			revaluationIndex.setAssetId(asset.getId());
			revaluationIndex.setAssetCode(asset.getCode());
			revaluationIndex.setAssetName(asset.getName());
		}
	}

	private Asset getAssetData(final Long assetId, final String tenantId) {
		final String url = applicationProperties.getAssetServiceHostName()
				+ applicationProperties.getAssetServiceSearchPath() + "?&tenantId=" + tenantId + "&id="
				+ assetId.toString();
		LOGGER.info("asset search url :: " + url);
		final Asset asset = restTemplate.postForObject(url, new RequestInfo(), Asset.class);
		LOGGER.info("asset object :: " + asset);

		return asset;
	}

	private SubScheme getSubSchemeData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceSubSchemesSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getSubScheme().toString();
		LOGGER.info("subscheme url :: " + url);
		final SubScheme subScheme = restTemplate.postForObject(url, new RequestInfo(), SubScheme.class);
		LOGGER.info("subscheme object :: " + subScheme);
		return subScheme;
	}

	private Scheme getSchemeData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceSchemesSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getScheme().toString();
		LOGGER.info("scheme url :: " + url);
		final Scheme scheme = restTemplate.postForObject(url, new RequestInfo(), Scheme.class);
		LOGGER.info("scheme object :: " + scheme);
		return scheme;
	}

	private Fund getFundData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceFundsSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getFund().toString();
		LOGGER.info("fund url :: " + url);
		final Fund fund = restTemplate.postForObject(url, new RequestInfo(), Fund.class);
		LOGGER.info("fund object :: " + fund);

		return fund;
	}

	private Function getFunctionData(final Revaluation revaluation) {
		final String url = applicationProperties.getEgfServiceHostName()
				+ applicationProperties.getEgfServiceFunctionsSearchPath() + "?&tenantId=" + revaluation.getTenantId()
				+ "&id=" + revaluation.getFunction().toString();
		LOGGER.info("function url :: " + url);
		final Function function = restTemplate.postForObject(url, new RequestInfo(), Function.class);
		LOGGER.info("function object :: " + function);

		return function;
	}

	private void setAuditDetails(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
		final AuditDetails ad = revaluation.getAuditDetails();
		if (ad != null) {
			revaluationIndex.setCreatedBy(ad.getCreatedBy());
			revaluationIndex.setCreatedDate(ad.getCreatedDate());
			revaluationIndex.setLastModifiedBy(ad.getLastModifiedBy());
			revaluationIndex.setLastModifiedDate(ad.getLastModifiedDate());
		}
	}
}
