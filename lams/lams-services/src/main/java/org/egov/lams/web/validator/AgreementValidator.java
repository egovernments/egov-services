package org.egov.lams.web.validator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.egov.lams.config.PropertiesManager;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.Allottee;
import org.egov.lams.model.AssetCategory;
import org.egov.lams.model.Demand;
import org.egov.lams.model.DemandDetails;
import org.egov.lams.model.RentIncrementType;
import org.egov.lams.model.WorkflowDetails;
import org.egov.lams.model.enums.Action;
import org.egov.lams.model.enums.Source;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.AssetRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.repository.WorkFlowRepository;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

@Component
public class AgreementValidator implements org.springframework.validation.Validator {

	public static final Logger logger = LoggerFactory.getLogger(AgreementValidator.class);

	@Autowired
	private AssetRepository assetService;

	@Autowired
	private WorkFlowRepository workFlowRepository;

	@Autowired
	private AllotteeRepository allotteeService;

	@Autowired
	private RentIncrementRepository rentIncrementService;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LamsConfigurationService lamsConfigurationService;

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private AgreementService agreementService;

	@Override
	public boolean supports(Class<?> clazz) {
		return AgreementRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AgreementRequest agreementRequest = null;

		if (target instanceof AgreementRequest)
			agreementRequest = (AgreementRequest) target;
		else
			throw new RuntimeException("invalid datatype for agreement validator");
		
		if(agreementRequest.getAgreement().getAction() == null)
		agreementRequest.getAgreement().setAction(Action.CREATE);
		
		switch (agreementRequest.getAgreement().getAction()) {

		case CREATE:
			validateCreate(agreementRequest, errors);
			break;

		case CANCELLATION:
			validateCancel(agreementRequest, errors);
			break;

		case RENEWAL:
			validateRenewal(agreementRequest, errors);
			break;
			
		case EVICTION:
			validateEviction(agreementRequest, errors);
			break;
		case OBJECTION:
			validateObjection(agreementRequest, errors);
			break;
		case JUDGEMENT:
			validateJudgement(agreementRequest, errors);
			break;
			

		default:
			break;
		}
	}

	private void validateEviction(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		List<String> assetCategoryNames = getConfigurations(propertiesManager.getEvictionAssetCategoryKey());
		logger.info("the eviction asset category names found ::: " + assetCategoryNames);
		for (String string : assetCategoryNames) {
			if (!(string.equalsIgnoreCase(assetCategory.getName()))) {
	
					errors.rejectValue("Agreement.asset.assetCategory", "",
							"eviction is valid only for shop asset category");
				}
			}
		}

	public void validateRenewal(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();

		checkWorkFlowState(agreement.getStateId(), agreement.getTenantId(), requestInfo, errors,
				agreement.getAction().toString());
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, agreement.getAction().toString());

		Long assetId = agreement.getAsset().getId();

