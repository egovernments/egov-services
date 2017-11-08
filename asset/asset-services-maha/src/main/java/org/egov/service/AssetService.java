package org.egov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.TransactionType;
import org.egov.repository.AssetRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetService {

	@Autowired
	private LogAwareKafkaTemplate<String, Object> logAwareKafkaTemplate;

	@Autowired
	private ApplicationProperties appProps;

	@Autowired
	private AssetCommonService assetCommonService;

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private CurrentValueService currentValueService;

	public AssetResponse createAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();

		// FIXME put asset code seq per ulb Ghanshyam will update
		asset.setCode(asset.getTenantId() + asset.getDepartmentCode() + asset.getAssetCategory().getCode()
				+ assetCommonService.getCode(Sequence.ASSETCODESEQUENCE));

		asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));

		log.debug("assetRequest createAsync::" + assetRequest);
		asset.setAuditDetails(assetCommonService.getAuditDetails(assetRequest.getRequestInfo()));

		logAwareKafkaTemplate.send(appProps.getCreateAssetTopicNameTemp(), assetRequest);
		CurrentValue currentValue = new CurrentValue();
		currentValue.setId(new Long(assetCommonService.getCode(Sequence.CURRENTVALUESEQUENCE)));
		currentValue.setAssetId(asset.getId());
		currentValue.setAssetTranType(TransactionType.CREATE);
		currentValue.setTenantId(asset.getTenantId());
		currentValue.setAuditDetails(assetCommonService.getAuditDetails(assetRequest.getRequestInfo()));
		AssetCurrentValueRequest assetCurrentValueRequest = new AssetCurrentValueRequest();
		List<CurrentValue> assetCurrentValueList = new ArrayList<>();
		BigDecimal grossValue = asset.getGrossValue();
		BigDecimal accumulatedDepreciation = asset.getAccumulatedDepreciation();
		if (grossValue != null && accumulatedDepreciation != null)
			currentValue.setCurrentAmount(grossValue.subtract(accumulatedDepreciation));
		else
			currentValue.setCurrentAmount(asset.getOriginalValue());

		assetCurrentValueList.add(currentValue);
		assetCurrentValueRequest.setAssetCurrentValue(assetCurrentValueList);
		assetCurrentValueRequest.setRequestInfo(assetRequest.getRequestInfo());
		logAwareKafkaTemplate.send(appProps.getSaveCurrentvalueTopic(), assetCurrentValueRequest);

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		log.debug("assetRequest updateAsync::" + assetRequest);
		logAwareKafkaTemplate.send(appProps.getUpdateAssetTopicName(), KafkaTopicName.UPDATEASSET.toString(),
				assetRequest);
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		log.info("AssetService getAssets");
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		if (!assets.isEmpty())
			mapAssetCategories(assets, requestInfo, searchAsset.getTenantId());
		return getAssetResponse(assets, requestInfo);
	}

	private AssetResponse getAssetResponse(final List<Asset> assets, final RequestInfo requestInfo) {
		final AssetResponse assetResponse = new AssetResponse();
		assetResponse.setAssets(assets);
		assetResponse.setResposneInfo(responseInfoFactory.createResponseInfoFromRequestHeaders(requestInfo));
		return assetResponse;
	}

	public Asset getAsset(final String tenantId, final Long assetId, final RequestInfo requestInfo) {
		final Set<Long> assetIds = new HashSet<>();
		assetIds.add(assetId);
		final AssetCriteria assetCriteria = AssetCriteria.builder().tenantId(tenantId).id(assetIds).build();
		final List<Asset> assets = getAssets(assetCriteria, requestInfo).getAssets();
		if (assets != null && !assets.isEmpty())
			return assets.get(0);
		else
			throw new RuntimeException(
					"There is no asset exists for id ::" + assetId + " for tenant id :: " + tenantId);
	}

	private void mapAssetCategories(List<Asset> assets, RequestInfo requestInfo, String tenantId) {

		Set<Long> idSet = assets.stream().map(asset -> asset.getAssetCategory().getId()).collect(Collectors.toSet());

		Map<Long, AssetCategory> assetCatMap = masterDataService.getAssetCategoryMap(idSet, requestInfo, tenantId);

		assets.forEach(asset -> {
			Long key = asset.getAssetCategory().getId();
			asset.setAssetCategory(assetCatMap.get(key));
		});
	}

	private static String getIdQuery(final Set<Long> idSet) {
		StringBuilder query = null;
		Long[] arr = new Long[idSet.size()];
		arr = idSet.toArray(arr);
		query = new StringBuilder(arr[0].toString());
		for (int i = 1; i < arr.length; i++)
			query.append("," + arr[i]);
		return query.toString();
	}

}
