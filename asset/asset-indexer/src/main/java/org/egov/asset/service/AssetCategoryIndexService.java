package org.egov.asset.service;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.contract.RequestInfo;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryIndex;
import org.egov.asset.model.Tenant;
import org.egov.asset.repository.AssetCategoryIndexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetCategoryIndexService {

    @Autowired
    private AssetCategoryIndexRepository assetCategoryIndexRepository;

    @Autowired
    private AssetIndexCommonService assetIndexCommonService;

    @Autowired
    private ObjectMapper mapper;

    public void postAssetCategory(final AssetCategoryRequest assetCategoryRequest) {
        final AssetCategoryIndex assetCategoryIndex = prepareAssetCategoryIndex(assetCategoryRequest);
        log.info("the logged value of assetCategoryIndex ::" + assetCategoryIndex);
        assetCategoryIndexRepository.saveAssetCategory(assetCategoryIndex);
    }

    private AssetCategoryIndex prepareAssetCategoryIndex(final AssetCategoryRequest assetCategoryRequest) {
        final AssetCategoryIndex assetCategoryIndex = new AssetCategoryIndex();
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        setAssetCategoryData(assetCategoryIndex, assetCategory);
        setTenantProperties(assetCategoryIndex, assetCategoryRequest.getRequestInfo(), assetCategory.getTenantId());
        return assetCategoryIndex;
    }

    private void setAssetCategoryData(final AssetCategoryIndex assetCategoryIndex, final AssetCategory assetCategory) {
        assetCategoryIndex.setTenantId(assetCategory.getTenantId());
        assetCategoryIndex.setId(assetCategory.getId());
        assetCategoryIndex.setCode(assetCategory.getCode());
        assetCategoryIndex.setName(assetCategory.getName());
        assetCategoryIndex.setAssetCategoryType(assetCategory.getAssetCategoryType().toString());
        assetCategoryIndex.setParent(assetCategory.getParent());
        assetCategoryIndex.setDepreciationMethod(assetCategory.getDepreciationMethod().toString());
        assetCategoryIndex.setIsAssetAllow(assetCategory.getIsAssetAllow());
        assetCategoryIndex.setAssetAccount(assetCategory.getAssetAccount());
        assetCategoryIndex.setAccumulatedDepreciationAccount(assetCategory.getAccumulatedDepreciationAccount());
        assetCategoryIndex.setRevaluationReserveAccount(assetCategory.getRevaluationReserveAccount());
        assetCategoryIndex.setDepreciationExpenseAccount(assetCategory.getDepreciationExpenseAccount());
        assetCategoryIndex.setUnitOfMeasurement(assetCategory.getUnitOfMeasurement());
        assetCategoryIndex.setVersion(assetCategory.getVersion());
        assetCategoryIndex.setDepreciationRate(assetCategory.getDepreciationRate());

        try {
            final String customFields = mapper.writeValueAsString(assetCategory.getAssetFieldsDefination());
            assetCategoryIndex.setAssetFieldsDefination(customFields);
        } catch (final JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void setTenantProperties(final AssetCategoryIndex assetCategoryIndex, final RequestInfo requestInfo,
            final String tenantId) {
        final Tenant tenant = assetIndexCommonService.getTenantData(requestInfo, tenantId).get(0);
        assetCategoryIndex.setCityName(tenant.getCity().getName());
        assetCategoryIndex.setUlbGrade(tenant.getCity().getUlbGrade());
        assetCategoryIndex.setLocalName(tenant.getCity().getLocalName());
        assetCategoryIndex.setDistrictCode(tenant.getCity().getDistrictCode());
        assetCategoryIndex.setDistrictName(tenant.getCity().getDistrictName());
        assetCategoryIndex.setRegionName(tenant.getCity().getRegionName());
    }

}
