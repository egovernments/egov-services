package org.egov.lams.web.validator;

import org.apache.commons.lang3.StringUtils;
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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Component
public class AgreementValidator {

	public static final Logger logger = LoggerFactory.getLogger(AgreementValidator.class);

	public static final String WF_ACTION_CANCEL = "Cancel";
	public static final String WF_ACTION_PRINT_NOTICE = "Print Notice";
	public static final String ERROR_FIELD_AGREEMENT_NO = "Agreement.agreementNumber" ;
	public static final String ERROR_MSG_UNDER_WORKFLOW = "Agreement is already under going in some workflow.";
	public static final String ACTION_MODIFY ="Modify";
	public static final String SHOPPING_COMPLEX = "Shopping Complex";
	private static final List<String> AUCTION_CATEGORIES = Arrays.asList("Market", "Fish Tanks", "Slaughter House", "Community Toilet Complex", "Community Hall");
	private static final String ERROR_MSG_AGREEMENT_INACTIVE = "History/InActive agreements are not allowed.";
	private static final String CENTRAL_GST = "CENTRAL_GST";
	private static final String STATE_GST = "STATE_GST";
	private static final String RENT = "RENT";
	
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
		Boolean allowedAssetCategory;
		Agreement agreement = agreementRequest.getAgreement();
		if (agreement.getIsHistory()) {
			validateHistoryAgreementsForAsset(agreement, errors);
		} else {
 		Double rent = agreement.getRent();
		Double securityDeposit = agreement.getSecurityDeposit();
		Date solvencyCertificateDate = agreement.getSolvencyCertificateDate();
		Date bankGuaranteeDate = agreement.getBankGuaranteeDate();
		Date expiryDate = agreementService.getExpiryDate(agreement);
		Date currentDate = new Date();
		String securityDepositFactor = getConfigurations(propertiesManager.getSecurityDepositFactor(),
				agreement.getTenantId()).get(0);
		allowedAssetCategory = isValidAuctionAssetCategory(agreement);

		if(allowedAssetCategory){
			agreement.setExpiryDate(agreementService.getEffectiveFinYearToDate());
		}
         
		if (securityDeposit < rent * Integer.valueOf(securityDepositFactor) && !allowedAssetCategory){
			errors.rejectValue("Agreement.securityDeposit", "",
					"security deposit value should be greater than or equal to thrice rent value");}
		 if (Source.SYSTEM.equals(agreement.getSource())) {
			if (solvencyCertificateDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.solvencyCertificateDate", "",
					"solvency certificate date should be lesser than current date");

			else if (bankGuaranteeDate.compareTo(new Date()) >= 0)
			errors.rejectValue("Agreement.bankGuaranteeDate", "",
						"bank Guarantee Date date should be lesser than current date");
		validateWorkflowDetails(agreement.getWorkflowDetails(), errors);
		}
		else {

			if (agreement.getCollectedSecurityDeposit() != null
					&& (agreement.getSecurityDeposit().compareTo(agreement.getCollectedSecurityDeposit()) < 0))
				errors.rejectValue("Agreement.CollectedSecurotyDeposit", "",
						"collectedSecurityDeposit should not be greater than security deposit");

			else if (agreement.getCollectedGoodWillAmount() != null
					&& (agreement.getGoodWillAmount().compareTo(agreement.getCollectedGoodWillAmount()) < 0))
				errors.rejectValue("Agreement.CollectedGoodWillAmount", "",
						"CollectedGoodWillAmount should not be greater than GoodWillAmount");
			/*else if (!allowedAssetCategory && StringUtils.isBlank(agreement.getOldAgreementNumber()))
				errors.rejectValue("Agreement.oldAgreementNumber", "",
						"Old agreement number is mandatory for Data Entry");*/

		}
		if(currentDate.after(expiryDate)){
			errors.rejectValue("Agreement.TimePeriod", "",
					"Can not create history agreement,please change Timeperiod/CommencementDate");
		}
			validateAsset(agreementRequest, errors);
			calculateGstAndServiceTax(agreementRequest);

	    }
	    validateAllottee(agreementRequest, errors);
				
	}

	public void validateModifiedData(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		Date expiryDate = agreementService.getExpiryDate(agreement);
		Date currentDate = new Date();
		if (agreement.getCollectedSecurityDeposit() != null
				&& (agreement.getSecurityDeposit().compareTo(agreement.getCollectedSecurityDeposit()) < 0)) {
			errors.reject("Agreement.CollectedSecurotyDeposit",
					"collectedSecurityDeposit should not be greater than security deposit");

		} else if (agreement.getCollectedGoodWillAmount() != null
				&& (agreement.getGoodWillAmount().compareTo(agreement.getCollectedGoodWillAmount()) < 0)) {
			errors.reject("Agreement.CollectedGoodWillAmount",
					"CollectedGoodWillAmount should not be greater than GoodWillAmount");
		} else if (currentDate.after(expiryDate)) {
			errors.reject("Agreement.TimePeriod",
					"Can not create history agreement,please change Timeperiod/CommencementDate");
		}
		validateAgreementsForFloor(agreement, agreement.getAsset().getId(), errors);

	}
	public void validateEviction(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		AssetCategory assetCategory = agreement.getAsset().getCategory();

		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		if(Status.EVICTED.equals(agreement.getStatus()) || Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_AGREEMENT_INACTIVE);
		}
		List<String> evictionAssetCategories = getConfigurations(propertiesManager.getEvictionAssetCategoryKey(),
				agreement.getTenantId());

		if (!evictionAssetCategories.stream().anyMatch(category -> category.equalsIgnoreCase(assetCategory.getName()))) {
			errors.reject("Agreement not allowed for Evicition",
					"Eviction is valid only for ShoppingComplex/Shop types.");
		}
	}

	public void validateRenewal(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		RequestInfo requestInfo = agreementRequest.getRequestInfo();
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		if(Status.EVICTED.equals(agreement.getStatus()) || Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_AGREEMENT_INACTIVE);
		}
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, Action.RENEWAL.toString());

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
		if(Status.EVICTED.equals(agreement.getStatus()) || Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_AGREEMENT_INACTIVE);
		}
		checkRentDue(agreement.getDemands().get(0), requestInfo, errors, Action.CANCELLATION.toString());
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
		Double remissionRent = agreement.getRemission().getRemissionRent();
		Boolean isRentCollected;
		demandSearchCriteria.setDemandId(Long.valueOf(agreement.getDemands().get(0)));
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		if(Status.EVICTED.equals(agreement.getStatus()) || Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_AGREEMENT_INACTIVE);
		}
		if (remissionRent.compareTo(agreement.getRent()) > 0) {

			errors.reject("Remission", "Remission rent can not be more than actual rent.");
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
			errors.reject("Agreement not valid",
					"Can't allowed to modify as transactions already made for this agreement!");
		}
		if (agreement.getIsUnderWorkflow()) {
			errors.reject("Agreement underworkflow", "Agreement is already in other workflow");
		}
		
	}

	public void validateAsset(AgreementRequest agreementRequest, Errors errors) {

		Agreement agreement = agreementRequest.getAgreement();
		Long assetId = agreementRequest.getAgreement().getAsset().getId();
		String queryString = "id=" + assetId + "&tenantId=" + agreementRequest.getAgreement().getTenantId();
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(agreementRequest.getRequestInfo());
		AssetResponse assetResponse = assetService.getAssets(queryString, requestInfoWrapper);
		if (assetResponse.getAssets() == null || assetResponse.getAssets().isEmpty())
			errors.rejectValue("Agreement.securityDeposit", "", "No asset is created");
	    validateAgreementsForFloor(agreement, assetId, errors);

	}

	private void validateAgreementsForFloor(Agreement agreement, Long assetId, Errors errors) {
		List<Agreement> agreements;
		Boolean isExist = Boolean.FALSE;
		String shopNumber = agreement.getReferenceNumber();
		Long agreementId = agreement.getId();
		if (StringUtils.isNotBlank(shopNumber)) {
			agreements = agreementService.getAgreementsForAssetIdAndFloor(agreement, assetId);

			if (!agreements.isEmpty()) {
				if (agreementId == null)
					isExist = agreements.stream().anyMatch(a -> shopNumber.equalsIgnoreCase(a.getReferenceNumber()));
				else {
					isExist = agreements.stream().filter(a -> !agreementId.equals(a.getId()))
							.anyMatch(a -> shopNumber.equalsIgnoreCase(a.getReferenceNumber()));
				}
			}

		}
		if (isExist) {
			errors.rejectValue("Agreement.ReferenceNumber", "",
					"Agreement already exists with the shop Number: " + shopNumber);

		}

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
				
				Boolean balance = demand.getDemandDetails().stream()
						.filter(demandDetail -> today.compareTo(demandDetail.getPeriodStartDate()) >= 0)
						.anyMatch(demandDetail -> demandDetail.getTaxAmount()
								.subtract(demandDetail.getCollectionAmount()).compareTo(BigDecimal.ZERO) > 0);

				if (balance) {
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
	
	
	private Boolean isValidAuctionAssetCategory(Agreement agreement) {
		String assetCategory = agreement.getAsset().getCategory().getName();

		return AUCTION_CATEGORIES.stream().anyMatch(category -> category.equalsIgnoreCase(assetCategory));

	}

	private Boolean checkCollection(List<DemandDetails> demandDetails, Date fromDate, Date toDate) {
		Boolean isPaid = Boolean.FALSE;
		for (DemandDetails demandDetail : demandDetails) {
			if (RENT.equalsIgnoreCase(demandDetail.getTaxReasonCode())
					|| CENTRAL_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())
					|| STATE_GST.equalsIgnoreCase(demandDetail.getTaxReasonCode())) {
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
		if(Status.EVICTED.equals(agreement.getStatus()) || Status.INACTIVE.equals(agreement.getStatus()) || Status.HISTORY.equals(agreement.getStatus())){
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_AGREEMENT_INACTIVE);
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
		if (agreement.getIsUnderWorkflow()) {
			errors.reject(ERROR_FIELD_AGREEMENT_NO, ERROR_MSG_UNDER_WORKFLOW);
		}
		if (currentDate.after(expiryDate)) {
			errors.reject("Can't allowed", "Remission Can not be allowed on expired agreements.");
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

	private void validateHistoryAgreementsForAsset(Agreement agreement, Errors errors) {

		List<Agreement> agreements = null;
		agreements = agreementService.getAllHistoryAgreementsForAsset(agreement);
		if (!agreements.isEmpty()) {
			errors.rejectValue("Duplicate History Agreement", "History agreement is already created for this asset.");
		}

	}
	
	private void calculateGstAndServiceTax(AgreementRequest agreementRequest) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Agreement agreement = agreementRequest.getAgreement();
		Date gstDate = null;
		Long gstRate = Long.valueOf("18");
		Double rent = agreement.getRent();
		Date agreementExpiryDate = agreementService.getExpiryDate(agreement);
		List<String> gstDates = getConfigurations(propertiesManager.getGstEffectiveDate(), agreement.getTenantId());
		List<String> gstRates = getConfigurations(propertiesManager.getGstRate(), agreement.getTenantId());
		if (!gstDates.isEmpty() && !gstRates.isEmpty()) {
			try {
				gstDate = formatter.parse(gstDates.get(0));
				gstRate = Long.valueOf(gstRates.get(0));
			} catch (ParseException e) {
				logger.error("exception in parsing GST date  ::: " + e);
			}
			if (agreementExpiryDate.compareTo(gstDate) > 0) {
				agreement.setCgst(Double.valueOf(Math.round((rent * gstRate / 100) / 2)));
				agreement.setSgst(Double.valueOf(Math.round((rent * gstRate / 100) / 2)));
			}

		}
	}

}
