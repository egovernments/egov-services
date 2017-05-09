package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.contract.AssetCategoryRequest;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCategoryCriteria;
import org.egov.asset.service.AssetCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetCategoryValidator {
	
	@Autowired
	private AssetCategoryService assetCategoryService;
	
	private static final Logger logger = LoggerFactory.getLogger(AssetCategoryValidator.class);
	
	
	public void validateAssetCategory(AssetCategoryRequest assetCategoryRequest) {
		
		List<AssetCategory> assetCategories = findByName(assetCategoryRequest.getAssetCategory().getName(),
														assetCategoryRequest.getAssetCategory().getTenantId());
		
		if(!assetCategories.isEmpty()) {
			throw new RuntimeException("Duplicate asset category name");
		}
	}
	
	public List<AssetCategory> findByName(String name, String tenantId) {
		
		AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();
		categoryCriteria.setName(name);
		categoryCriteria.setTenantId(tenantId);
		List<AssetCategory> assetCategories =null;
		
		try {
			 assetCategories = assetCategoryService.search(categoryCriteria);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("findByName assetCategories:"+assetCategories);
		}
		return assetCategories;
	}
	public List<AssetCategory> findByIdAndCode(Long id, String code,String tenantId) {
		
		AssetCategoryCriteria categoryCriteria = new AssetCategoryCriteria();
		
		categoryCriteria.setTenantId(tenantId);
		categoryCriteria.setId(id);;
		categoryCriteria.setCode(code);
		List<AssetCategory> assetCategories =null;
		
		try {
			 assetCategories = assetCategoryService.search(categoryCriteria);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.info("findById assetCategories:"+assetCategories);
		}
		
		return assetCategories;
	}


}
