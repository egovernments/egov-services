package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.Disposal;
import org.egov.asset.model.enums.Status;
import org.egov.asset.service.AssetService;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AssetValidator {

	private static final Logger logger = LoggerFactory.getLogger(AssetValidator.class);

	@Autowired
	private AssetCategoryValidator assetCategoryValidator;

	@Autowired
	private AssetService assetService;

	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	private RestTemplate restTemplate;

	public void validateAsset(final AssetRequest assetRequest) {
		findAssetCategory(assetRequest);
		// findAsset(assetRequest); FIXME not need as per elzan remove the full
		// code later
	}

	public void findAssetCategory(final AssetRequest assetRequest) {

		final List<AssetCategory> assetCategories = assetCategoryValidator.findByIdAndCode(
				assetRequest.getAsset().getAssetCategory().getId(),
				assetRequest.getAsset().getAssetCategory().getCode(), assetRequest.getAsset().getTenantId());

		if (assetCategories.isEmpty())
			throw new RuntimeException("Invalid asset category");

	}

	public void findAsset(final AssetRequest assetRequest) {

		final Asset asset = assetRequest.getAsset();
		final String existingName = assetService.getAssetName(asset.getTenantId(), asset.getName());

		if (existingName != null) {
			if (existingName.equalsIgnoreCase(assetRequest.getAsset().getName())) {
				logger.info("duplicate asset with same name found");
				throw new RuntimeException("Duplicate asset name asset already exists");
			}
		} else
			logger.info("no duplicate asset with same name found");
	}

	public void validateAssetForDispose(final Disposal disposal) {
		final String url = applicationProperties.getAssetServiceHostName()
				+ applicationProperties.getAssetServiceSearchPath() + "?tenantId=" + disposal.getTenantId() + "&id="
				+ disposal.getAssetId().toString();
		final AssetResponse assetResponse = restTemplate.postForObject(url, new RequestInfo(), AssetResponse.class);
		if (!assetResponse.getAssets().isEmpty()
				&& Status.CAPITALIZED.compareTo(assetResponse.getAssets().get(0).getStatus()) == -1)
			throw new RuntimeException("Asset Status Should be Captalized for disposal of asset");

	}

}
