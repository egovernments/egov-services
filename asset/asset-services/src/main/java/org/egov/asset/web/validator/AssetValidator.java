package org.egov.asset.web.validator;

import java.util.List;

import org.egov.asset.contract.AssetRequest;
import org.egov.asset.contract.DisposalRequest;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCategory;
import org.egov.asset.model.DisposalCriteria;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.service.AssetCurrentAmountService;
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

	@Autowired
	private AssetCurrentAmountService assetCurrentAmountService;

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

	public void validateRevaluationCriteria(RevaluationCriteria revaluationCriteria) {
		if (revaluationCriteria.getFromDate() == null && revaluationCriteria.getToDate() != null) {
			throw new RuntimeException("Invalid Search! from date required");
		}
		if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() == null) {
			throw new RuntimeException("Invalid Search! to date required");
		}
		if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() != null
				&& revaluationCriteria.getToDate().compareTo(revaluationCriteria.getFromDate()) == -1) {
			throw new RuntimeException("Invalid Search! to date should not be less than from date");
		}

		if (revaluationCriteria.getFromDate() != null && revaluationCriteria.getToDate() != null
				&& revaluationCriteria.getToDate().compareTo(revaluationCriteria.getFromDate()) == 0) {
			throw new RuntimeException("Invalid Search! to date should not be equal to from date");
		}
	}

	public void validateDisposalCriteria(DisposalCriteria disposalCriteria) {
		if (disposalCriteria.getFromDate() == null && disposalCriteria.getToDate() != null) {
			throw new RuntimeException("Invalid Search! from date required");
		}
		if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() == null) {
			throw new RuntimeException("Invalid Search! to date required");
		}

		if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null
				&& disposalCriteria.getToDate().compareTo(disposalCriteria.getFromDate()) == -1) {
			throw new RuntimeException("Invalid Search! to date should not be less than from date");
		}

		if (disposalCriteria.getFromDate() != null && disposalCriteria.getToDate() != null
				&& disposalCriteria.getToDate().compareTo(disposalCriteria.getFromDate()) == 0) {
			throw new RuntimeException("Invalid Search! to date should not be equal to from date");
		}
	}

	public void validateDisposal(final DisposalRequest disposalRequest) {
		final Asset asset = assetCurrentAmountService.getAsset(disposalRequest.getDisposal().getAssetId(),
				disposalRequest.getDisposal().getTenantId(), disposalRequest.getRequestInfo());
		validateAssetForDisposedStatus(asset);
		validateAssetForCapitalizedStatus(asset);
	}

	private void validateAssetForCapitalizedStatus(final Asset asset) {
		if (!"CAPITALIZED".equalsIgnoreCase(asset.getStatus()))
			throw new RuntimeException("Status of Asset " + asset.getName()
					+ " Should be Captalized for Reevaluation, Depreciation and Disposal/sale");
	}

	private void validateAssetForDisposedStatus(final Asset asset) {
		if ("DISPOSED".equalsIgnoreCase(asset.getStatus()))
			throw new RuntimeException("Asset " + asset.getName() + " is already Disposed");
	}

}
