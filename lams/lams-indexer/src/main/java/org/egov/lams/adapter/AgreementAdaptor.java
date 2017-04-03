package org.egov.lams.adapter;

import org.egov.lams.contract.AgreementDetails;
import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Asset;
import org.egov.lams.service.AllotteeService;
import org.egov.lams.service.AssetService;
import org.egov.lams.service.BoundaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgreementAdaptor {
	
	@Autowired
	private AssetService assetRepository;

	@Autowired
	private AllotteeService allotteeRepository;
	
	@Autowired
	private BoundaryService boundaryRepository;
	/***
	 * method to create agreementdetails object and populate values
	 * 
	 * @param agreement
	 * @return AgreementDetails
	 */
	public AgreementDetails indexOnCreate(AgreementRequest agreementRequest) {
		
		Agreement agreement = agreementRequest.getAgreement();

		AgreementDetails agreementDetails = new AgreementDetails();
		agreementDetails.setAgreement(agreement);
		agreementDetails.setAllottee(allotteeRepository.getAllottee(agreement.getAllottee().getId()));
		Asset asset = assetRepository.getAsset(agreement.getAsset().getId());
		agreementDetails.setAsset(asset);
		
		/*List<Long> boundaryIdList = new ArrayList<>();
		boundaryIdList.add(asset.getLocality());
		boundaryIdList.add(asset.getZone());
		boundaryIdList.add(asset.getElectionward());
		boundaryIdList.add(asset.getWard());
		boundaryIdList.add(asset.getBlock());
		boundaryIdList.add(asset.getStreet());
		*/
		//getlist of boundaries using the list of id form asset object
		//boundaryRepository.getBoundary(); FIXME make call to city controller

		return agreementDetails;
	}
}
