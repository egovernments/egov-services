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
	
	@Autowired
	private AssetValidator assetValidator;
	
	public String revaluationVoucherGenerator(RevaluationRequest revaluationRequest) {
		System.out.println(" VoucherService revaluationVoucherGenerator");
		
		Revaluation  revaluation=revaluationRequest.getRevaluation();
		Long assetId = revaluation.getAssetId();
	    assetValidator.validateForRevaluation(revaluationRequest);
		String voucher= "VOURE"+Math.random();
		return voucher;
		
	}
	
	 public String disposalVoucherGenerator(DisposalRequest disposalRequest) {
		
		Disposal disposal=disposalRequest.getDisposal();
		Long assetId = disposal.getAssetId();
	    assetValidator.validateForDisposal(disposalRequest);
		String voucher= "VOUDS"+Math.random();
		return voucher;
		
	}

}
