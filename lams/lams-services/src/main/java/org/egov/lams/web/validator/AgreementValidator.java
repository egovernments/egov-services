package org.egov.lams.web.validator;

import java.util.Date;
import java.util.List;

import org.egov.lams.config.PropertiesManager;
import org.egov.lams.exception.LamsException;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.service.AllotteeService;
import org.egov.lams.service.AssetService;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.service.RentIncrementService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgreementValidator {

	@Autowired
	private AssetService assetService;

	@Autowired
	private AllotteeService allotteeService;
	
	@Autowired
	private RentIncrementService rentIncrementService;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private LamsConfigurationService lamsConfigurationService;
	

	public void validateAgreement(AgreementRequest agreementRequest) {

		Agreement agreement = agreementRequest.getAgreement();

		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();

		// TODO remove hard coded value of rent*3
		if (securityDeposit<rent * 3)
			throw new LamsException("security deposit value should be greater than or equal to thrice rent value");

		if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			throw new LamsException("solvency certificate date should be lesser than current date");

		if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			throw new LamsException("bank Guarantee Date date should be lesser than current date");

		// FIXME uncomment this part before pushing-->
		validateAllottee(agreementRequest);
		validateAsset(agreementRequest);
		validateRentIncrementType(agreement);
	}

	public void validateAsset(AgreementRequest agreementRequest) {

		Long assetId = agreementRequest.getAgreement().getAsset().getId();
		String queryString = "id=" + assetId;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		AssetResponse assetResponse = assetService.getAssets(queryString,requestInfoWrapper);
		if (assetResponse.getAssets() == null || assetResponse.getAssets().size() == 0) 
			throw new LamsException("the asset object does not exist");
			
		if(!assetService.isAssetAvailable(assetId))
				throw new LamsException("Agreement has been already signed for the particular asset");
		// FIXME use invalidDataException here
	}

	public void validateAllottee(AgreementRequest agreementRequest) {

		Allottee allottee = agreementRequest.getAgreement().getAllottee();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		AllotteeResponse allotteeResponse = allotteeService.isAllotteeExist(allottee, requestInfo);
		if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().size() == 0)
			allotteeService.createAllottee(allottee, requestInfo);
		else 
			allottee.setId(allotteeResponse.getAllottee().get(0).getId());
	}

	public void validateRentIncrementType(Agreement agreement) {

		RentIncrementType rentIncrement = agreement.getRentIncrementMethod();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		String keyName = propertiesManager.getRentIncrementAssetCategoryKey();
		lamsConfigurationGetRequest.setName(keyName);
		List<String> assetCategoryNames = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(keyName);

		for (String string : assetCategoryNames) {
			if (string.equals(assetCategory.getName())) {
				if (rentIncrement != null) {
					Long rentIncrementId = rentIncrement.getId();
					RentIncrementType responseRentIncrement = rentIncrementService
							.getRentIncrementById(rentIncrementId);
					if (!responseRentIncrement.getId().equals(rentIncrement.getId()))
						throw new RuntimeException("invalid rentincrement type object");
				} else {
					throw new RuntimeException("please enter a rentincrement type value for given agreement");
				}
			}
		}

		if (rentIncrement == null) {
			rentIncrement = new RentIncrementType();
			rentIncrement.setId(null);
		}
	}
}
