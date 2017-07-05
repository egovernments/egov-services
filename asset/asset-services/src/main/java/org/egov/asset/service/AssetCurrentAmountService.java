package org.egov.asset.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.common.contract.request.RequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetCurrentAmountService {

	@Autowired
	private AssetService assetService;

	@Autowired
	private RevaluationService revaluationService;

	private static final Logger logger = LoggerFactory.getLogger(AssetCurrentAmountService.class);

	public AssetCurrentValueResponse getCurrentAmount(final Long assetId, final String tenantId,
			final RequestInfo requestInfo) {

		BigDecimal currentValue = null;
		final Asset asset = getAsset(assetId, tenantId, requestInfo);
		final Revaluation revaluation = getRevaluateAsset(assetId, tenantId);
		currentValue = asset.getGrossValue();

		if (revaluation != null) {
			currentValue = revaluation.getCurrentCapitalizedValue();
			if (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.INCREASED.toString()))
				currentValue = currentValue.add(revaluation.getRevaluationAmount());
			else if (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.DECREASED.toString()))
				currentValue = currentValue.subtract(revaluation.getRevaluationAmount());
		}

		return getResponse(currentValue, tenantId, assetId);
	}

	public Asset getAsset(final Long assetId, final String tenantId, final RequestInfo requestInfo) {

		logger.info("AssetCurrentAmountService getAsset");

		final AssetCriteria assetCriteria = new AssetCriteria();
		final List<Long> assetIds = new ArrayList<Long>();
		assetIds.add(assetId);
		assetCriteria.setId(assetIds);
		assetCriteria.setTenantId(tenantId);
		final AssetResponse assetResponse = assetService.getAssets(assetCriteria, requestInfo);

		Asset asset = null;
		if (assetResponse.getAssets().size() != 0)
			asset = assetResponse.getAssets().get(0);

		if (asset == null)
			throw new RuntimeException("Invalid Asset");

		return asset;
	}

	public Revaluation getRevaluateAsset(final Long assetId, final String tenantId) {

		logger.info("AssetCurrentAmountService getRevaluateAsset");

		final List<Long> assetIds = new ArrayList<Long>();
		assetIds.add(assetId);

		final RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().assetId(assetIds)
				.tenantId(tenantId).status("ACTIVE").build();

		final RevaluationResponse revaluationResponse = revaluationService.search(revaluationCriteria);

		Revaluation revaluation = null;
		if (revaluationResponse.getRevaluations().size() != 0)
			revaluation = revaluationResponse.getRevaluations().get(0);

		return revaluation;
	}

	// TODO
	public void getAssetDepreciation(final Long assetId, final String tenantId) {

	}

	public AssetCurrentValueResponse getResponse(final BigDecimal currentValue, final String tenantId,
			final Long assetId) {

		final AssetCurrentValue assetCurrentValue = new AssetCurrentValue(tenantId, assetId, currentValue);
		final AssetCurrentValueResponse assetCurrentValueResponse = new AssetCurrentValueResponse();

		assetCurrentValueResponse.setAssetCurrentValue(assetCurrentValue);

		return assetCurrentValueResponse;
	}

	public AuditDetails getAuditDetails(final RequestInfo requestInfo) {
		final String userId = requestInfo.getUserInfo().getId().toString();
		final Long currEpochDate = new Date().getTime();

		final AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(userId);
		auditDetails.setCreatedDate(currEpochDate);
		auditDetails.setLastModifiedBy(userId);
		auditDetails.setLastModifiedDate(currEpochDate);

		return auditDetails;
	}

}
