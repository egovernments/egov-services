package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator {
	
	@Autowired
	private AssetCategoryValidator assetCategoryValidator;
	
	@Autowired
	private AssetService assetService;
	
	public void validateAsset(AssetRequest assetRequest){
		findAssetCategory(assetRequest);
		findAsset(assetRequest);
	}
	
	public void findAssetCategory(AssetRequest assetRequest) {
		
		List<AssetCategory>	assetCategories = assetCategoryValidator.findByIdAndCode(assetRequest.getAsset().getAssetCategory().getId(), 
				assetRequest.getAsset().getAssetCategory().getCode(), 
				assetRequest.getAsset().getTenantId());
		
		if(assetCategories.isEmpty()) {
			throw new RuntimeException("Invalid asset category");
		}
		
	}
	
	public void findAsset(AssetRequest assetRequest){
		
		AssetCriteria assetCriteria = new AssetCriteria();
		assetCriteria.setTenantId(assetRequest.getAsset().getTenantId());
		assetCriteria.setName(assetRequest.getAsset().getName());
		List<Asset> assets = assetService.getAssets(assetCriteria);
		
		if(assets.get(0).getName().equalsIgnoreCase(assetRequest.getAsset().getName()))
			throw new RuntimeException("Duplicate asset name asset already exists");
	}


}
