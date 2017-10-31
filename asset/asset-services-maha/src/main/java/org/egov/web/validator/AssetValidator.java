package org.egov.web.validator;

import org.egov.contract.AssetRequest;
import org.egov.model.Asset;
import org.egov.model.DefectLiability;
import org.egov.model.Location;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AssetValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		
	}
	
	public void addMissingPathForPersister(AssetRequest assetRequest) {
		
		Asset asset = assetRequest.getAsset();
		
		if(asset.getDefectLiabilityPeriod() == null)
			asset.setDefectLiabilityPeriod(new DefectLiability());
		if(asset.getLocationDetails() == null)
			asset.setLocationDetails(new Location());
		
		
	}

}
