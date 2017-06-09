package org.egov.lams.service;

import org.egov.lams.contract.AgreementDetails;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.RequestInfo;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.Asset;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.AssetRepository;
import org.egov.lams.repository.BoundaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgreementAdaptorService {
	
	@Autowired
	private AssetRepository assetRepository;

	@Autowired
	private AllotteeRepository allotteeRepository;
	
	@Autowired
	private BoundaryRepository boundaryRepository;
	/***
	 * method to create agreementdetails object and populate values
	 * 
	 * @param agreement
	 * @return AgreementDetails
	 */
	public AgreementDetails indexOnCreate(AgreementRequest agreementRequest) {
		
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		AgreementDetails agreementDetails = new AgreementDetails();
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId(),agreement.getTenantId());
		Allottee allottee = allotteeRepository.getAllottee(agreement.getAllottee().getId(),agreement.getTenantId(),requestInfo);
		
		agreementDetails.setAsset(asset);
		agreementDetails.setAgreement(agreement);
		agreementDetails.setAllottee(allottee);
		agreementDetails.setBoundaryDetails(asset.getLocationDetails(), boundaryRepository.getBoundariesById(agreement,asset));
		
		//boundaryRepository.getBoundary(); FIXME make call to city controller
		return agreementDetails;
	}
}
