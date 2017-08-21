package org.egov.asset.service;

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
        final RequestInfo requestInfo = revaluationRequest.getRequestInfo();
        setRevaluationData(revaluationIndex, revaluation);
        setAssetData(requestInfo, revaluationIndex, revaluation);
        setSubSchemeData(requestInfo, revaluationIndex, revaluation);
        setSchemeData(requestInfo, revaluationIndex, revaluation);
        setFundData(requestInfo, revaluationIndex, revaluation);
        setFunctionData(requestInfo, revaluationIndex, revaluation);
        setAuditDetails(revaluationIndex, revaluation);
        setTenantProperties(requestInfo, revaluationIndex, revaluation.getTenantId());
        return revaluationIndex;
    }

    private void setRevaluationData(final RevaluationIndex revaluationIndex, final Revaluation revaluation) {
        revaluationIndex.setTenantId(revaluation.getTenantId());
        revaluationIndex.setRevaluationId(revaluation.getId());
        revaluationIndex.setCurrentCapitalizedValue(revaluation.getCurrentCapitalizedValue());
        revaluationIndex.setTypeOfChange(revaluation.getTypeOfChange().toString());
        revaluationIndex.setRevaluationAmount(revaluation.getRevaluationAmount());
        revaluationIndex.setValueAfterRevaluation(revaluation.getValueAfterRevaluation());
        revaluationIndex.setRevaluationDate(revaluation.getRevaluationDate());
        revaluationIndex.setRevaluatedBy(revaluation.getReevaluatedBy());
        revaluationIndex.setReasonForRevaluation(revaluation.getReasonForRevaluation());
        revaluationIndex.setFixedAssetsWrittenOffAccount(revaluation.getFixedAssetsWrittenOffAccount());
        revaluationIndex.setComments(revaluation.getComments());
        revaluationIndex.setStatus(revaluation.getStatus());
        revaluationIndex.setVoucherReference(revaluation.getVoucherReference());

    }

    private void setFunctionData(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final Revaluation revaluation) {
        if (revaluation.getFunction() != null) {
            final Function function = assetIndexCommonService.getFunctionData(requestInfo, revaluation.getTenantId(),
                    revaluation.getFunction());
            if (function != null) {
                revaluationIndex.setFunctionId(function.getId());
                revaluationIndex.setFunctionCode(function.getCode());
                revaluationIndex.setFunctionName(function.getName());
            }
        }
    }

    private void setFundData(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final Revaluation revaluation) {
        if (revaluation.getFund() != null) {
            final Fund fund = assetIndexCommonService.getFundData(requestInfo, revaluation.getTenantId(),
                    revaluation.getFund());
            if (fund != null) {
                revaluationIndex.setFundId(fund.getId());
                revaluationIndex.setFundCode(fund.getCode());
                revaluationIndex.setFundName(fund.getName());
            }
        }
    }

    private void setSchemeData(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final Revaluation revaluation) {
        if (revaluation.getScheme() != null) {
            final Scheme scheme = assetIndexCommonService.getSchemeData(requestInfo, revaluation.getTenantId(),
                    revaluation.getScheme());
            if (scheme != null) {
                revaluationIndex.setSchemeId(scheme.getId());
                revaluationIndex.setSchemeCode(scheme.getCode());
                revaluationIndex.setSchemeName(scheme.getName());
            }
        }
    }

    private void setSubSchemeData(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final Revaluation revaluation) {
        if (revaluation.getSubScheme() != null) {
            final SubScheme subscheme = assetIndexCommonService.getSubSchemeData(requestInfo, revaluation.getTenantId(),
                    revaluation.getSubScheme());
            if (subscheme != null) {
                revaluationIndex.setSubSchemeId(subscheme.getId());
                revaluationIndex.setSubSchemeCode(subscheme.getCode());
                revaluationIndex.setSubSchemeName(subscheme.getName());
            }
        }
    }

    private void setAssetData(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final Revaluation revaluation) {
        final Asset asset = assetIndexCommonService.getAssetData(requestInfo, revaluation.getAssetId(),
                revaluation.getTenantId());
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

    private void setTenantProperties(final RequestInfo requestInfo, final RevaluationIndex revaluationIndex,
            final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(requestInfo, tenantId).get(0);
        revaluationIndex.setCityName(tenant.getCity().getName());
        revaluationIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        revaluationIndex.setLocalName(tenant.getCity().getLocalName());
        revaluationIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        revaluationIndex.setDistrictName(tenant.getCity().getDistrictName());
        revaluationIndex.setRegionName(tenant.getCity().getRegionName());
    }
}
