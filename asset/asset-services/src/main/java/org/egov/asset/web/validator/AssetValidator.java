package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.AssetCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator {
	
	@Autowired
	private AssetCategoryValidator assetCategoryValidator;
	
	public void validateAsset(AssetRequest assetRequest){
		findAssetCategory(assetRequest);
	}
	
	public void findAssetCategory(AssetRequest assetRequest) {
		
		List<AssetCategory>	assetCategories = assetCategoryValidator.findByIdAndCode(assetRequest.getAsset().getAssetCategory().getId(), 
				assetRequest.getAsset().getAssetCategory().getCode(), 
				assetRequest.getAsset().getTenantId());
		
		if(assetCategories.isEmpty()) {
			throw new RuntimeException("Invalid asset category");
		}
		
	}


}
