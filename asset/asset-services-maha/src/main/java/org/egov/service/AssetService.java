package org.egov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.Department;
import org.egov.model.FundSource;
import org.egov.model.ModeOfAcquisition;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.Status;
import org.egov.model.enums.TransactionType;
import org.egov.repository.AssetRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

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
	private MasterDataService mDService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private CurrentValueService currentValueService;

	public AssetResponse createAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();

		// FIXME put asset code seq per ulb Ghanshyam will update
		asset.setCode(asset.getTenantId() +"/"+ asset.getDepartment().getCode() + "/"+asset.getAssetCategory().getCode()
				+"/"+ assetCommonService.getCode(Sequence.ASSETCODESEQUENCE));
		log.info("asset.getcode"+asset.getCode());

		asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));
		asset.setStatus(Status.CAPITALIZED.toString());

		asset.setAuditDetails(assetCommonService.getAuditDetails(assetRequest.getRequestInfo()));
		logAwareKafkaTemplate.send(appProps.getCreateAssetTopicNameTemp(), assetRequest);
		
		CurrentValue currentValue = CurrentValue.builder().assetId(asset.getId()).tenantId(asset.getTenantId())
				.transactionDate(asset.getDateOfCreation()).assetTranType(TransactionType.CREATE).build();
		
		BigDecimal grossValue = asset.getGrossValue();
		BigDecimal accumulatedDepreciation = asset.getAccumulatedDepreciation();
		
		// Accumulateddepreciation should be less than gross value todo in validator
		if (grossValue != null && accumulatedDepreciation != null)
			currentValue.setCurrentAmount(grossValue.subtract(accumulatedDepreciation));
		else if (grossValue != null)
			currentValue.setCurrentAmount(grossValue);
		else
			currentValue.setCurrentAmount(asset.getOriginalValue());

		List<CurrentValue> assetCurrentValueList = new ArrayList<>();
		assetCurrentValueList.add(currentValue);
		currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder().assetCurrentValue(assetCurrentValueList).requestInfo(assetRequest.getRequestInfo()).build());

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
			mapMasters(assets, requestInfo, searchAsset.getTenantId());
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
			return null;
	}

	private void mapMasters(List<Asset> assets, RequestInfo requestInfo, String tenantId) {

		Map<String, Map<String, JSONArray>> rsMasterMap = mDService.getStateWideMastersByListParams(assets,
				requestInfo, tenantId);
		Map<String,JSONArray> rsAssetMap = rsMasterMap.get("ASSET");
		Map<Long, AssetCategory> assetCatMap = mDService.getAssetCategoryMapFromJSONArray(rsAssetMap.get("AssetCategory"));
		Map<String, FundSource> fundMap = mDService.getFundSourceMapFromJSONArray(rsMasterMap.get("egf-master").get("funds"));
		
		Map<String, Department> departmentMap = mDService
				.getDepartmentMapFromJSONArray(rsMasterMap.get("common-masters").get("Department"));
		Map<String, ModeOfAcquisition> modeOfAquisitionMap = mDService.getModeOfAcquisitionMapFromJSONArray(rsAssetMap.get("ModeOfAcquisition"));

		assets.forEach(asset -> {
			Long assetCatkey = asset.getAssetCategory().getId();
			String fundKey = asset.getFundSource().getCode();
			String deptKey = asset.getDepartment().getCode();
			String mOAKey = asset.getModeOfAcquisition().getCode();

			if (assetCatMap.get(assetCatkey) != null)
				asset.setAssetCategory(assetCatMap.get(assetCatkey));
			if (fundMap.get(fundKey) != null)
				asset.setFundSource(fundMap.get(fundKey));
			if (departmentMap.get(deptKey) != null)
				asset.setDepartment(departmentMap.get(deptKey));
			if (modeOfAquisitionMap.get(mOAKey) != null)
				asset.setModeOfAcquisition(modeOfAquisitionMap.get(mOAKey));
		});
	}
}