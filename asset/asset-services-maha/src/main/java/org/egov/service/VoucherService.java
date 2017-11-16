package org.egov.service;

import org.egov.contract.DisposalRequest;
import org.egov.contract.RevaluationRequest;
import org.egov.model.Asset;
import org.egov.model.Disposal;
import org.egov.model.Revaluation;
import org.egov.web.validator.AssetValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {
	
	@Autowired
	private AssetService assetService;
	
/*	
	public String revaluationVoucherGenerator(RevaluationRequest revaluationRequest) {
		System.out.println(" VoucherService revaluationVoucherGenerator");
		
		Revaluation  revaluation=revaluationRequest.getRevaluation();
		Long assetId = revaluation.getAssetId();

		Asset asset = assetService.getAsset(revaluationRequest.getRevaluation().getTenantId(),
				revaluationRequest.getRevaluation().getAssetId(), revaluationRequest.getRequestInfo());
		if (asset.getAssetAccount() == null || asset.getRevaluationReserveAccount() == null 
				|| asset.getAccumulatedDepreciationAccount() == null
				|| asset.getDepreciationExpenseAccount() == null)
		
		
		return  "VOURE"+Math.random();
		
	}
	
	 public String disposalVoucherGenerator(DisposalRequest disposalRequest) {
		
		Disposal disposal=disposalRequest.getDisposal();
		Long assetId = disposal.getAssetId();

		String voucher= "VOUDS"+Math.random();
		return voucher;
		
	}*/

}
