package org.egov.web.validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.DoubleRange;
import org.egov.contract.AssetRequest;
import org.egov.model.Asset;
import org.egov.model.AssetCategory;
import org.egov.model.DefectLiability;
import org.egov.model.Location;
import org.egov.tracer.model.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AssetValidator implements Validator {

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

		  long newVal = new Double(100/depreciationRate).longValue();
		  if (!anticipatedLife.equals(newVal))
		  errorMap.put("Asset_anticipatedLife", "anticipatedLife Value is wrong");
	}

	public void addMissingPathForPersister(Asset asset) {

		if (asset.getDefectLiabilityPeriod() == null)
			asset.setDefectLiabilityPeriod(new DefectLiability());
		if (asset.getLocationDetails() == null)
			asset.setLocationDetails(new Location());

	}
}
