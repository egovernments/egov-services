package org.egov.asset.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.egov.asset.config.ApplicationProperties;
import org.egov.asset.contract.AssetCurrentValueRequest;
import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.AuditDetails;
import org.egov.asset.repository.CurrentValueRepository;
import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrentValueService {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private CurrentValueRepository currentValueRepository;

	@Autowired
	private SequenceGenService sequenceGenService;

	@Autowired
	private ApplicationProperties applicationProperties;

	public AssetCurrentValueResponse getCurrentValues(final Set<Long> assetIds, final String tenantId,
			final RequestInfo requestInfo) {

		return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
				currentValueRepository.getCurrentValues(assetIds, tenantId));
	}

	public AssetCurrentValueResponse createCurrentValueAsync(AssetCurrentValueRequest assetCurrentValueRequest) {

		RequestInfo requestInfo = assetCurrentValueRequest.getRequestInfo();
		List<AssetCurrentValue> assetCurrentValues = assetCurrentValueRequest.getAssetCurrentValues();
		AuditDetails auditDetails = getAuditDetails(requestInfo);

		List<Long> idList = sequenceGenService.getIds(assetCurrentValues.size(),
				applicationProperties.getCurrentValueServiceSequence());
		int i = 0;
		for (AssetCurrentValue assetCurrentValue : assetCurrentValues) {
			assetCurrentValue.setAuditDetails(auditDetails);
			assetCurrentValue.setId(idList.get(i++));
		}
		saveCurrentValue(assetCurrentValueRequest);
		// TODO kafka send message and remove save method call
		return new AssetCurrentValueResponse(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo),
				assetCurrentValues);
	}

	public void saveCurrentValue(AssetCurrentValueRequest assetCurrentValueRequest) {

		currentValueRepository.create(assetCurrentValueRequest.getAssetCurrentValues());
	}

	public void getAssetDepreciation(final Long assetId, final String tenantId) {
		// TODO
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

	/*
	 * public AssetCurrentValueResponse getCurrentAmount(final Long assetId,
	 * final String tenantId, final RequestInfo requestInfo) {
	 * 
	 * BigDecimal currentValue = null; final Asset asset = getAsset(assetId,
	 * tenantId, requestInfo); final Revaluation revaluation =
	 * getRevaluateAsset(assetId, tenantId); currentValue =
	 * asset.getGrossValue();
	 * 
	 * if (revaluation != null) { currentValue =
	 * revaluation.getCurrentCapitalizedValue(); if
	 * (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.
	 * INCREASED.toString())) currentValue =
	 * currentValue.add(revaluation.getRevaluationAmount()); else if
	 * (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.
	 * DECREASED.toString())) currentValue =
	 * currentValue.subtract(revaluation.getRevaluationAmount()); }
	 * 
	 * return getResponse(currentValue, tenantId, assetId); }
	 */

	/*
	 * public Revaluation getRevaluateAsset(final Long assetId, final String
	 * tenantId) {
	 * 
	 * log.info("AssetCurrentAmountService getRevaluateAsset");
	 * 
	 * final List<Long> assetIds = new ArrayList<>(); assetIds.add(assetId);
	 * 
	 * final RevaluationCriteria revaluationCriteria =
	 * RevaluationCriteria.builder().assetId(assetIds)
	 * .tenantId(tenantId).status("ACTIVE").build();
	 * 
	 * final RevaluationResponse revaluationResponse =
	 * revaluationService.search(revaluationCriteria);
	 * 
	 * Revaluation revaluation = null; if
	 * (!revaluationResponse.getRevaluations().isEmpty()) revaluation =
	 * revaluationResponse.getRevaluations().get(0);
	 * 
	 * return revaluation; }
	 */

	/*
	 * public Asset getAsset(final Long assetId, final String tenantId, final
	 * RequestInfo requestInfo) {
	 * 
	 * log.info("AssetCurrentAmountService getAsset");
	 * 
	 * final AssetCriteria assetCriteria = new AssetCriteria(); final List<Long>
	 * assetIds = new ArrayList<>(); assetIds.add(assetId);
	 * assetCriteria.setId(assetIds); assetCriteria.setTenantId(tenantId); final
	 * AssetResponse assetResponse = assetService.getAssets(assetCriteria,
	 * requestInfo);
	 * 
	 * Asset asset = null; if (assetResponse.getAssets().size() != 0) asset =
	 * assetResponse.getAssets().get(0);
	 * 
	 * if (asset == null) throw new RuntimeException("Invalid Asset");
	 * 
	 * return asset; }
	 */
}
