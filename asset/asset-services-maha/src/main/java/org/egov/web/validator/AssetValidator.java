package org.egov.web.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.AssetRequest;
import org.egov.contract.DisposalRequest;
import org.egov.contract.RevaluationRequest;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.DefectLiability;
import org.egov.model.Department;
import org.egov.model.FundSource;
import org.egov.model.Location;
import org.egov.model.enums.AssetCategoryType;
import org.egov.service.AssetService;
import org.egov.service.MasterDataService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

	Map<String, String> errorMap = new HashMap<>();

	@Override
	public boolean supports(Class<?> clazz) {
		return AssetRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {

	}

	public void validateAsset(AssetRequest assetRequest) {

		Asset asset = assetRequest.getAsset();
		AssetCategory assetCategory = asset.getAssetCategory();

		addMissingPathForPersister(asset);
		validateAndEnrichStateWideMasters(assetRequest);
		if(!asset.getAssetCategory().getAssetCategoryType().equals(AssetCategoryType.LAND))
		validateAnticipatedLife(asset.getAnticipatedLife(),assetCategory.getDepreciationRate());
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

	private void validateAndEnrichStateWideMasters(AssetRequest assetRequest) {

		Asset asset = assetRequest.getAsset();
		List<Asset> assets = new ArrayList<>();
		assets.add(asset);

		Map<String, Map<String, JSONArray>> ResultDataMap = mDService.getStateWideMastersByListParams(assets,
				assetRequest.getRequestInfo(), asset.getTenantId());
		
		System.err.println("the result : "+ResultDataMap);
		Map<Long, AssetCategory> assetCatMap = mDService.getAssetCategoryMapFromJSONArray(ResultDataMap.get("ASSET").get("AssetCategory"));
		Map<String, FundSource> fundMap = mDService.getFundSourceMapFromJSONArray(ResultDataMap.get("egf-master").get("funds"));
		Map<String, Department> departmentMap = mDService.getDepartmentMapFromJSONArray(ResultDataMap.get("common-masters").get("Depratment"));

		
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

	private void validateAnticipatedLife(Long anticipatedLife, Double depreciationRate) {

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
	}

	public void assetIdValidation(Long assetId, String tenantId, RequestInfo requestInfo) {
		Asset asset = assetService.getAsset(tenantId, assetId, requestInfo);
		
		if (asset == null)
			errorMap.put("egasset_AssetId", "The given Assetid is Invalid");
		
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);
	}

	public void validateForRevaluation(RevaluationRequest revaluationRequest) {
		assetIdValidation(revaluationRequest.getRevaluation().getAssetId(),
				revaluationRequest.getRevaluation().getTenantId(), revaluationRequest.getRequestInfo());
		Asset asset = assetService.getAsset(revaluationRequest.getRevaluation().getTenantId(),
				revaluationRequest.getRevaluation().getAssetId(), revaluationRequest.getRequestInfo());
		if (asset.getAssetAccount() == null || asset.getAssetAccount().isEmpty()
				|| asset.getRevaluationReserveAccount() == null || asset.getRevaluationReserveAccount().isEmpty()
				|| asset.getAccumulatedDepreciationAccount() == null
				|| asset.getAccumulatedDepreciationAccount().isEmpty()
				|| asset.getDepreciationExpenseAccount().isEmpty() || asset.getDepreciationExpenseAccount() == null)
			errorMap.put("Revaluation", "Invalid Account details");
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

	}

	public void validateForDisposal(DisposalRequest disposalequest) {
		assetIdValidation(disposalequest.getDisposal().getAssetId(), disposalequest.getDisposal().getTenantId(),
				disposalequest.getRequestInfo());
		Asset asset = assetService.getAsset(disposalequest.getDisposal().getTenantId(),
				disposalequest.getDisposal().getAssetId(), disposalequest.getRequestInfo());
		if (asset.getAssetAccount() == null || asset.getAssetAccount().isEmpty()
				|| asset.getRevaluationReserveAccount() == null || asset.getRevaluationReserveAccount().isEmpty()
				|| asset.getAccumulatedDepreciationAccount() == null
				|| asset.getAccumulatedDepreciationAccount().isEmpty()
				|| asset.getDepreciationExpenseAccount().isEmpty() || asset.getDepreciationExpenseAccount() == null)
			errorMap.put("Disposal", "Invalid Account details");
		if (!errorMap.isEmpty())
			throw new CustomException(errorMap);

	}
}
