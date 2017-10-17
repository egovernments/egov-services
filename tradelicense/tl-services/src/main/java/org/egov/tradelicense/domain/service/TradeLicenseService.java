package org.egov.tradelicense.domain.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchResponse;
import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.UserInfo;
import org.egov.tl.commons.web.contract.WorkFlowDetails;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomInvalidInputException;
import org.egov.tradelicense.domain.enums.LicenseStatus;
import org.egov.tradelicense.domain.enums.NewLicenseStatus;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradePartner;
import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.domain.repository.LicenseApplicationRepository;
import org.egov.tradelicense.domain.repository.LicenseFeeDetailRepository;
import org.egov.tradelicense.domain.repository.SupportDocumentRepository;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.repository.TradePartnerRepository;
import org.egov.tradelicense.domain.repository.TradeShiftRepository;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.contract.Demand;
import org.egov.tradelicense.web.contract.DemandResponse;
import org.egov.tradelicense.web.repository.FeeMatrixRepository;
import org.egov.tradelicense.web.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;

import lombok.extern.slf4j.Slf4j;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class TradeLicenseService {

	public static final String NEW_LICENSE_MODULE_TYPE = "NEW LICENSE";

	public static final String LICENSE_MODULE_TYPE = "LICENSE";

	public static final String NEW_TRADE_LICENSE_WF_TYPE = "New Trade License";

	public static final String NEW_TRADE_LICENSE_BUSINESSKEY = "New Trade License";

	@Autowired
	private TradeLicenseServiceValidator tradeLicenseServiceValidator;

	@Autowired
	private TradeLicenseRepository tradeLicenseRepository;

	@Autowired
	LicenseApplicationRepository licenseApplicationRepository;

	@Autowired
	LicenseFeeDetailRepository licenseFeeDetailRepository;

	@Autowired
	SupportDocumentRepository supportDocumentRepository;

	@Autowired
	private TradeLicenseNumberGeneratorService licenseNumberGenerationService;

	@Autowired
	private ApplicationNumberGeneratorService applNumberGenrationService;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	FeeMatrixRepository feeMatrixRepository;

	@Autowired
	private LicenseBillService licenseBillService;
	
	@Autowired
	PropertiesManager propertiesManager;
	
	@Autowired
	LicenseUserService licenseUserService;
	
	@Autowired
	TradeShiftRepository tradeShiftRepository;
	
	@Autowired
	TradePartnerRepository tradePartnerRepository;

	
	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		
		// external end point validations for creating license
		tradeLicenseServiceValidator.validateCreateTradeLicenseRelated(tradeLicenses, requestInfo);

		// setting the id for the license and application, support document and
		// fee details
		for (TradeLicense license : tradeLicenses) {

			// set the audit details for the license
			setLicenseCreationAuditDetails(license, requestInfo);
			// get the next sequence of license and set it as id of license
			license.setId(tradeLicenseRepository.getNextSequence());
			// set the licenseId for the application
			license.getApplication().setLicenseId(license.getId());
			// get the next sequence of application and set it as id of
			// application
			license.getApplication().setId(licenseApplicationRepository.getNextSequence());
			// set the id's for all the available support documents of the
			// application
			setIdsForLicenseApplicationSupportDocuments(license);
			// set the id's for all the available TradePartners of the license
			setIdsForLicenseTradePartners(license);
			// set the id's for all the available TradeShift of the license
			setIdsForLicenseTradeShifts(license);
			
			licenseUserService.createUser(license, requestInfo);

			if (license.getIsLegacy()) {

				// set the id's for all the available fee details of the
				// application
				setIdsForLicenseApplicationFeeDetails(license);
				// set the license status for legacy applications
				if(!license.getIsDataPorting()){
					
					setLicenseStatus(license, requestInfo);
					
				} else if(license.getIsDataPorting() && license.getExpiryDate() != null){
					
					setLicenseStatus(license, requestInfo);
				}
				// set the license number
				license.setLicenseNumber(licenseNumberGenerationService.generate(license.getTenantId(), requestInfo));
				
			} else {

				// set the application status for new license
				setCreateNewLicenseApplicationStatus(license, requestInfo);
				// populate the work flow details for new license
				populateWorkFlowDetails(license, requestInfo);
				// set the application number
				if (license.getApplication().getApplicationNumber() == null
						|| (license.getApplication().getApplicationNumber() != null
								&& license.getApplication().getApplicationNumber().isEmpty())) {

					license.getApplication().setApplicationNumber(
							applNumberGenrationService.generate(license.getTenantId(), requestInfo));
				}
				// set the application date
				if (license.getApplication().getApplicationDate() == null) {

					license.getApplication().setApplicationDate(System.currentTimeMillis());
				}
			}
		}

		return tradeLicenses;
	}

	@Transactional
	public List<TradeLicense> update(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		
		// external end point validations
		tradeLicenseServiceValidator.validateUpdateTradeLicenseRelated(tradeLicenses, requestInfo);

		for (TradeLicense license : tradeLicenses) {

			if (license.getIsLegacy()) {

				// set the license status for legacy applications
				setLicenseStatus(license, requestInfo);

			} else {

				populateWorkFlowDetails(license, requestInfo);
				populateNextStatus(license, requestInfo);
			}
		}

		return tradeLicenses;
	}

	public void addToQue(TradeLicenseRequest request, Boolean isNewRecord) {
		tradeLicenseRepository.add(request, isNewRecord);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense, RequestInfo requestInfo) {
		if(!tradeLicense.getIsLegacy())
			generateApplicationDemand(tradeLicense, requestInfo);
		return tradeLicenseRepository.save(tradeLicense);
	}

	@Transactional
	public TradeLicense update(TradeLicense tradeLicense, RequestInfo requestInfo) {

		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		if (null != tradeLicense.getApplication().getStatus())
			currentStatus = statusRepository.findByCodes(tradeLicense.getTenantId(),
					tradeLicense.getApplication().getStatus().toString(), requestInfoWrapper);

		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty() && currentStatus.getLicenseStatuses()
				.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.INSPECTION_COMPLETED.getName())) {

			DemandResponse demandResponse = null;
			Long billId = tradeLicenseRepository.getLicenseBillId(tradeLicense.getApplication().getId());
		    	Map<String, Object> licenseFeeMap = new HashMap<String, Object>();
		    	licenseFeeMap.put("businessService", propertiesManager.getBillBusinessService());
		    	licenseFeeMap.put("minimumAmountPayable", BigDecimal.valueOf(tradeLicense.getApplication().getLicenseFee()));
		    	licenseFeeMap.put("taxHeadMasterCode", propertiesManager.getTaxHeadMasterCode());
		    	licenseFeeMap.put("taxAmount", BigDecimal.valueOf(tradeLicense.getApplication().getLicenseFee()));
			
			try {
				if (billId == null)
					demandResponse = licenseBillService.createBill(tradeLicense, requestInfo, licenseFeeMap);
				else
					demandResponse = licenseBillService.updateBill(tradeLicense, billId, requestInfo, licenseFeeMap);
			} catch (ParseException e) {
				log.error("Error while generating demand");
			}
			Integer[] ids = new Integer[1];
			if (tradeLicense.getId() != null) {

				ids[0] = Integer.valueOf(tradeLicense.getId().toString());
			}
			LicenseSearch licenseSearch = LicenseSearch.builder().ids(ids).tenantId(tradeLicense.getTenantId()).build();
			TradeLicense license = tradeLicenseRepository.findLicense(licenseSearch);
			System.out.println("Trade License Search: " + license);

			if (license != null) {
				org.egov.tl.commons.web.contract.AuditDetails auditDetails = new org.egov.tl.commons.web.contract.AuditDetails(
						license.getAuditDetails().getCreatedBy(), license.getAuditDetails().getLastModifiedBy(),
						new Date().getTime(), new Date().getTime());

				LicenseBill licenseBill = new LicenseBill(null, license.getApplication().getId(),
						demandResponse.getDemands().get(0).getId(), null, license.getTenantId(), auditDetails);
				if (billId == null)
					tradeLicenseRepository.createLicenseBill(licenseBill);
			}
		}
		
		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty() && currentStatus.getLicenseStatuses()
				.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.LICENSE_FEE_PAID.getName())) {
			// generate license number and setting license number and
			// license issued date
			log.info("updating trade license number after fee paid");
			tradeLicense
					.setLicenseNumber(licenseNumberGenerationService.generate(tradeLicense.getTenantId(), requestInfo));
			tradeLicense.setIssuedDate(System.currentTimeMillis());
		}
		
		TradeLicense newTL = tradeLicenseRepository.update(tradeLicense);
		
		return newTL;
	}
	
	public void generateApplicationDemand(TradeLicense tradeLicense, RequestInfo requestInfo) {
		
		log.info("entered generateApplicationDemand");
		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		
		LicenseSearch licenseSearchFromDB = LicenseSearch.builder().applicationNumber(tradeLicense.getApplicationNumber()).tenantId(tradeLicense.getTenantId()).build();
		TradeLicense tradeLicenseFromDB = findLicense(licenseSearchFromDB);

		if (null != tradeLicenseFromDB.getApplication().getStatus())
			currentStatus = statusRepository.findByCodes(tradeLicense.getTenantId(),
					tradeLicense.getApplication().getStatus().toString(), requestInfoWrapper);
		
		log.info("status = " + tradeLicenseFromDB.getApplication().getStatus());
		if(currentStatus != null)
		log.info("currentStatus = " + currentStatus.getLicenseStatuses()
				.get(0).getCode());
		
		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty() && currentStatus.getLicenseStatuses()
				.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.ACKNOWLEDGED.getName())) {

			DemandResponse demandResponse = null;
			Long billId = tradeLicenseRepository.getApplicationBillId(tradeLicense.getApplication().getId());
			log.info("bill Id = " + billId);
			if(billId == null) {
		    	Map<String, Object> newApplicationMap = new HashMap<String, Object>();
		    	newApplicationMap.put("businessService", propertiesManager.getApplicationBusinessService());
		    	newApplicationMap.put("minimumAmountPayable", BigDecimal.valueOf(Long.parseLong(propertiesManager.getApplicationFeeAmount())));
		    	newApplicationMap.put("taxHeadMasterCode", propertiesManager.getApplicationFeeMasterCode());
		    	newApplicationMap.put("taxAmount", BigDecimal.valueOf(Long.parseLong(propertiesManager.getApplicationFeeAmount())));
		    	
				try {
					demandResponse = licenseBillService.createBill(tradeLicense, requestInfo, newApplicationMap);
				} catch (ParseException e) {
					log.error("Error while generating demand");
				}
				Integer[] ids = new Integer[1];
				if (tradeLicense.getId() != null) {
	
					ids[0] = Integer.valueOf(tradeLicense.getId().toString());
				}
				LicenseSearch licenseSearch = LicenseSearch.builder().ids(ids).tenantId(tradeLicense.getTenantId()).build();
				TradeLicense license = tradeLicenseRepository.findLicense(licenseSearch);
				System.out.println("Trade License Search: " + license);
	
				if (license != null) {
					org.egov.tl.commons.web.contract.AuditDetails auditDetails = new org.egov.tl.commons.web.contract.AuditDetails(
							license.getAuditDetails().getCreatedBy(), license.getAuditDetails().getLastModifiedBy(),
							new Date().getTime(), new Date().getTime());
	
					LicenseBill licenseBill = new LicenseBill(null, license.getApplication().getId(),
							null, demandResponse.getDemands().get(0).getId(), license.getTenantId(), auditDetails);
	
					tradeLicenseRepository.createLicenseBill(licenseBill);
				}
			}
		}
	}

	public List<TradeLicense> search(LicenseSearch domain) {

		return tradeLicenseRepository.search(domain);

	}

	private void setLicenseCreationAuditDetails(TradeLicense license, RequestInfo requestInfo) {

		// preparing audit details
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedTime(new Date().getTime());

		if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {

			auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());

		}
		// set the audit details for license
		license.setAuditDetails(auditDetails);

	}

	private void setLicenseUpdationAuditDetails(TradeLicense license, RequestInfo requestInfo) {

		// preparing audit details
		AuditDetails auditDetails = new AuditDetails();
		if (license.getAuditDetails() != null) {
			auditDetails = license.getAuditDetails();
		}
		auditDetails.setLastModifiedTime(new Date().getTime());
		if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {

			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());

		}
		// set the audit details for license
		license.setAuditDetails(auditDetails);

	}
	
	private void setIdsForLicenseTradeShifts(TradeLicense license) {
		
		if(license.getShifts() != null && license.getShifts().size() > 0){
			
			for(TradeShift tradeShift : license.getShifts()){
				
				tradeShift.setTenantId(license.getTenantId());
				tradeShift.setLicenseId(license.getId());
				tradeShift.setId(tradeShiftRepository.getNextSequence());
				
				if (license.getAuditDetails() != null) {
					
					tradeShift.setAuditDetails(license.getAuditDetails());
				}
			}
		}
	}

	private void setIdsForLicenseTradePartners(TradeLicense license) {

		if(license.getPartners() != null && license.getPartners().size() > 0){
			
			for(TradePartner tradePartner : license.getPartners()){
				
				tradePartner.setTenantId(license.getTenantId());
				tradePartner.setLicenseId(license.getId());
				tradePartner.setId(tradePartnerRepository.getNextSequence());
				
				if (license.getAuditDetails() != null) {
					
					tradePartner.setAuditDetails(license.getAuditDetails());
				}
			}
		}
	}

	private void setIdsForLicenseApplicationSupportDocuments(TradeLicense license) {

		if (license.getApplication() != null && license.getApplication().getSupportDocuments() != null
				&& license.getApplication().getSupportDocuments().size() > 0) {

			for (SupportDocument supportDocument : license.getApplication().getSupportDocuments()) {
				// set tenantId for support document
				supportDocument.setTenantId(license.getTenantId());
				// set application id for the support document
				supportDocument.setApplicationId(license.getApplication().getId());
				// get the next sequence of the support document and set it as
				// id
				supportDocument.setId(supportDocumentRepository.getNextSequence());
				// set the support document audit details
				if (license.getAuditDetails() != null) {

					supportDocument.setAuditDetails(license.getAuditDetails());
				}
			}
		}
	}

	private void setIdsForLicenseApplicationFeeDetails(TradeLicense license) {

		if (license.getApplication() != null && license.getApplication().getFeeDetails() != null
				&& license.getApplication().getFeeDetails().size() > 0) {

			for (LicenseFeeDetail feeDetail : license.getApplication().getFeeDetails()) {
				// set tenantId for fee detail
				feeDetail.setTenantId(license.getTenantId());
				// set application id for the fee detail
				feeDetail.setApplicationId(license.getApplication().getId());
				// get the next sequence of the fee detail and set it as id
				feeDetail.setId(licenseFeeDetailRepository.getNextSequence());
				// set the fee detail audit details
				if (license.getAuditDetails() != null) {

					feeDetail.setAuditDetails(license.getAuditDetails());
				}
			}
		}
	}

	private void setLicenseStatus(TradeLicense license, RequestInfo requestInfo) {

		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		if (license.getExpiryDate() > System.currentTimeMillis()) {

			currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), LICENSE_MODULE_TYPE,
					LicenseStatus.INFORCE.getName(), requestInfoWrapper);

		} else {

			currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), LICENSE_MODULE_TYPE,
					LicenseStatus.EXPIRED.getName(), requestInfoWrapper);

		}

		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

			if (currentStatus.getLicenseStatuses().size() > 0) {

				license.setStatus(currentStatus.getLicenseStatuses().get(0).getCode());
			}

		}
	}

	private void setCreateNewLicenseApplicationStatus(TradeLicense license, RequestInfo requestInfo) {

		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
				NewLicenseStatus.ACKNOWLEDGED.getName(), requestInfoWrapper);

		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

			if (currentStatus.getLicenseStatuses().size() > 0 && license.getApplication() != null
					&& currentStatus.getLicenseStatuses().get(0).getCode() != null) {

				license.getApplication().setStatus(currentStatus.getLicenseStatuses().get(0).getCode().toString());
			}
		}
	}

	private void populateWorkFlowDetails(TradeLicense license, RequestInfo requestInfo) {

		if (null != license && null != license.getApplication()
				&& null != license.getApplication().getWorkFlowDetails()) {

			WorkFlowDetails workFlowDetails = license.getApplication().getWorkFlowDetails();

			workFlowDetails.setType(NEW_TRADE_LICENSE_WF_TYPE);
			workFlowDetails.setBusinessKey(NEW_TRADE_LICENSE_BUSINESSKEY);

			if (null != requestInfo && null != requestInfo.getUserInfo()) {
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUserName());
			}

			if (workFlowDetails.getStateId() != null) {

				license.getApplication().setState_id(workFlowDetails.getStateId());
			}
		}

	}

	private void populateNextStatus(TradeLicense license, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		LicenseStatusResponse currentStatus = null;
		LicenseStatusResponse nextStatus = null;
		WorkFlowDetails workFlowDetails = null;

		if (null != license.getApplication() && null != license.getApplication().getStatus()) {

			workFlowDetails = license.getApplication().getWorkFlowDetails();
			currentStatus = statusRepository.findByCodes(license.getTenantId(),
					license.getApplication().getStatus().toString(), requestInfoWrapper);
		}

		if (null != requestInfo && null != requestInfo.getApiId()
				&& !requestInfo.getApiId().equalsIgnoreCase("org.egov.cs.tl")) {

			if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
					&& !currentStatus.getLicenseStatuses().isEmpty()
					&& workFlowDetails.getAction().equalsIgnoreCase("Forward") && (currentStatus.getLicenseStatuses()
							.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.APPLICATION_FEE_PAID.getName()) || currentStatus.getLicenseStatuses()
							.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.REJECTED.getName()))) {

				nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
						NewLicenseStatus.SCRUTINY_COMPLETED.getName(), requestInfoWrapper);

				if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

					license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
				}

			}
		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Forward") && currentStatus.getLicenseStatuses().get(0)
						.getCode().equalsIgnoreCase(NewLicenseStatus.APPLICATION_FEE_PAID.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.SCRUTINY_COMPLETED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Forward") && currentStatus.getLicenseStatuses().get(0)
						.getCode().equalsIgnoreCase(NewLicenseStatus.SCRUTINY_COMPLETED.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.INSPECTION_COMPLETED.getName(), requestInfoWrapper);

			if(license.getApplication().getFieldInspectionReport() == null 
					|| license.getApplication().getFieldInspectionReport().isEmpty()){
				
				throw new CustomInvalidInputException(propertiesManager.getFieldInspectionReportNotDefinedCode(),
						propertiesManager.getFieldInspectionReportNotDefinedErrorMsg(), requestInfoWrapper.getRequestInfo());
			}
			
			if(license.getQuantity() == null){
				
				throw new CustomInvalidInputException(propertiesManager.getUomQuanityNotDefinedCode(),
						propertiesManager.getUomQuanityNotDefinedErrorMsg(), requestInfoWrapper.getRequestInfo());
			}
			
			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
				populateLicenseFeeCalculatedValue(license, requestInfo);
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Approve") && currentStatus.getLicenseStatuses().get(0)
						.getCode().equalsIgnoreCase(NewLicenseStatus.INSPECTION_COMPLETED.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.FINAL_APPROVAL_COMPLETED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {
				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Print Certificate")
				&& currentStatus.getLicenseStatuses().get(0).getCode()
						.equalsIgnoreCase(NewLicenseStatus.LICENSE_FEE_PAID.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.LICENSE_ISSUED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());

			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Reject")) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.REJECTED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Cancel")) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.CANCELLED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getCode().toString());
			}

		}
	}

	private void populateLicenseFeeCalculatedValue(TradeLicense license, RequestInfo requestInfo) {

		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		FeeMatrixSearchResponse feeMatrixSearchResponse = feeMatrixRepository.findFeeMatrix(license,
				requestInfoWrapper);

		if (feeMatrixSearchResponse != null && feeMatrixSearchResponse.getFeeMatrices() != null
				&& feeMatrixSearchResponse.getFeeMatrices().size() > 0) {

			Double quantity = license.getQuantity();
			Double rate = null;
			String feeType = null;
			String rateType = "";
			Double licenseFee = null;
			Boolean isRateExists = false;

			for (FeeMatrixSearchContract feeMatrix : feeMatrixSearchResponse.getFeeMatrices()) {

				if (feeMatrix.getFeeType() != null
						&& feeMatrix.getFeeType().equalsIgnoreCase(FeeTypeEnum.LICENSE.name())) {
					
					feeType = feeMatrix.getFeeType();

					if (feeMatrix.getFeeMatrixDetails() != null && !feeMatrix.getFeeMatrixDetails().isEmpty()) {

						for (FeeMatrixDetailContract feeMatrixDetail : feeMatrix.getFeeMatrixDetails()) {

							if (feeMatrixDetail.getUomFrom() != null && feeMatrixDetail.getUomTo() != null) {

								if (quantity >= feeMatrixDetail.getUomFrom() && quantity < feeMatrixDetail.getUomTo()) {

									isRateExists = true;

									if (feeMatrix.getRateType() != null) {

										rateType = feeMatrix.getRateType().toString();
									}

									rate = feeMatrixDetail.getAmount();
								}

							} else if (feeMatrixDetail.getUomFrom() != null && feeMatrixDetail.getUomTo() == null) {

								if (quantity >= feeMatrixDetail.getUomFrom()) {

									isRateExists = true;

									if (feeMatrix.getRateType() != null) {

										rateType = feeMatrix.getRateType().toString();
									}

									rate = feeMatrixDetail.getAmount();
								}
							}
						}

						if (!isRateExists) {

							throw new CustomInvalidInputException(propertiesManager.getFeeMatrixRatesNotDefinedCode(),
									propertiesManager.getFeeMatrixRatesNotDefinedErrorMsg(),
									requestInfoWrapper.getRequestInfo());
						}

					} else {

						throw new CustomInvalidInputException(propertiesManager.getFeeMatrixRatesNotDefinedCode(),
								propertiesManager.getFeeMatrixRatesNotDefinedErrorMsg(),
								requestInfoWrapper.getRequestInfo());

					}

				}

			}
			
			if(feeType == null){
				
				throw new CustomInvalidInputException(propertiesManager.getFeeMatrixlicenseFeeTypeNotDefinedCode(),
						propertiesManager.getFeeMatrixlicenseFeeTypeNotDefinedErrorMsg(), requestInfoWrapper.getRequestInfo());
			}

			if (isRateExists && rate != null && rateType != null && license.getApplication() != null) {

				if (rateType.equalsIgnoreCase(RateTypeEnum.UNIT_BY_RANGE.toString())) {

					licenseFee = (rate * quantity);

				} else if (rateType.equalsIgnoreCase(RateTypeEnum.FLAT_BY_RANGE.toString())) {

					licenseFee = rate;

				} else if (rateType.equalsIgnoreCase(RateTypeEnum.FLAT_BY_PERCENTAGE.toString())) {

					licenseFee = (rate * quantity) / 100;
				}

				if(licenseFee == null || licenseFee == 0){
					
					throw new CustomInvalidInputException(propertiesManager.getLicenseFeeNotZeroCode(),
							propertiesManager.getLicenseFeeNotZeroErrorMsg(), requestInfoWrapper.getRequestInfo());
					
				} else {
					
					license.getApplication().setLicenseFee(licenseFee);
				}
				
			}

		} else {

			throw new CustomInvalidInputException(propertiesManager.getFeeMatrixNotDefinedCode(),
					propertiesManager.getFeeMatrixNotDefinedErrorMsg(), requestInfoWrapper.getRequestInfo());

		}
	}
	
	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {

		ResponseInfo responseInfo = new ResponseInfo();
		String apiId = requestInfo.getApiId();
		responseInfo.setApiId(apiId);
		String ver = requestInfo.getVer();
		responseInfo.setVer(ver);
		Long ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs();

		responseInfo.setTs(ts);
		String resMsgId = "uief87324";
		responseInfo.setResMsgId(resMsgId);
		String msgId = requestInfo.getMsgId();
		responseInfo.setMsgId(msgId);
		String responseStatus = (success.booleanValue()) ? "SUCCESSFUL" : "FAILED";
		responseInfo.setStatus(responseStatus);

		return responseInfo;
	}

	public RequestInfo createRequestInfoFromResponseInfo(ResponseInfo responseInfo) {

		RequestInfo requestInfo = new RequestInfo();
		String apiId = responseInfo.getApiId();
		requestInfo.setApiId(apiId);
		String ver = responseInfo.getVer();
		requestInfo.setVer(ver);
		Long ts = null;
		if (responseInfo.getTs() != null)
			ts = responseInfo.getTs();

		requestInfo.setTs(ts);
		String msgId = responseInfo.getMsgId();
		requestInfo.setMsgId(msgId);
		requestInfo.setUserInfo(new UserInfo());
		return requestInfo;
	}

	@Transactional
	public void updateTradeLicenseAfterCollection(DemandResponse demandResponse) {

		LicenseStatusResponse nextStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(createRequestInfoFromResponseInfo(demandResponse.getResponseInfo()));
		LicenseSearch licenseSearch = LicenseSearch.builder().applicationNumber(demandResponse.getDemands().get(0).getConsumerCode())
										.tenantId(demandResponse.getDemands().get(0).getTenantId()).build();
		TradeLicense license = findLicense(licenseSearch);
		log.debug("license.getApplication().getStatus()" + license.getApplication().getStatus());
		if (license.getApplication().getStatus() != null && license.getApplication().getStatus().
				equalsIgnoreCase(NewLicenseStatus.ACKNOWLEDGED.toString())) {
			nextStatus = statusRepository.findByModuleTypeAndCode(demandResponse.getDemands().get(0).getTenantId(),
					NEW_LICENSE_MODULE_TYPE, NewLicenseStatus.APPLICATION_FEE_PAID.getName(), requestInfoWrapper);
		} else {
			nextStatus = statusRepository.findByModuleTypeAndCode(demandResponse.getDemands().get(0).getTenantId(),
					NEW_LICENSE_MODULE_TYPE, NewLicenseStatus.LICENSE_FEE_PAID.getName(), requestInfoWrapper);
		}
		if (demandResponse != null && null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {
			
			Demand demand = demandResponse.getDemands().get(0);
			
			if (demand != null && demand.getBusinessService() != null
					&& ("TRADELICENSE".equals(demand.getBusinessService())
							|| "TLAPPLNFEE".equals(demand.getBusinessService()))) {
				
				log.debug(demand.toString());
				tradeLicenseRepository.updateTradeLicenseAfterWorkFlowQuery(demand.getConsumerCode(),
						nextStatus.getLicenseStatuses().get(0).getCode().toString());
				
			} else {
				
				log.debug("Demand is null in Trade License service");
			}
		}
	}

	public TradeLicense findLicense(LicenseSearch domain) {

		return tradeLicenseRepository.findLicense(domain);
	}

}
