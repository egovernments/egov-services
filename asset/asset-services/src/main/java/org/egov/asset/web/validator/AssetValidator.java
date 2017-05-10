package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.service.AssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AssetValidator {

	private static final Logger logger = LoggerFactory.getLogger(AssetValidator.class);

	@Autowired
	private AssetCategoryValidator assetCategoryValidator;

	@Autowired
	private AssetService assetService;

	public void validateAsset(AssetRequest assetRequest) {
		findAssetCategory(assetRequest);
		findAsset(assetRequest);
	}

	public void findAssetCategory(AssetRequest assetRequest) {

		List<AssetCategory> assetCategories = assetCategoryValidator.findByIdAndCode(
				assetRequest.getAsset().getAssetCategory().getId(),
				assetRequest.getAsset().getAssetCategory().getCode(), assetRequest.getAsset().getTenantId());

		if (assetCategories.isEmpty()) {
			throw new RuntimeException("Invalid asset category");
		}

	}

	public void findAsset(AssetRequest assetRequest) {

		Asset asset = assetRequest.getAsset();
		String existingName = assetService.getAssetName(asset.getTenantId(), asset.getName());

		if (existingName != null) {
			if (existingName.equalsIgnoreCase(assetRequest.getAsset().getName())){
				logger.info("duplicate asset with same name found");
				throw new RuntimeException("Duplicate asset name asset already exists");
			}
		} else {
			logger.info("no duplicate asset with same name found");
		}
	}

}
