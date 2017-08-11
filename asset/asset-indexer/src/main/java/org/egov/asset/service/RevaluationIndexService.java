package org.egov.asset.service;

import org.egov.asset.contract.RevaluationRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Function;
import org.egov.asset.model.Fund;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationIndex;
import org.egov.asset.model.Scheme;
import org.egov.asset.model.SubScheme;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.RevaluationIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RevaluationIndexService {

    @Autowired
    private RevaluationIndexRepository revaluationIndexRepository;

    @Autowired
    private AssetIndexCommonService assetIndexCommonService;

    public void postAssetRevaluation(final RevaluationRequest revaluationRequest) {
        if (revaluationRequest != null) {
            final RevaluationIndex revaluationIndex = prepareRevaluationIndex(revaluationRequest);
            log.info("the logged value of revaluationIndex ::" + revaluationIndex);
            revaluationIndexRepository.saveAssetRevaluation(revaluationIndex);
        } else
            log.info("RevaluationRequest object is null");
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
        setTenantProperties(revaluationIndex, revaluation.getTenantId());
        return revaluationIndex;
    }

    private void setFunctionData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        final Function function = assetIndexCommonService.getFunctionData(revaluation.getTenantId(),
                revaluation.getFunction());
        if (function != null) {
            revaluationIndex.setFunctionId(function.getId());
            revaluationIndex.setFunctionCode(function.getCode());
            revaluationIndex.setFunctionName(function.getName());
        }
    }

    private void setFundData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        final Fund fund = assetIndexCommonService.getFundData(revaluation.getTenantId(), revaluation.getFund());
        if (fund != null) {
            revaluationIndex.setFundId(fund.getId());
            revaluationIndex.setFundCode(fund.getCode());
            revaluationIndex.setFundName(fund.getName());
        }
    }

    private void setSchemeData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        final Scheme scheme = assetIndexCommonService.getSchemeData(revaluation.getTenantId(), revaluation.getScheme());
        if (scheme != null) {
            revaluationIndex.setSchemeId(scheme.getId());
            revaluationIndex.setSchemeCode(scheme.getCode());
            revaluationIndex.setSchemeName(scheme.getName());
        }
    }

    private void setSubSchemeData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        final SubScheme subscheme = assetIndexCommonService.getSubSchemeData(revaluation.getTenantId(),
                revaluation.getSubScheme());
        if (subscheme != null) {
            revaluationIndex.setSubSchemeId(subscheme.getId());
            revaluationIndex.setSubSchemeCode(subscheme.getCode());
            revaluationIndex.setSubSchemeName(subscheme.getName());
        }
    }

    private void setAssetData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        final Asset asset = assetIndexCommonService.getAssetData(revaluation.getAssetId(), revaluation.getTenantId());
        if (asset != null) {
            revaluationIndex.setAssetId(asset.getId());
            revaluationIndex.setAssetCode(asset.getCode());
            revaluationIndex.setAssetName(asset.getName());
        }
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

    private void setTenantProperties(final RevaluationIndex revaluationIndex, final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(tenantId).get(0);
        revaluationIndex.setCityName(tenant.getCity().getName());
        revaluationIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        revaluationIndex.setLocalName(tenant.getCity().getLocalName());
        revaluationIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        revaluationIndex.setDistrictName(tenant.getCity().getDistrictName());
        revaluationIndex.setRegionName(tenant.getCity().getRegionName());
    }
}
