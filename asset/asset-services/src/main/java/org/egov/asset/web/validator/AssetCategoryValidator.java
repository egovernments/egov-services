package org.egov.asset.web.validator;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.egov.asset.service.AssetCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AssetCategoryValidator {

    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private AssetCommonService assetCommonService;

    public void validateAssetCategory(final AssetCategoryRequest assetCategoryRequest) {

        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final List<AssetCategory> assetCategories = findByName(assetCategory.getName(), assetCategory.getTenantId());

        if (!assetCategories.isEmpty())
            throw new RuntimeException("Duplicate asset category name");

        validateDepreciationRate(assetCategory);

    }

    private void validateDepreciationRate(final AssetCategory assetCategory) {
        final Double depreciationRate = assetCategory.getDepreciationRate();
        assetCommonService.validateDepreciationRateValue(depreciationRate);
        assetCategory.setDepreciationRate(assetCommonService.getDepreciationRate(depreciationRate));
    }

    public List<AssetCategory> findByName(final String name, final String tenantId) {

        final AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();
        categoryCriteria.setName(name);
        categoryCriteria.setTenantId(tenantId);
        List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();

        try {
            assetCategories = assetCategoryService.search(categoryCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("findByName assetCategories:" + assetCategories);
        }
        return assetCategories;
    }

    public List<AssetCategory> findByIdAndCode(final Long id, final String code, final String tenantId) {

        final AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();

        categoryCriteria.setTenantId(tenantId);
        categoryCriteria.setId(id);
        categoryCriteria.setCode(code);
        List<AssetCategory> assetCategories = new ArrayList<AssetCategory>();

        try {
            assetCategories = assetCategoryService.search(categoryCriteria);
        } catch (final Exception ex) {
            ex.printStackTrace();
            log.info("findById assetCategories:" + assetCategories);
        }

        return assetCategories;
    }

    public void validateAssetCategoryForUpdate(final AssetCategoryRequest assetCategoryRequest) {
        final AssetCategory assetCategory = assetCategoryRequest.getAssetCategory();
        final List<AssetCategory> assetCategories = findByIdAndCode(assetCategory.getId(), assetCategory.getCode(),
                assetCategory.getTenantId());
        if (assetCategories.isEmpty())
            throw new RuntimeException("Invalid Asset Category Code for Asset :: " + assetCategory.getName());

        validateDepreciationRate(assetCategory);
    }

}
