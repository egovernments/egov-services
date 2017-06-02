package org.egov.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.asset.contract.AssetCurrentValueResponse;
import org.egov.asset.contract.AssetResponse;
import org.egov.asset.contract.RevaluationResponse;
import org.egov.asset.model.Asset;
import org.egov.asset.model.AssetCriteria;
import org.egov.asset.model.AssetCurrentValue;
import org.egov.asset.model.Revaluation;
import org.egov.asset.model.RevaluationCriteria;
import org.egov.asset.model.enums.RevaluationStatus;
import org.egov.asset.model.enums.TypeOfChangeEnum;
import org.egov.asset.web.controller.AssetController;
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

	
	public AssetCurrentValueResponse getCurrentAmount(Long assetId,String tenantId) {
		
		Double currentValue = null;
		Asset asset = getAsset(assetId, tenantId);
		Revaluation revaluation= getRevaluateAsset(assetId, tenantId);
		currentValue = asset.getGrossValue();
		
		if(revaluation!=null){
			currentValue = revaluation.getCurrentCapitalizedValue();
			if(revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.INCREASED.toString())){
				currentValue = currentValue + revaluation.getRevaluationAmount();
			} else if (revaluation.getTypeOfChange().toString().equals(TypeOfChangeEnum.DECREASED.toString())){
				currentValue = currentValue - revaluation.getRevaluationAmount();
			}
		}
		
		return getResponse(currentValue, tenantId, assetId);
	}
	
	public Asset getAsset(Long assetId,String tenantId){
		
		logger.info("AssetCurrentAmountService getAsset");
		
		AssetCriteria assetCriteria = new AssetCriteria();
		List<Long> assetIds= new ArrayList<Long>();
		assetIds.add(assetId);
		assetCriteria.setId(assetIds);
		assetCriteria.setTenantId(tenantId);
		AssetResponse assetResponse = assetService.getAssets(assetCriteria); 
		
		Asset asset = null;
		if(assetResponse.getAssets().size()!=0)
			asset = assetResponse.getAssets().get(0);
		
		if(asset == null)
			throw new RuntimeException("Invalid Asset");
		
		return asset;
	}
	
	public Revaluation getRevaluateAsset(Long assetId,String tenantId){

		logger.info("AssetCurrentAmountService getRevaluateAsset");
		
		
		List<Long> assetIds= new ArrayList<Long>();
		assetIds.add(assetId);
		RevaluationCriteria revaluationCriteria = RevaluationCriteria.builder().
				assetId(assetIds).tenantId(tenantId).status(RevaluationStatus.ACTIVE).build();
		
		RevaluationResponse revaluationResponse = revaluationService.search(revaluationCriteria);
		
		Revaluation	revaluation = null;
		if(revaluationResponse.getRevaluations().size()!=0)
		revaluation = revaluationResponse.getRevaluations().get(0);
		
		return revaluation;
	}
		
	//TODO
	public void getAssetDepreciation(Long assetId,String tenantId){
		
	
	}
	
	public AssetCurrentValueResponse getResponse(final Double currentValue, String tenantId, Long assetId) {
		
		AssetCurrentValue assetCurrentValue = new AssetCurrentValue(tenantId,assetId,currentValue);
		AssetCurrentValueResponse assetCurrentValueResponse = 
				new AssetCurrentValueResponse();
		
		assetCurrentValueResponse.setAssetCurrentValue(assetCurrentValue);
		
		return assetCurrentValueResponse;
	}
	
	

}
