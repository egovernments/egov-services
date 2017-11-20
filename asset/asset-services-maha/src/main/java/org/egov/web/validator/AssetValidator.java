package org.egov.web.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.contract.AssetRequest;
import org.egov.contract.DisposalRequest;
import org.egov.contract.RevaluationRequest;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.DefectLiability;
import org.egov.model.Department;
import org.egov.model.Disposal;
import org.egov.model.FundSource;
import org.egov.model.Location;
import org.egov.model.Revaluation;
import org.egov.model.criteria.AssetCriteria;
import org.egov.model.enums.AssetCategoryType;
import org.egov.model.enums.TypeOfChange;
import org.egov.service.AssetService;
import org.egov.service.MasterDataService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;

@Component
@Slf4j
public class AssetValidator implements Validator {

	@Autowired
	private MasterDataService mDService;

	@Autowired
	private AssetService assetService;

	@Override
	public boolean supports(Class<?> clazz) {
		return AssetRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

	}

	public void validateAsset(AssetRequest assetRequest) {
		
		Map<String, String> errorMap = new HashMap<>();

		Asset asset = assetRequest.getAsset();
		AssetCategory assetCategory = asset.getAssetCategory();

		addMissingPathForPersister(asset);
		validateAndEnrichStateWideMasters(assetRequest,errorMap);
		if(!asset.getAssetCategory().getAssetCategoryType().equals(AssetCategoryType.LAND))
		validateAnticipatedLife(asset.getAnticipatedLife(),assetCategory.getDepreciationRate(),errorMap);
		else
			asset.setAnticipatedLife(null);
		/* assetAccountValidate(asset, errorMap); */

		if (asset.getWarrantyAvailable()) {
			if (asset.getWarrantyExpiryDate() == null)
				errorMap.put("Asset_warranty", "warrantyExpiryDate is Mandatory if Warranty is available");
			else if (asset.getWarrantyExpiryDate().compareTo(asset.getDateOfCreation()) <= 0)
				errorMap.put("Asset_warranty", "warrantyExpiryDate should be greater than asset date");
		}

		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	private void validateAndEnrichStateWideMasters(AssetRequest assetRequest, Map<String, String> errorMap) {

		Asset asset = assetRequest.getAsset();
		List<Asset> assets = new ArrayList<>();
		assets.add(asset);

		Map<String, Map<String, JSONArray>> ResultDataMap = mDService.getStateWideMastersByListParams(assets,
				assetRequest.getRequestInfo(), asset.getTenantId());
		
		System.err.println("the result : "+ResultDataMap);
		Map<Long, AssetCategory> assetCatMap = mDService.getAssetCategoryMapFromJSONArray(ResultDataMap.get("ASSET").get("AssetCategory"));
		Map<String, FundSource> fundMap = mDService.getFundSourceMapFromJSONArray(ResultDataMap.get("egf-master").get("funds"));
		Map<String, Department> departmentMap = mDService.getDepartmentMapFromJSONArray(ResultDataMap.get("common-masters").get("Department"));

		
		AssetCategory masterAssetCat = assetCatMap.get(asset.getAssetCategory().getId());
		if (masterAssetCat == null)
			errorMap.put("EGASSET_INVALID_ASSETCATEGORY", "the given AssetCategory Id is Invalid");
		else
			asset.setAssetCategory(masterAssetCat);

		Department department = departmentMap.get(asset.getDepartment().getCode());
		if (department == null)
			errorMap.put("EGASSET_INVALID_DEPARTMENT", "the  given Department code is Invalid");
		else
			asset.setDepartment(department);

		if (asset.getFundSource().getCode() != null) {
			FundSource fundSource = fundMap.get(asset.getFundSource().getCode());
			if (fundSource == null)
				errorMap.put("EGASSET_INVALID_FUNDSOURCE", "The given FundSource code is Invalid");
			else
				asset.setFundSource(fundSource);
		}
	
		
	}

	private void validateAndEnrichUlbWideMasters(AssetRequest assetRequest) {

	}

	private void validateAnticipatedLife(Long anticipatedLife, Double depreciationRate, Map<String, String> errorMap) {

		long newVal = new Double(Math.round(100 / depreciationRate)).longValue();
		log.info("newVal : " + newVal);
		log.info("anticipated val : "+anticipatedLife);
		log.info("before the if statement : "+ (anticipatedLife-newVal));
		if (anticipatedLife-newVal != 0) {
			errorMap.put("Asset_anticipatedLife", "anticipatedLife Value is wrong");
		}
	}

	public void addMissingPathForPersister(Asset asset) {

		if (asset.getDefectLiabilityPeriod() == null)
			asset.setDefectLiabilityPeriod(new DefectLiability());
		if (asset.getLocationDetails() == null)
			asset.setLocationDetails(new Location());
		if(asset.getFundSource() == null)
			asset.setFundSource(new FundSource());
		
		//FIXME TODO remove it after ghansyam handles it in persister
		if(asset.getTitleDocumentsAvalable()==null) {
			asset.setTitleDocumentsAvalable(new ArrayList<>());
		}
	}


	public void validateForRevaluation(RevaluationRequest revaluationRequest) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		Revaluation revaluation= revaluationRequest.getRevaluation();
		Asset asset = assetService.getAsset(revaluationRequest.getRevaluation().getTenantId(),
				revaluationRequest.getRevaluation().getAssetId(), revaluationRequest.getRequestInfo());
		
		if(asset == null)
			errorMap.put("EGASSET_REVALUATION_ASSET", "Given Asset For Revaluation Cannot Be Found");
		
		if(revaluation.getRevaluationAmount()!=null && revaluation.getRevaluationAmount().longValue()<=0)
			errorMap.put("EGASSET_REVALUATION_AMOUNT", "Negative Revaluation Amount Cannot Be Accepted");
		
		if(revaluation.getRevaluationDate().compareTo(new Date().getTime()) > 0)
			errorMap.put("EGASSET_REVALUATION_DATE", "Assets Cannot be Revaluated For Future Dates");
		
		if(revaluation.getOrderDate().compareTo(new Date().getTime()) > 0)
			errorMap.put("EGASSET_REVALUATION_ORDER_DATE", "Future Dates Cannot Be Given For Order Date");
		
		if (asset != null) {
			BigDecimal positiveTransaction = asset.getCurrentValue().add(revaluation.getRevaluationAmount());
			BigDecimal negativeTransaction = asset.getCurrentValue().add(revaluation.getRevaluationAmount());

			if (revaluation.getTypeOfChange().equals(TypeOfChange.DECREASED))
				if (!(asset.getCurrentValue().subtract(revaluation.getRevaluationAmount()).doubleValue()
						- (revaluation.getValueAfterRevaluation().doubleValue()) == 0))
					errorMap.put("EGASSET_REVALUATION_VALUATION_AMOUNT", "THE Valuation Amount Calculation Is Wrong");

			if (revaluation.getTypeOfChange().equals(TypeOfChange.INCREASED))
				if (!(asset.getCurrentValue().add(revaluation.getRevaluationAmount()).doubleValue()
						- (revaluation.getValueAfterRevaluation().doubleValue()) == 0))
					errorMap.put("EGASSET_REVALUATION_VALUATION_AMOUNT", "THE Valuation Amount Calculation Is Wrong");
		}
		
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateForDisposal(DisposalRequest disposalRequest) {
		
		Map<String, String> errorMap = new HashMap<>();
		
		Disposal disposal = disposalRequest.getDisposal();
		
		Asset asset = assetService.getAsset(disposal.getTenantId(),
				disposal.getAssetId(), disposalRequest.getRequestInfo());
		
		if(asset == null)
			errorMap.put("EGASSET_DISPOSAL_ASSET", "Given Asset For Disposal Cannot Be Found");
		
		if(disposal.getSaleValue()!=null && disposal.getSaleValue().longValue()<=0)
			errorMap.put("EGASSET_DISPOSAL_AMOUNT", "Negative Sale Amount Cannot Be Accepted");
		
		if(disposal.getDisposalDate().compareTo(new Date().getTime()) > 0)
			errorMap.put("EGASSET_DISPOSAL_DATE", "Assets Cannot be Disposed/Sold For Future Dates");
		
		if(disposal.getOrderDate().compareTo(new Date().getTime()) > 0)
			errorMap.put("EGASSET_DISPOSAL_ORDER_DATE", "Future Dates Cannot Be Given For Order Date");
		
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateSearch(AssetCriteria assetCriteria) {
		
		Map<String, String> errorMap = new HashMap<>();
				
		
		if(assetCriteria.getAssetSubCategory() == null || CollectionUtils.isEmpty(assetCriteria.getAssetSubCategory())) {
			if(assetCriteria.getAssetCategory() == null || CollectionUtils.isEmpty(assetCriteria.getAssetCategory()))
				errorMap.put("EGASSET_SEARCH_ASSET_CATEGORY", "Either AssetCategory Or AssetSubCategory Has To Be Given For Search");
		}
		
		if(!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}
}