package org.egov.web.validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.egov.common.contract.request.RequestInfo;
import org.egov.contract.AssetRequest;
import org.egov.contract.DisposalRequest;
import org.egov.contract.RevaluationRequest;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.DefectLiability;
import org.egov.model.Location;
import org.egov.service.AssetService;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DisposableSqlTypeValue;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AssetValidator implements Validator {
	
	@Autowired
	private  AssetService assetService;
	Map<String, String> errorMap = new HashMap<>();

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
		validateAnticipatedLife(asset.getAnticipatedLife(),asset.getOriginalValue(),assetCategory.getDepreciationRate(),errorMap);
		/*assetAccountValidate(asset, errorMap);*/
        
		if(asset.getWarrantyAvailable()) {
			if(asset.getWarrantyExpiryDate() == null)
				errorMap.put("Asset_warranty", "warrantyExpiryDate is Mandatory if Warranty is available");
			else if(asset.getWarrantyExpiryDate().compareTo(asset.getDateOfCreation()) <= 0)
				errorMap.put("Asset_warranty", "warrantyExpiryDate should be greater than asset date");
		}
			
		if(!errorMap.isEmpty())
		throw new CustomException(errorMap);
	}
	
	

	private void validateAnticipatedLife(Long anticipatedLife, BigDecimal originalValue, Double depreciationRate,
			Map<String, String> errorMap) {

		  long newVal = new Double(Math.round(100/depreciationRate)).longValue();
		  System.err.println("newVal"+newVal);
		  if (!anticipatedLife.equals(newVal))
		  errorMap.put("Asset_anticipatedLife", "anticipatedLife Value is wrong");
	}

	public void addMissingPathForPersister(Asset asset) {

		if (asset.getDefectLiabilityPeriod() == null)
			asset.setDefectLiabilityPeriod(new DefectLiability());
		if (asset.getLocationDetails() == null)
			asset.setLocationDetails(new Location());

	}
	
	public void assetIdValidation(Long assetId,String tenantId,RequestInfo requestInfo) {
		Asset asset = assetService.getAsset(tenantId, assetId, requestInfo);
		if(asset==null)
		 errorMap.put("AssetId ", "Invalid Assetid");
		 if(!errorMap.isEmpty()) 
				throw new CustomException(errorMap);
		}
	
	
	public void validateForRevaluation(RevaluationRequest revaluationRequest ) {
		 assetIdValidation(revaluationRequest.getRevaluation().getAssetId(),revaluationRequest.getRevaluation().getTenantId(),revaluationRequest.getRequestInfo());
		 Asset asset = assetService.getAsset(revaluationRequest.getRevaluation().getTenantId(), revaluationRequest.getRevaluation().getAssetId(), revaluationRequest.getRequestInfo());
		  if (asset.getAssetAccount()==null||asset.getAssetAccount().isEmpty()|| asset.getRevaluationReserveAccount()==null||asset.getRevaluationReserveAccount().isEmpty()||asset.getAccumulatedDepreciationAccount()==null||asset.getAccumulatedDepreciationAccount().isEmpty()||asset.getDepreciationExpenseAccount().isEmpty()|| asset.getDepreciationExpenseAccount()==null)
			   errorMap.put("Revaluation", "Invalid Account details");
		  if(!errorMap.isEmpty())
				throw new CustomException(errorMap);
			 
	}
	
	public void validateForDisposal( DisposalRequest disposalequest) {
		 assetIdValidation(disposalequest.getDisposal().getAssetId(),disposalequest.getDisposal().getTenantId(),disposalequest.getRequestInfo());
		 Asset asset = assetService.getAsset(disposalequest.getDisposal().getTenantId(), disposalequest.getDisposal().getAssetId(), disposalequest.getRequestInfo());
		 if (asset.getAssetAccount()==null||asset.getAssetAccount().isEmpty()|| asset.getRevaluationReserveAccount()==null||asset.getRevaluationReserveAccount().isEmpty()||asset.getAccumulatedDepreciationAccount()==null||asset.getAccumulatedDepreciationAccount().isEmpty()||asset.getDepreciationExpenseAccount().isEmpty()|| asset.getDepreciationExpenseAccount()==null)
			   errorMap.put("Disposal", "Invalid Account details");
		  if(!errorMap.isEmpty())
				throw new CustomException(errorMap);
			 
	}
}