		for (Agreement agreement2 : agreementService.getAgreementsForAssetId(assetId)) {
			if (!agreement2.getAgreementNumber().equals(agreement.getAgreementNumber())) {
				errors.rejectValue("Renewal Rejected", "",
						"new agreement has already been signed for the particular asset");
			}
		}

		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreement.getExpiryDate());
		calendar.setTimeZone(TimeZone.getTimeZone(propertiesManager.getTimeZone()));
		
		String beforeExpiryMonth = getConfigurations(propertiesManager.getRenewalTimeBeforeExpiry()).get(0);
		calendar.add(Calendar.MONTH, -Integer.valueOf(beforeExpiryMonth));
		calendar.add(Calendar.DATE, 1);
		Date beforeExpiry = calendar.getTime();

		calendar.setTime(agreement.getExpiryDate());
		String afterExpiryMonth = getConfigurations(propertiesManager.getRenewalTimeAfterExpiry()).get(0);
		calendar.add(Calendar.MONTH, Integer.valueOf(afterExpiryMonth));
		Date afterExpiry = calendar.getTime();

		if (!(today.compareTo(beforeExpiry) >= 0 && today.compareTo(afterExpiry) <= 0)) {
			if (today.compareTo(beforeExpiry) < 0)
				errors.rejectValue("Renewal Rejected", "",
						"agreement can be renewed only in the period of " + 3 + " months before expiry date");
			else if (today.compareTo(afterExpiry) > 0)
				errors.rejectValue("Renewal Rejected", "",
						"agreement can be renewed only in the period of " + 3 + " months after expiry date");
		}
	}

	public void validateCancel(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();

		checkWorkFlowState(agreement.getStateId(), agreement.getTenantId(), requestInfo, errors,
				agreement.getAction().toString());
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, agreement.getAction().toString());
	}

	public void validateCreate(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();

		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();

		String securityDepositFactor = getConfigurations(propertiesManager.getSecurityDepositFactor()).get(0);
		if (securityDeposit < rent * Integer.valueOf(securityDepositFactor))
			errors.rejectValue("Agreement.securityDeposit", "",
					"security deposit value should be greater than or equal to thrice rent value");

		if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.solvencyCertificateDate", "",
					"solvency certificate date should be lesser than current date");

		if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.bankGuaranteeDate", "",
					"bank Guarantee Date date should be lesser than current date");
		if (agreement.getSource().equals(Source.DATA_ENTRY)) {

			if (agreement.getCollectedSecurityDeposit() != null
					&& (agreement.getSecurityDeposit().compareTo(agreement.getCollectedSecurityDeposit()) < 0))
				errors.rejectValue("Agreement.CollectedSecurotyDeposit", "",
						"collectedSecurityDeposit should not be greater than security deposit");

			if (agreement.getCollectedGoodWillAmount() != null
					&& (agreement.getGoodWillAmount().compareTo(agreement.getCollectedGoodWillAmount()) < 0))
				errors.rejectValue("Agreement.CollectedGoodWillAmount", "",
						"CollectedGoodWillAmount should not be greater than GoodWillAmount");

		}
		validateAsset(agreementRequest, errors);
		validateAllottee(agreementRequest, errors);
		validateRentIncrementType(agreement, errors);
		if(agreement.getSource().equals(Source.SYSTEM)){
		validateWorkflowDetails(agreement.getWorkflowDetails(), errors);
		}
	}

	private void validateWorkflowDetails(WorkflowDetails workflowDetails, Errors errors) {
		if(workflowDetails.getAssignee() == null)
			errors.rejectValue("Agreement.workflowDetails.assignee", "",
					"Approver assignee details has to be filled");
	}

	public void validateAsset(AgreementRequest agreementRequest, Errors errors) {

		Long assetId = agreementRequest.getAgreement().getAsset().getId();
		String queryString = "id=" + assetId + "&tenantId=" + agreementRequest.getAgreement().getTenantId();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		AssetResponse assetResponse = assetService.getAssets(queryString, requestInfoWrapper);
		if (assetResponse.getAssets() == null || assetResponse.getAssets().size() == 0)
			errors.rejectValue("Agreement.securityDeposit", "", "the asset given does not exist");

		if (!assetService.isAssetAvailable(assetId))
			errors.rejectValue("Agreement.Asset.id", "", "Agreement has been already signed for the given asset");
	}

	public void validateAllottee(AgreementRequest agreementRequest, Errors errors) {

		Allottee allottee = agreementRequest.getAgreement().getAllottee();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		allottee.setTenantId(agreementRequest.getAgreement().getTenantId());

		AllotteeResponse allotteeResponse = allotteeService.getAllottees(allottee, requestInfo);
		if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().size() == 0) {
			allotteeService.createAllottee(allottee, requestInfo);
		} else
			allottee.setId(allotteeResponse.getAllottee().get(0).getId());
	}

	public void validateRentIncrementType(Agreement agreement, Errors errors) {

		RentIncrementType rentIncrement = agreement.getRentIncrementMethod();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		List<String> assetCategoryNames = getConfigurations(propertiesManager.getRentIncrementAssetCategoryKey());
		logger.info("the asset category names found ::: " + assetCategoryNames);
		for (String string : assetCategoryNames) {
			if (string.equalsIgnoreCase(assetCategory.getName())) {
				if (rentIncrement != null && rentIncrement.getId()!=null) {
					Long rentIncrementId = rentIncrement.getId();
					List<RentIncrementType> rentIncrements  = rentIncrementService.getRentIncrementById(rentIncrementId);
					if(rentIncrements.isEmpty()) 
						errors.rejectValue("Agreement.rentIncrementMethod.id", "",
								"no rentincrement type found for given value");
					RentIncrementType responseRentIncrement = rentIncrements.get(0);
					if (!responseRentIncrement.getId().equals(rentIncrement.getId()))
						errors.rejectValue("Agreement.rentIncrement.Id", "", "invalid rentincrement type object");
				} else {
					errors.rejectValue("Agreement.rentIncrementMethod.id", "",
							"please enter a rentincrement type value for given agreement");
				}
			}
		}
	}

	private void checkRentDue(String demandId, RequestInfo requestInfo, Errors errors, String processName) {

		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		demandSearchCriteria.setDemandId(Long.getLong(demandId));
		Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
		if (demand == null)
			errors.rejectValue("Agreement.demands", "", "No Demands found for the given agreement");
		else {
			Date today = new Date();
			for (DemandDetails demandDetails : demand.getDemandDetails()) {
				if (today.compareTo(demandDetails.getPeriodStartDate()) >= 0
						&& (!demandDetails.getTaxAmount().subtract(demandDetails.getCollectionAmount()).equals(0)))
					errors.rejectValue("Demands unpaid", "",
							"all due must be paid till current month to initiate " + processName);
			}
		}
	}

	private void checkWorkFlowState(String stateId, String tenantId, RequestInfo requestInfo, Errors errors,
			String processName) {

		Task task = workFlowRepository.getWorkFlowState(stateId, tenantId, requestInfo);
		if (!"END".equalsIgnoreCase(task.getState()))
			errors.rejectValue("Agreement workflow Incomplete", "",
					"Agreement workflow must be in end state to initiate " + processName);
	}

	private List<String> getConfigurations(String keyName) {
		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		lamsConfigurationGetRequest.setName(keyName);
		logger.info("the asset category names found ::: " + lamsConfigurationGetRequest);
		return lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest).get(keyName);
	}

	public void validateUpdate(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		validateWorkflowDetails(agreement.getWorkflowDetails(), errors);
		
	}
	
	private void validateObjection(AgreementRequest agreementRequest, Errors errors) {
		String renewalStatus = agreementService.checkRenewalStatus(agreementRequest);
		if (!"ACTIVE".equals(renewalStatus)) {
			errors.reject("Can't do objection", "Renewal status is not active");
		}

	}

	private void validateJudgement(AgreementRequest agreementRequest, Errors errors) {
		String objectionStatus = agreementService.checkObjectionStatus(agreementRequest);
		if (!"ACTIVE".equals(objectionStatus)) {
			errors.reject("Can't start ", "Judgement will be applicable on objected agreements only!");
		}
		
	}
}
