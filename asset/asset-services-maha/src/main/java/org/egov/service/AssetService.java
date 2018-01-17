package org.egov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.config.ApplicationProperties;
import org.egov.contract.AssetCurrentValueRequest;
import org.egov.contract.AssetCurrentValueResponse;
import org.egov.contract.AssetRequest;
import org.egov.contract.AssetResponse;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.CurrentValue;
import org.egov.model.Department;
import org.egov.model.FundSource;
import org.egov.model.LandDetail;
import org.egov.model.ModeOfAcquisition;
import org.egov.model.TransactionHistory;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.criteria.RevaluationCriteria;
import org.egov.model.enums.KafkaTopicName;
import org.egov.model.enums.Sequence;
import org.egov.model.enums.Status;
import org.egov.model.enums.TransactionType;
import org.egov.repository.AssetRepository;
import org.egov.repository.MasterDataRepository;
import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.egov.tracer.model.CustomException;
import org.egov.web.wrapperfactory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

	@Autowired
	private MasterDataRepository mDRepo;

	@Autowired
	private RevaluationService revaluationService;

	public AssetResponse createAsync(final AssetRequest assetRequest) {
		final Asset asset = assetRequest.getAsset();
		 List<LandDetail> landDetails=asset.getLandDetails();
		
		if(landDetails!=null) {
		for (LandDetail landDetail : landDetails) {
			landDetail.setId(assetCommonService.getNextId(Sequence.LANDDETAILSSEQUENCE));
			System.err.println(landDetail);
		}
		}
		
		System.err.println("assetservice landdetails id set"+asset);
		

		// FIXME put asset code seq per ulb Ghanshyam will update
		asset.setCode(asset.getTenantId() + "/" + asset.getDepartment().getCode() + "/"
				+ asset.getAssetCategory().getCode() + "/" + assetCommonService.getCode(Sequence.ASSETCODESEQUENCE));
		log.info("asset.getcode" + asset.getCode());

		asset.setId(assetCommonService.getNextId(Sequence.ASSETSEQUENCE));
		

		asset.setStatus(Status.CAPITALIZED.toString());

		asset.setAuditDetails(assetCommonService.getAuditDetails(assetRequest.getRequestInfo()));
		logAwareKafkaTemplate.send(appProps.getCreateAssetTopicNameTemp(), assetRequest);

		CurrentValue currentValue = CurrentValue.builder().assetId(asset.getId()).tenantId(asset.getTenantId())
				.transactionDate(asset.getOpeningDate()).assetTranType(TransactionType.CREATE).build();

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
		currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder()
				.assetCurrentValue(assetCurrentValueList).requestInfo(assetRequest.getRequestInfo()).build());

		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse updateAsync(final AssetRequest assetRequest) {
		Map<String, String> errorMap = new HashMap<>();
		final Asset asset = assetRequest.getAsset();
		Set<Long> assetIds=new HashSet<>();
		assetIds.add(asset.getId());

		final List<LandDetail> landDetails=asset.getLandDetails();
		if(landDetails!=null) {
			
		for (LandDetail landDetail : landDetails) {
			if(landDetail.getId()==null)
			landDetail.setId(assetCommonService.getNextId(Sequence.LANDDETAILSSEQUENCE));
			System.err.println(landDetail);
		}
		}
		CurrentValue currentValue =null;
	    List<CurrentValue> assetCurrentValueList = new ArrayList<>();
		log.debug("assetRequest updateAsync::" + assetRequest);
		AssetCriteria  assetCriteria=AssetCriteria.builder().id(assetIds).tenantId(asset.getTenantId()).build();
		AssetResponse assetResponse=getAssets(assetCriteria,assetRequest.getRequestInfo());
		
		if(!(assetResponse.getAssets().get(0).getGrossValue().equals(asset.getGrossValue())))
		     {
		 System.err.println("if gross");
		List<Long> assetCurrentvalue = currentValueService.getNonTransactedCurrentValues(assetIds, asset.getTenantId(), assetRequest.getRequestInfo());
		if(assetCurrentvalue.contains(asset.getId()))
		      {
			
			currentValue = CurrentValue.builder().assetId(asset.getId()).tenantId(asset.getTenantId()).transactionDate(asset.getOpeningDate()).assetTranType(TransactionType.CREATE).currentAmount(asset.getGrossValue()).build();
    		assetCurrentValueList.add(currentValue);
            logAwareKafkaTemplate.send(appProps.getUpdateAssetTopicName(), KafkaTopicName.UPDATEASSET.toString(),
    				assetRequest);
            currentValueService.createCurrentValueAsync(AssetCurrentValueRequest.builder().assetCurrentValue(assetCurrentValueList).requestInfo(assetRequest.getRequestInfo()).build());
			
			
		      }else 
			     errorMap.put("Asset_Grossvalue", "Grossvalue cannot be updated for assets whose transactions are already done");
		         if (!errorMap.isEmpty())
			     throw new CustomException(errorMap);
		}else {
			
			   System.err.println("assetResponse.getAssets().get(0).getGrossValue()"+assetResponse.getAssets().get(0).getGrossValue());
			
		       System.err.println("else for gross"+(assetResponse.getAssets().get(0).getGrossValue().equals(asset.getGrossValue())));
			   logAwareKafkaTemplate.send(appProps.getUpdateAssetTopicName(), KafkaTopicName.UPDATEASSET.toString(),
    				assetRequest);
    	}
			
		final List<Asset> assets = new ArrayList<>();
		assets.add(asset);
		return getAssetResponse(assets, assetRequest.getRequestInfo());
	}

	public AssetResponse getAssets(final AssetCriteria searchAsset, final RequestInfo requestInfo) {
		
		enrichParentCategory(searchAsset,requestInfo);
		final List<Asset> assets = assetRepository.findForCriteria(searchAsset);
		if(searchAsset.getIsTransactionHistoryRequired()==null)
			searchAsset.setIsTransactionHistoryRequired(false);
		else if (searchAsset.getIsTransactionHistoryRequired().equals(true) && !assets.isEmpty()) 
			enrichTransactionHistory(assets);
		

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

		Map<String, Map<String, JSONArray>> rsMasterMap = mDService.getStateWideMastersByListParams(assets, requestInfo,
				tenantId);
		Map<String, JSONArray> rsAssetMap = rsMasterMap.get("ASSET");
		Map<Long, AssetCategory> assetCatMap = mDService
				.getAssetCategoryMapFromJSONArray(rsAssetMap.get("AssetCategory"));
		Map<String, FundSource> fundMap = mDService
				.getFundSourceMapFromJSONArray(rsMasterMap.get("egf-master").get("Fund"));

		Map<String, Department> departmentMap = mDService
				.getDepartmentMapFromJSONArray(rsMasterMap.get("common-masters").get("Department"));
		Map<String, ModeOfAcquisition> modeOfAquisitionMap = mDService
				.getModeOfAcquisitionMapFromJSONArray(rsAssetMap.get("ModeOfAcquisition"));

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

	private void enrichParentCategory(AssetCriteria searchAsset, RequestInfo requestInfo) {

		if ((searchAsset.getAssetSubCategory() == null || CollectionUtils.isEmpty(searchAsset.getAssetSubCategory())) &&
				(searchAsset.getAssetCategory() != null || !CollectionUtils.isEmpty(searchAsset.getAssetCategory()))) {

			Map<String, String> paramsMap = new HashMap<>();
			Map<String, Map<String, String>> masterMap = new HashMap<>();
			Map<String, Map<String, Map<String, String>>> moduleMap = new HashMap<>();
			paramsMap.put("parent", assetCommonService.getIdQuery(searchAsset.getAssetCategory()));
			masterMap.put("AssetCategory", paramsMap);
			moduleMap.put("ASSET", masterMap);

			String tenantId = searchAsset.getTenantId();
			if (!tenantId.equals("default"))
				tenantId = tenantId.split("\\.")[0];

			JSONArray jsonArray = mDRepo.getMastersByListParams(moduleMap, requestInfo, tenantId).get("ASSET")
					.get("AssetCategory");
			Map<Long, AssetCategory> asCatMap = mDService.getAssetCategoryMapFromJSONArray(jsonArray);
			if (searchAsset.getAssetSubCategory() == null)
				searchAsset.setAssetSubCategory(asCatMap.keySet());
			else
				searchAsset.getAssetSubCategory().addAll(asCatMap.keySet());
		}
	}

	private void enrichTransactionHistory(List<Asset> assets) {
       Set<Long> assetIds=assets.stream().map(a -> a.getId()).collect(Collectors.toSet());
		Map<Long, List<TransactionHistory>> transactionhistoryMap = assetRepository.getTransactionHistory(assetIds,assets.get(0).getTenantId());
		assets.forEach( a -> a.setTransactionHistory(transactionhistoryMap.get(a.getId())));
	}
}