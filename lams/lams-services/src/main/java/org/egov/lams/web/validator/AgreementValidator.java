package org.egov.lams.web.validator;

import java.util.Date;
import java.util.List;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.AssetRepository;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AgreementValidator implements org.springframework.validation.Validator{
	
	public static final Logger logger = LoggerFactory.getLogger(AgreementValidator.class);

	@Autowired
	private AssetRepository assetService;

	@Autowired
	private AllotteeRepository allotteeService;
	
	@Autowired
	private RentIncrementRepository rentIncrementService;
	
	@Autowired
	private PropertiesManager propertiesManager;
	
	@Autowired
	private LamsConfigurationService lamsConfigurationService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return AgreementRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AgreementRequest agreementRequest = null;
		
		if(target instanceof AgreementRequest){
			 agreementRequest = (AgreementRequest)target;
		}else
			throw new RuntimeException("invalid datatype for agreement validator");
		validateAgreement(agreementRequest,errors);
	}

	public void validateAgreement(AgreementRequest agreementRequest,Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();

		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();

		// TODO remove hard coded value of rent*3
		if (securityDeposit<rent * 3)
			errors.rejectValue("Agreement.securityDeposit", "","security deposit value should be greater than or equal to thrice rent value");

		if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.solvencyCertificateDate", "","solvency certificate date should be lesser than current date");

		if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.bankGuaranteeDate", "","bank Guarantee Date date should be lesser than current date");

		// FIXME uncomment this part before pushing-->
		validateAllottee(agreementRequest,errors);
		validateAsset(agreementRequest,errors);
		validateRentIncrementType(agreement,errors);
		logger.info("after the validations");
	}

	public void validateAsset(AgreementRequest agreementRequest,Errors errors) {

		Long assetId = agreementRequest.getAgreement().getAsset().getId();
		String queryString = "id=" + assetId + "&tenantId=" + agreementRequest.getAgreement().getTenantId();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		AssetResponse assetResponse = assetService.getAssets(queryString,requestInfoWrapper);
		if (assetResponse.getAssets() == null || assetResponse.getAssets().size() == 0) 
			errors.rejectValue("Agreement.securityDeposit", "","the asset given does not exist");
			
		if(!assetService.isAssetAvailable(assetId))
			errors.rejectValue("Agreement.Asset.id", "","Agreement has been already signed for the given asset");
	}

	public void validateAllottee(AgreementRequest agreementRequest,Errors errors) {

		Allottee allottee = agreementRequest.getAgreement().getAllottee();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		allottee.setTenantId(agreementRequest.getAgreement().getTenantId());
		
		AllotteeResponse allotteeResponse = allotteeService.isAllotteeExist(allottee, requestInfo);
		if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().size() == 0){
			allotteeService.createAllottee(allottee, requestInfo);
		}
		else 
			allottee.setId(allotteeResponse.getAllottee().get(0).getId());
	}

	public void validateRentIncrementType(Agreement agreement,Errors errors) {

		RentIncrementType rentIncrement = agreement.getRentIncrementMethod();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		String keyName = propertiesManager.getRentIncrementAssetCategoryKey();
		lamsConfigurationGetRequest.setName(keyName);
		logger.info("the asset category names found ::: " + lamsConfigurationGetRequest);
		List<String> assetCategoryNames = lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest)
				.get(keyName);
		logger.info("the asset category names found ::: " + assetCategoryNames);
		for (String string : assetCategoryNames) {
			if (string.equals(assetCategory.getName())) {
				if (rentIncrement != null) {
					Long rentIncrementId = rentIncrement.getId();
					RentIncrementType responseRentIncrement = rentIncrementService
							.getRentIncrementById(rentIncrementId);
					if (!responseRentIncrement.getId().equals(rentIncrement.getId()))
						errors.rejectValue("Agreement.rentIncrement.Id", "","invalid rentincrement type object");
				} else {
					errors.rejectValue("Agreement.rentIncrement.Id", "","please enter a rentincrement type value for given agreement");
				}
			}
		}
	}
}
