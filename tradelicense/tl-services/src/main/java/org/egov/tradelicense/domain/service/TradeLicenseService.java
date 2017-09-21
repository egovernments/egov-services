package org.egov.tradelicense.domain.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchContract;
import org.egov.tl.commons.web.contract.FeeMatrixSearchResponse;
import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.WorkFlowDetails;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.enums.LicenseStatus;
import org.egov.tradelicense.domain.enums.NewLicenseStatus;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.repository.LicenseApplicationRepository;
import org.egov.tradelicense.domain.repository.LicenseFeeDetailRepository;
import org.egov.tradelicense.domain.repository.SupportDocumentRepository;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.contract.Demand;
import org.egov.tradelicense.web.contract.DemandResponse;
import org.egov.tradelicense.web.repository.FeeMatrixRepository;
import org.egov.tradelicense.web.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

	private BindingResult validate(List<TradeLicense> tradeLicenses, BindingResult errors) {

		try {
			Assert.notNull(tradeLicenses, "tradeLicenses to create must not be null");
			for (TradeLicense tradeLicense : tradeLicenses) {
				validator.validate(tradeLicense, errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}

		return errors;

	}

	@Transactional
	public List<TradeLicense> add(List<TradeLicense> tradeLicenses, RequestInfo requestInfo, BindingResult errors) {

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}

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

			if (license.getIsLegacy()) {

				// set the id's for all the available fee details of the
				// application
				setIdsForLicenseApplicationFeeDetails(license);
				// set the license status for legacy applications
				setLicenseStatus(license, requestInfo);
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

		validate(tradeLicenses, errors);

		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
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
	public TradeLicense save(TradeLicense tradeLicense) {
		return tradeLicenseRepository.save(tradeLicense);
	}

	@Transactional
	public TradeLicense update(TradeLicense tradeLicense, RequestInfo requestInfo) {

		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);

		if (null != tradeLicense.getApplication().getStatus())
			currentStatus = statusRepository.findByIds(tradeLicense.getTenantId(),
					tradeLicense.getApplication().getStatus().toString(), requestInfoWrapper);

		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty() && currentStatus.getLicenseStatuses()
				.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.INSPECTION_COMPLETED.getName())) {

			DemandResponse demandResponse = null;

			try {
				demandResponse = licenseBillService.createBill(tradeLicense, requestInfo);
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
						demandResponse.getDemands().get(0).getId(), license.getTenantId(), auditDetails);

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
		return tradeLicenseRepository.update(tradeLicense);
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

				license.setStatus(currentStatus.getLicenseStatuses().get(0).getId());
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
					&& currentStatus.getLicenseStatuses().get(0).getId() != null) {

				license.getApplication().setStatus(currentStatus.getLicenseStatuses().get(0).getId().toString());
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
				workFlowDetails.setSenderName(requestInfo.getUserInfo().getUsername());
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
			currentStatus = statusRepository.findByIds(license.getTenantId(),
					license.getApplication().getStatus().toString(), requestInfoWrapper);
		}

		if (null != requestInfo && null != requestInfo.getApiId()
				&& !requestInfo.getApiId().equalsIgnoreCase("org.egov.cs.tl")) {

			if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
					&& !currentStatus.getLicenseStatuses().isEmpty()
					&& workFlowDetails.getAction().equalsIgnoreCase("Forward") && currentStatus.getLicenseStatuses()
							.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.ACKNOWLEDGED.getName())) {

				nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
						NewLicenseStatus.SCRUTINY_COMPLETED.getName(), requestInfoWrapper);

				if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

					license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
					populateLicenseFeeCalculatedValue(license, requestInfo);
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

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
				populateLicenseFeeCalculatedValue(license, requestInfo);
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Forward") && currentStatus.getLicenseStatuses().get(0)
						.getCode().equalsIgnoreCase(NewLicenseStatus.SCRUTINY_COMPLETED.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.INSPECTION_COMPLETED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Approve") && currentStatus.getLicenseStatuses().get(0)
						.getCode().equalsIgnoreCase(NewLicenseStatus.INSPECTION_COMPLETED.getName())) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.FINAL_APPROVAL_COMPLETED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {
				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
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

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());

			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Reject")) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.REJECTED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
			}

		}

		if (null != workFlowDetails && null != workFlowDetails.getAction() && null != currentStatus
				&& !currentStatus.getLicenseStatuses().isEmpty()
				&& workFlowDetails.getAction().equalsIgnoreCase("Cancel")) {

			nextStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(), NEW_LICENSE_MODULE_TYPE,
					NewLicenseStatus.CANCELLED.getName(), requestInfoWrapper);

			if (null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {

				license.getApplication().setStatus(nextStatus.getLicenseStatuses().get(0).getId().toString());
			}

		}
	}

	private void populateLicenseFeeCalculatedValue(TradeLicense license, RequestInfo requestInfo) {
		
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		
		FeeMatrixSearchResponse feeMatrixSearchResponse = feeMatrixRepository.findFeeMatrix(license, requestInfoWrapper);
		
		if (feeMatrixSearchResponse != null && feeMatrixSearchResponse.getFeeMatrices() != null
				&& feeMatrixSearchResponse.getFeeMatrices().size() > 0) {

			Double quantity = license.getQuantity();
			Double rate = null;
			String rateType = "";
			Double licenseFee = null;
			
			for(FeeMatrixSearchContract feeMatrix : feeMatrixSearchResponse.getFeeMatrices()){
				
				if(feeMatrix.getFeeMatixDetails() != null && !feeMatrix.getFeeMatixDetails().isEmpty()){	
					
					for(FeeMatrixDetailContract feeMatrixDetail: feeMatrix.getFeeMatixDetails()){
						
						if(feeMatrixDetail.getUomFrom() != null && feeMatrixDetail.getUomTo() != null){
							
							if(quantity >= feeMatrixDetail.getUomFrom() && quantity < feeMatrixDetail.getUomTo()){
								
								if(feeMatrix.getRateType() != null){
									
									rateType = feeMatrix.getRateType().toString();
								}
								
								rate = feeMatrixDetail.getAmount();
							}
						}
					}
				}
			}
			
			if(rate != null && rateType != null && license.getApplication() != null){
				
				if(rateType.equalsIgnoreCase(RateTypeEnum.UNIT_BY_RANGE.toString())){
					
					licenseFee = (rate * quantity);
					
				} else if(rateType.equalsIgnoreCase(RateTypeEnum.FLAT_BY_RANGE.toString())){
					
					licenseFee = rate;
					
				} else if(rateType.equalsIgnoreCase(RateTypeEnum.FLAT_BY_PERCENTAGE.toString())){
					
					licenseFee = (rate * quantity)/100;
				}
				
				if(license.getValidityYears() != null){
					licenseFee = licenseFee * license.getValidityYears();
				}
				
				license.getApplication().setLicenseFee(licenseFee);
			}
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

		return requestInfo;
	}

	@Transactional
	public void updateTradeLicenseAfterCollection(DemandResponse demandResponse) {

		LicenseStatusResponse nextStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(createRequestInfoFromResponseInfo(demandResponse.getResponseInfo()));
		nextStatus = statusRepository.findByModuleTypeAndCode(demandResponse.getDemands().get(0).getTenantId(),
				NEW_LICENSE_MODULE_TYPE, NewLicenseStatus.LICENSE_FEE_PAID.getName(), requestInfoWrapper);
		
		if (demandResponse != null && null != nextStatus && !nextStatus.getLicenseStatuses().isEmpty()) {
			
			Demand demand = demandResponse.getDemands().get(0);
			
			if (demand != null && demand.getBusinessService() != null
					&& "TRADELICENSE".equals(demand.getBusinessService())) {
				
				log.debug(demand.toString());
				tradeLicenseRepository.updateTradeLicenseAfterWorkFlowQuery(demand.getConsumerCode(),
						nextStatus.getLicenseStatuses().get(0).getId().toString());
				
			} else {
				
				log.debug("Demand is null in Trade License service");
			}
		}
	}

	public TradeLicense findLicense(LicenseSearch domain) {

		return tradeLicenseRepository.findLicense(domain);
	}

}