package org.egov.lams.web.validator;

import java.math.BigDecimal;
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
import org.egov.lams.model.enums.Status;
import org.egov.lams.repository.AllotteeRepository;
import org.egov.lams.repository.AssetRepository;
import org.egov.lams.repository.DemandRepository;
import org.egov.lams.repository.RentIncrementRepository;
import org.egov.lams.service.AgreementService;
import org.egov.lams.service.LamsConfigurationService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AllotteeResponse;
import org.egov.lams.web.contract.AssetResponse;
import org.egov.lams.web.contract.DemandSearchCriteria;
import org.egov.lams.web.contract.LamsConfigurationGetRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AgreementValidator {

	public static final Logger logger = LoggerFactory.getLogger(AgreementValidator.class);

	public static final String WF_ACTION_CANCEL = "Cancel";
	public static final String WF_ACTION_PRINT_NOTICE = "Print Notice";
	public static final String ERROR_FIELD_AGREEMENT_NO = "Agreement.agreementNumber" ;
	public static final String ERROR_MSG_UNDER_WORKFLOW = "Agreement is already under going in some workflow.";
	public static final String ACTION_MODIFY ="Modify";
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

	@Autowired
	private DemandRepository demandRepository;

	@Autowired
	private AgreementService agreementService;

	public void validateCreate(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();

		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();
		Date expiryDate = agreementService.getExpiryDate(agreement);
		Date currentDate = new Date();

		String securityDepositFactor = getConfigurations(propertiesManager.getSecurityDepositFactor(),
				agreement.getTenantId()).get(0);
		if (securityDeposit < rent * Integer.valueOf(securityDepositFactor))
			errors.rejectValue("Agreement.securityDeposit", "",
					"security deposit value should be greater than or equal to thrice rent value");
		if (Source.SYSTEM.equals(agreement.getSource())) {
			if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.solvencyCertificateDate", "",
					"solvency certificate date should be lesser than current date");

		if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.bankGuaranteeDate", "",
						"bank Guarantee Date date should be lesser than current date");
		}
		else {

			if (agreement.getCollectedSecurityDeposit() != null
					&& (agreement.getSecurityDeposit().compareTo(agreement.getCollectedSecurityDeposit()) < 0))
				errors.rejectValue("Agreement.CollectedSecurotyDeposit", "",
						"collectedSecurityDeposit should not be greater than security deposit");

			if (agreement.getCollectedGoodWillAmount() != null
					&& (agreement.getGoodWillAmount().compareTo(agreement.getCollectedGoodWillAmount()) < 0))
				errors.rejectValue("Agreement.CollectedGoodWillAmount", "",
						"CollectedGoodWillAmount should not be greater than GoodWillAmount");

		}
		if(currentDate.after(expiryDate)){
			errors.rejectValue("Agreement.TimePeriod", "",
					"Can not create history agreement,please change Timeperiod/CommencementDate");
		}
		validateAsset(agreementRequest, errors);
		validateAllottee(agreementRequest, errors);
		if (agreement.getSource().equals(Source.SYSTEM)) {
			validateWorkflowDetails(agreement.getWorkflowDetails(), errors);
		}
	}

	public void validateModifiedData(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		Date expiryDate = agreementService.getExpiryDate(agreement);
		Date currentDate = new Date();
		if (agreement.getCollectedSecurityDeposit() != null
				&& (agreement.getSecurityDeposit().compareTo(agreement.getCollectedSecurityDeposit()) < 0))
			errors.reject("Agreement.CollectedSecurotyDeposit",
					"collectedSecurityDeposit should not be greater than security deposit");

		if (agreement.getCollectedGoodWillAmount() != null
				&& (agreement.getGoodWillAmount().compareTo(agreement.getCollectedGoodWillAmount()) < 0))
			errors.reject("Agreement.CollectedGoodWillAmount",
					"CollectedGoodWillAmount should not be greater than GoodWillAmount");
		if (currentDate.after(expiryDate)) {
			errors.reject("Agreement.TimePeriod",
					"Can not create history agreement,please change Timeperiod/CommencementDate");
		}

	}
	public void validateEviction(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		List<String> assetCategoryNames = getConfigurations(propertiesManager.getEvictionAssetCategoryKey(),
				agreement.getTenantId());
		logger.info("the eviction asset category names found ::: " + assetCategoryNames);
		for (String string : assetCategoryNames) {
			if (!(string.equalsIgnoreCase(assetCategory.getName()))) {

				errors.reject("Agreement not allowed for Evicition", "Eviction is valid only for Shop types.");
			}
		}
	}

	public void validateRenewal(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, agreement.getAction().toString());

		Long assetId = agreement.getAsset().getId();

		for (Agreement agreement2 : agreementService.getAgreementsForAssetId(assetId)) {
			if (!agreement2.getAgreementNumber().equals(agreement.getAgreementNumber())) {
				errors.rejectValue(ERROR_FIELD_AGREEMENT_NO, "",
						"new agreement has already been signed for the particular asset");
			}
		}

		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(agreement.getExpiryDate());
		calendar.setTimeZone(TimeZone.getTimeZone(propertiesManager.getTimeZone()));

		String beforeExpiryMonth = getConfigurations(propertiesManager.getRenewalTimeBeforeExpiry(),
				agreement.getTenantId()).get(0);
		calendar.add(Calendar.MONTH, -Integer.valueOf(beforeExpiryMonth));
		calendar.add(Calendar.DATE, 1);
		Date beforeExpiry = calendar.getTime();

		calendar.setTime(agreement.getExpiryDate());
		String afterExpiryMonth = getConfigurations(propertiesManager.getRenewalTimeAfterExpiry(),
				agreement.getTenantId()).get(0);
		calendar.add(Calendar.MONTH, Integer.valueOf(afterExpiryMonth));
		Date afterExpiry = calendar.getTime();

		if (!(today.compareTo(beforeExpiry) >= 0 && today.compareTo(afterExpiry) <= 0)) {
			if (today.compareTo(beforeExpiry) < 0)
				errors.reject(ERROR_FIELD_AGREEMENT_NO,
						"agreement can be renewed only in the period of " + 3 + " months before expiry date");
			else if (today.compareTo(afterExpiry) > 0)
				errors.reject(ERROR_FIELD_AGREEMENT_NO,
						"agreement can be renewed only in the period of " + 3 + " months after expiry date");
		}
	}

	public void validateCancel(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, agreement.getAction().toString());
	}

	public void validateObjection(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		String renewalStatus = agreementService.checkRenewalStatus(agreementRequest);
		if (!"ACTIVE".equals(renewalStatus)) {
			errors.reject("Can't do objection", "Renewal status is not active");
		}
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}

	}

	public void validateJudgement(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		String objectionStatus = agreementService.checkObjectionStatus(agreementRequest);
		if (!"ACTIVE".equals(objectionStatus)) {
			errors.reject("Can't proceed ", "Judgement will be applicable on objected agreements only!");
		}
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
	}

	public void validateRemissionDetails(AgreementRequest agreementRequest, Errors errors) {
		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		Agreement agreement = agreementRequest.getAgreement();
		Date fromDate = agreement.getRemission().getRemissionDate();
		Date toDate = agreement.getRemission().getRemissionToDate();
		AssetCategory assetCategory = agreement.getAsset().getCategory();
		Boolean isRentCollected;
		demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
		if (!propertiesManager.getAssetCategoryMarket().equalsIgnoreCase(assetCategory.getName())) {
			errors.reject("Remission Can't allowed", "Remission can be allowed on Market asset type only.");
		}
		Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, agreementRequest.getRequestInfo())
				.getDemands().get(0);
		if (demand == null)
			errors.reject("No demands", "No Demands found for the given agreement");
		else {
			isRentCollected = checkCollection(demand.getDemandDetails(), fromDate, toDate);

			if (isRentCollected) {
				errors.reject("Agreement", "Rent can not be modified for already collected installment!");
			}
		}

	}
	
	public void validateModify(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		if (!Source.DATA_ENTRY.equals(agreement.getSource())) {
			errors.reject("Agreement not valid", "Only data entry agreements allowed to modify.");
		}
		if (!Action.CREATE.equals(agreement.getAction())) {
			errors.reject("Agreement not valid", "Can't allowed to modify as transactions already made for this agreement!");
		}
		if (agreement.getIsUnderWorkflow()) {
			errors.reject("Agreement underworkflow", "Agreement is already in other workflow");
		}
	}

	public void validateAsset(AgreementRequest agreementRequest, Errors errors) {

		Long assetId = agreementRequest.getAgreement().getAsset().getId();
		String queryString = "id=" + assetId + "&tenantId=" + agreementRequest.getAgreement().getTenantId();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		AssetResponse assetResponse = assetService.getAssets(queryString, requestInfoWrapper);
		if (assetResponse.getAssets() == null || assetResponse.getAssets().isEmpty())
			errors.rejectValue("Agreement.securityDeposit", "", "the asset given does not exist");

		if (!assetService.isAssetAvailable(assetId))
			errors.rejectValue("Agreement.Asset.id", "", "Agreement has been already signed for the given asset");
	}

	public void validateAllottee(AgreementRequest agreementRequest, Errors errors) {

		Allottee allottee = agreementRequest.getAgreement().getAllottee();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		allottee.setTenantId(agreementRequest.getAgreement().getTenantId());

		AllotteeResponse allotteeResponse = allotteeService.getAllottees(allottee, requestInfo);
		if (allotteeResponse.getAllottee() == null || allotteeResponse.getAllottee().isEmpty()) {
			allotteeService.createAllottee(allottee, requestInfo);
		} else
			allottee.setId(allotteeResponse.getAllottee().get(0).getId());
	}

	private void validateWorkflowDetails(WorkflowDetails workflowDetails, Errors errors) {

		if (!WF_ACTION_CANCEL.equalsIgnoreCase(workflowDetails.getAction())
				&& !WF_ACTION_PRINT_NOTICE.equalsIgnoreCase(workflowDetails.getAction())
				&& workflowDetails.getAssignee() == null)
			errors.rejectValue("Agreement.workflowDetails.assignee", "", "Approver assignee details has to be filled");
	}

	
	private void checkRentDue(String demandId, RequestInfo requestInfo, Errors errors, String processName) {

		DemandSearchCriteria demandSearchCriteria = new DemandSearchCriteria();
		if (demandId == null) {
			errors.rejectValue("No Demand", "", "No demandId in this agreement request");

		} else {
			demandSearchCriteria.setDemandId(Long.valueOf(demandId));
			Demand demand = demandRepository.getDemandBySearch(demandSearchCriteria, requestInfo).getDemands().get(0);
			if (demand == null)
				errors.reject("No demands", "No Demands found for the given agreement");
			else {
				Date today = new Date();
				for (DemandDetails demandDetails : demand.getDemandDetails()) {
					if (today.compareTo(demandDetails.getPeriodStartDate()) >= 0
							&& (demandDetails.getTaxAmount().subtract(demandDetails.getCollectionAmount()))
									.compareTo(BigDecimal.ZERO) > 0)
						errors.reject("Rent due",
								"All the dues must be paid till current installment to initiate " + processName);
				}
			}
		}
	}

	public void validateRentIncrementType(Agreement agreement, Errors errors) {

		RentIncrementType rentIncrement = agreement.getRentIncrementMethod();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		List<String> assetCategoryNames = getConfigurations(propertiesManager.getRentIncrementAssetCategoryKey(),
				agreement.getTenantId());
		logger.info("the asset category names found ::: " + assetCategoryNames);
		for (String string : assetCategoryNames) {
			if (string.equalsIgnoreCase(assetCategory.getName())) {
				if (rentIncrement != null && rentIncrement.getId() != null) {
					Long rentIncrementId = rentIncrement.getId();
					List<RentIncrementType> rentIncrements = rentIncrementService.getRentIncrementById(rentIncrementId);
					if (rentIncrements.isEmpty())
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

	private List<String> getConfigurations(String keyName, String tenantId) {
		LamsConfigurationGetRequest lamsConfigurationGetRequest = new LamsConfigurationGetRequest();
		lamsConfigurationGetRequest.setName(keyName);
		lamsConfigurationGetRequest.setTenantId(tenantId);
		logger.info("the asset category names found ::: " + lamsConfigurationGetRequest);
		return lamsConfigurationService.getLamsConfigurations(lamsConfigurationGetRequest).get(keyName);
	}

	private Boolean checkCollection(List<DemandDetails> demandDetails, Date fromDate, Date toDate) {
		Boolean isPaid = Boolean.FALSE;
		for (DemandDetails demandDetail : demandDetails) {
			if (propertiesManager.getTaxReasonRent().equalsIgnoreCase(demandDetail.getTaxReason())) {
				if (demandDetail.getPeriodEndDate().compareTo(fromDate) >= 0
						&& demandDetail.getPeriodStartDate().compareTo(toDate) <= 0
						&& demandDetail.getCollectionAmount().compareTo(BigDecimal.ZERO) > 0) {
					isPaid = Boolean.TRUE;
					return isPaid;
				}
			}

		}
		return isPaid;
	}

	public void validateUpdate(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		validateWorkflowDetails(agreement.getWorkflowDetails(), errors);

	}

	public void validateAgreementForWorkFLow(AgreementRequest agreementRequest, Errors errors, String action) {

		Agreement agreement = agreementRequest.getAgreement();
		if(Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, "History/InActive agreements are not allowed.");
		}
		
		if (Action.RENEWAL.toString().equals(action)) {
			validateRenewal(agreementRequest, errors);
		} else if (Action.CANCELLATION.toString().equals(action)) {
			validateCancel(agreementRequest, errors);
		} else if (Action.EVICTION.toString().equals(action)) {
			validateEviction(agreementRequest, errors);
		} else if (Action.OBJECTION.toString().equals(action)) {
			validateObjection(agreementRequest, errors);
		} else if (Action.JUDGEMENT.toString().equals(action)) {
			validateJudgement(agreementRequest, errors);
		} else if (Action.REMISSION.toString().equals(action)) {
			validateRemission(agreementRequest, errors);
		} else if (ACTION_MODIFY.equalsIgnoreCase(action)){
			validateModify(agreementRequest, errors);
			
		}
	}

	public void validateRemission(AgreementRequest agreementRequest, Errors errors) {
		Agreement agreement = agreementRequest.getAgreement();
		Date currentDate = new Date();
		Date expiryDate = agreement.getExpiryDate();
		AssetCategory assetCategory = agreement.getAsset().getCategory();
		if (currentDate.after(expiryDate)) {
			errors.reject("Can't allowed", "Remission Can not be allowed on expired agreements.");
		}
		if (!propertiesManager.getAssetCategoryMarket().equalsIgnoreCase(assetCategory.getName())) {
			errors.reject("Can't allowed", "Remission can be allowed on Market asset type only.");
		}

	}
	
	public void validatePartialCollection(AgreementRequest agreementRequest, Errors errors){
		Agreement agreement = agreementRequest.getAgreement();
		for(Demand demand : agreement.getLegacyDemands()){
			for(DemandDetails demandDetails : demand.getDemandDetails()){
				if ((propertiesManager.getTaxReasonRent().equalsIgnoreCase(demandDetails.getTaxReason())
						|| propertiesManager.getTaxReasonPenalty().equalsIgnoreCase(demandDetails.getTaxReason()))
						&& demandDetails.getCollectionAmount().compareTo(BigDecimal.ZERO) > 0 
						&& demandDetails.getTaxAmount().compareTo(demandDetails.getCollectionAmount()) > 0)
					errors.reject("No Partial Collection allowed", "Partial collection is not allowed for "
							+ demandDetails.getTaxReason() + " of " + demandDetails.getTaxPeriod());
			}
		}
	}

}
