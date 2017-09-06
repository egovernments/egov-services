package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.contract.WorkFlowDetails;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.enums.LicenseStatus;
import org.egov.tradelicense.domain.enums.NewLicenseStatus;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetail;
import org.egov.tradelicense.domain.model.SupportDocument;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.model.TradeLicenseSearch;
import org.egov.tradelicense.domain.repository.TradeLicenseRepository;
import org.egov.tradelicense.domain.repository.builder.LicenseBillQueryBuilder;
import org.egov.tradelicense.domain.service.validator.TradeLicenseServiceValidator;
import org.egov.tradelicense.web.contract.Demand;
import org.egov.tradelicense.web.contract.DemandResponse;
import org.egov.tradelicense.web.repository.StatusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
import org.springframework.web.client.RestTemplate;

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
	private TradeLicenseNumberGeneratorService licenseNumberGenerationService;

	@Autowired
	private ApplicationNumberGeneratorService applNumberGenrationService;

	@Autowired
	private SmartValidator validator;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private StatusRepository statusRepository;

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

		// external end point validations
		tradeLicenseServiceValidator.validateCreateTradeLicenseRelated(tradeLicenses, requestInfo);

		// setting the id for the license and support document and fee details
		for (TradeLicense license : tradeLicenses) {

			// preparing audit details
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedTime(new Date().getTime());
			auditDetails.setLastModifiedTime(new Date().getTime());
			if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
				auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}

			license.setAuditDetails(auditDetails);

			license.setId(tradeLicenseRepository.getNextSequence());
			license.getApplication().setId(tradeLicenseRepository.getApplicationNextSequence());
			license.getApplication().setLicenseId(license.getId());

			RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
			requestInfoWrapper.setRequestInfo(requestInfo);

			if (!license.getIsLegacy()) {
				
				LicenseStatusResponse currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(),
						NEW_LICENSE_MODULE_TYPE, NewLicenseStatus.ACKNOWLEDGED.getName(), requestInfoWrapper);

				// checking the application status and setting to the
				// application
				if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {
					
					if (currentStatus.getLicenseStatuses().size() > 0) {
						
						license.getApplication().setStatus(currentStatus.getLicenseStatuses().get(0).getId().toString());
					}
				}

				populateWorkFlowDetails(license, requestInfo);

			} else {

				LicenseStatusResponse currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(),
						LICENSE_MODULE_TYPE, LicenseStatus.APPROVED.getName(), requestInfoWrapper);

				if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

					if (currentStatus.getLicenseStatuses().size() > 0) {

						license.setStatus(currentStatus.getLicenseStatuses().get(0).getId());
					}

				}

			}

			if (license.getApplication().getSupportDocuments() != null
					&& license.getApplication().getSupportDocuments().size() > 0) {

				for (SupportDocument supportDocument : license.getApplication().getSupportDocuments()) {

					// supportDocument.setLicenseId(license.getId());
					supportDocument.setApplicationId(license.getApplication().getId());
					supportDocument.setId(tradeLicenseRepository.getSupportDocumentNextSequence());
					if (license.getAuditDetails() != null) {
						supportDocument.setAuditDetails(license.getAuditDetails());
					}

				}
			}
			if (license.getIsLegacy()) {

				if (license.getApplication().getFeeDetails() != null
						&& license.getApplication().getFeeDetails().size() > 0) {

					for (LicenseFeeDetail feeDetail : license.getApplication().getFeeDetails()) {

						// feeDetail.setLicenseId(license.getId());
						feeDetail.setApplicationId(license.getApplication().getId());
						feeDetail.setId(tradeLicenseRepository.getFeeDetailNextSequence());
						if (license.getAuditDetails() != null) {
							feeDetail.setAuditDetails(license.getAuditDetails());
						}
					}
				}
			} else {
				// clearing the unnecessary details for new trade license
				license.getApplication().setFeeDetails(new ArrayList<>());
				license.setOldLicenseNumber(null);
				license.setLicenseNumber(null);
			}

			if (license.getIsLegacy()) {

				license.setLicenseNumber(licenseNumberGenerationService.generate(license.getTenantId(), requestInfo));

			} else {

				if (license.getApplication().getApplicationNumber() == null
						|| (license.getApplication().getApplicationNumber() != null
								&& license.getApplication().getApplicationNumber().isEmpty())) {

					license.getApplication().setApplicationNumber(
							applNumberGenrationService.generate(license.getTenantId(), requestInfo));
				}

				if (license.getApplication().getApplicationDate() == null) {

					license.getApplication().setApplicationDate(System.currentTimeMillis());
				}

			}

		}

		return tradeLicenses;
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

	public void addToQue(TradeLicenseRequest request, Boolean isNewRecord) {
		tradeLicenseRepository.add(request, isNewRecord);
	}

	@Transactional
	public TradeLicense save(TradeLicense tradeLicense) {
		return tradeLicenseRepository.save(tradeLicense);
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

			if (!license.getIsLegacy()) {

				populateWorkFlowDetails(license, requestInfo);
				populateNextStatus(license, requestInfo);

			} else {

				RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
				requestInfoWrapper.setRequestInfo(requestInfo);

				LicenseStatusResponse currentStatus = statusRepository.findByModuleTypeAndCode(license.getTenantId(),
						LICENSE_MODULE_TYPE, LicenseStatus.APPROVED.getName(), requestInfoWrapper);

				if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()) {

					if (currentStatus.getLicenseStatuses().size() > 0) {

						license.setStatus(currentStatus.getLicenseStatuses().get(0).getId());
					}

				}
			}

		}

		return tradeLicenses;
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
	}

	@Transactional
	public TradeLicense update(TradeLicense tradeLicense, RequestInfo requestInfo) {
		LicenseStatusResponse currentStatus = null;
		RequestInfoWrapper requestInfoWrapper = new RequestInfoWrapper();
		requestInfoWrapper.setRequestInfo(requestInfo);
		System.out.println("TenantId: " + tradeLicense.getTenantId() + "Status: " + tradeLicense.getApplication().getStatus().toString());
		if (null != tradeLicense.getStatus())
			currentStatus = statusRepository.findByIds(tradeLicense.getTenantId(), tradeLicense.getApplication().getStatus().toString(),
					requestInfoWrapper);
		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty())
    		    System.out.println("Current Status: " + currentStatus.getLicenseStatuses()
                                    .get(0).getCode());

		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty() && currentStatus.getLicenseStatuses()
				.get(0).getCode().equalsIgnoreCase(NewLicenseStatus.INSPECTION_COMPLETED.getName())) {
			DemandResponse demandResponse = licenseBillService.createBill(tradeLicense, requestInfo);
			TradeLicenseSearch tradeLicenseSearch = tradeLicenseRepository.getByLicenseId(tradeLicense, requestInfo);
			System.out.println("Trade License Search: " + tradeLicenseSearch);
			if (tradeLicenseSearch != null) {
				final Object[] objValue = new Object[] { tradeLicenseSearch.getApplications().get(0).getId(),
						demandResponse.getDemands().get(0).getId(), tradeLicenseSearch.getTenantId(),
						Long.valueOf(tradeLicenseSearch.getAuditDetails().getCreatedBy()), new Date().getTime(),
						Long.valueOf(tradeLicenseSearch.getAuditDetails().getLastModifiedBy()), new Date().getTime() };
				tradeLicenseRepository.createLicenseBill(LicenseBillQueryBuilder.insertLicenseBill(), objValue);
			}
		}
		if (null != currentStatus && !currentStatus.getLicenseStatuses().isEmpty()
                        && currentStatus.getLicenseStatuses().get(0).getCode() == NewLicenseStatus.LICENSE_FEE_PAID.getName()) {
                        // generate license number and setting license number and
                        // license issued date
		        tradeLicense.setLicenseNumber(licenseNumberGenerationService.generate(tradeLicense.getTenantId(), requestInfo));
		        tradeLicense.setIssuedDate(System.currentTimeMillis());
                }
		return tradeLicenseRepository.update(tradeLicense);
	}

	public TradeLicenseSearchResponse getTradeLicense(RequestInfo requestInfo, String tenantId, Integer pageSize,
			Integer pageNumber, String sort, String active, Integer[] ids, String applicationNumber,
			String licenseNumber, String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status,
			Integer applicationStatus) {

		TradeLicenseSearchResponse tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		//
		// tradeLicenseSearchResponse = getLicensesFromEs(tenantId, pageSize,
		// pageNumber, sort, active, tradeLicenseId,
		// applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber,
		// aadhaarNumber, emailId,
		// propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle,
		// tradeType, tradeCategory,
		// tradeSubCategory, legacy, status);
		//
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		//
		// if (tradeLicenseSearchResponse != null &&
		// tradeLicenseSearchResponse.getLicenses() != null
		// && !(tradeLicenseSearchResponse.getLicenses().size() == 0)) {
		//
		// tradeLicenseSearchResponse.setResponseInfo(responseInfo);
		// return tradeLicenseSearchResponse;
		// }

		List<TradeLicenseSearch> licenses = tradeLicenseRepository.search(requestInfo, tenantId, pageSize, pageNumber,
				sort, active, ids, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber,
				emailId, propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status, applicationStatus);

		List<TradeLicenseSearchContract> tradeLicenseSearchContracts = new ArrayList<TradeLicenseSearchContract>();

		ModelMapper model = new ModelMapper();

		for (TradeLicenseSearch licenseSearch : licenses) {
			TradeLicenseSearchContract tradeSearchContract = new TradeLicenseSearchContract();
			model.map(licenseSearch, tradeSearchContract);
			tradeLicenseSearchContracts.add(tradeSearchContract);
		}

		if (tradeLicenseSearchResponse == null) {
			// instantiate for setting the values of db search
			tradeLicenseSearchResponse = new TradeLicenseSearchResponse();
		}
		tradeLicenseSearchResponse.setLicenses(tradeLicenseSearchContracts);
		tradeLicenseSearchResponse.setResponseInfo(responseInfo);

		return tradeLicenseSearchResponse;
	}

	private TradeLicenseSearchResponse getLicensesFromEs(String tenantId, Integer pageSize, Integer pageNumber,
			String sort, String active, String tradeLicenseId, String applicationNumber, String licenseNumber,
			String oldLicenseNumber, String mobileNumber, String aadhaarNumber, String emailId,
			String propertyAssesmentNo, Integer adminWard, Integer locality, String ownerName, String tradeTitle,
			String tradeType, Integer tradeCategory, Integer tradeSubCategory, String legacy, Integer status) {

		Map<String, Object> requestMap = new HashMap();
		StringBuilder url = new StringBuilder(propertiesManager.getTradeLicenseIndexerServiceHostName());
		url.append(propertiesManager.getTradeLicenseIndexerServiceBasePath());
		url.append(propertiesManager.getTradeLicenseIndexerLicenseSearchPath());

		url.append("?tenantId=" + tenantId + "&");

		if (pageSize != null) {
			url.append("pageSize=" + pageSize + "&");
		}

		if (pageNumber != null) {
			url.append("pageNumber=" + pageNumber + "&");
		}

		if (sort != null && !sort.isEmpty()) {
			url.append("sort=" + sort + "&");
		}

		if (active != null && !active.isEmpty()) {
			url.append("active=" + active + "&");
		}

		if (tradeLicenseId != null && !tradeLicenseId.isEmpty()) {
			url.append("tradeLicenseId=" + tradeLicenseId + "&");
		}

		if (applicationNumber != null && !applicationNumber.isEmpty()) {
			url.append("applicationNumber=" + applicationNumber + "&");
		}

		if (licenseNumber != null && !licenseNumber.isEmpty()) {
			url.append("licenseNumber=" + licenseNumber + "&");
		}

		if (oldLicenseNumber != null && !oldLicenseNumber.isEmpty()) {
			url.append("oldLicenseNumber=" + oldLicenseNumber + "&");
		}

		if (mobileNumber != null && !mobileNumber.isEmpty()) {
			url.append("mobileNumber=" + mobileNumber + "&");
		}
		if (aadhaarNumber != null && !aadhaarNumber.isEmpty()) {
			url.append("aadhaarNumber=" + aadhaarNumber + "&");
		}

		if (emailId != null && !emailId.isEmpty()) {
			url.append("emailId=" + emailId + "&");
		}

		if (propertyAssesmentNo != null && !propertyAssesmentNo.isEmpty()) {
			url.append("propertyAssesmentNo=" + propertyAssesmentNo + "&");
		}

		if (adminWard != null) {
			url.append("adminWard=" + adminWard + "&");
		}

		if (locality != null) {
			url.append("locality=" + locality + "&");
		}

		if (ownerName != null && !ownerName.isEmpty()) {
			url.append("ownerName=" + ownerName + "&");
		}

		if (tradeTitle != null && !tradeTitle.isEmpty()) {
			url.append("tradeTitle=" + tradeTitle + "&");
		}

		if (tradeType != null && !tradeType.isEmpty()) {
			url.append("tradeType=" + tradeType + "&");
		}

		if (tradeCategory != null) {
			url.append("tradeCategory=" + tradeCategory + "&");
		}

		if (tradeSubCategory != null) {
			url.append("tradeSubCategory=" + tradeSubCategory + "&");
		}

		if (legacy != null && !legacy.isEmpty()) {
			url.append("legacy=" + legacy + "&");
		}

		if (status != null) {
			url.append("status=" + status);
		} else {
			url.setLength(url.length() - 1);
		}

		TradeLicenseSearchResponse tradeLicenseSearchResponse = null;
		try {

			tradeLicenseSearchResponse = restTemplate.postForObject(url.toString(), requestMap,
					TradeLicenseSearchResponse.class);

		} catch (Exception e) {
			log.error("Error while connecting to the Tl-Indexer end point");
		}
		return tradeLicenseSearchResponse;
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

        public void updateTradeLicenseAfterCollection(DemandResponse demandResponse) {
            if (demandResponse != null) {
                Demand demand = demandResponse.getDemands().get(0);
                if (demand != null && demand.getBusinessService() != null && "TRADELICENSE".equals(demand.getBusinessService())) {
                    log.debug(demand.toString());
                    tradeLicenseRepository.updateTradeLicenseAfterWorkFlowQuery(demand.getConsumerCode());
                } else {
                    log.debug("Demand is null in Trade License service");
                }
            }
        }
        
        public TradeLicense searchByApplicationNumber(RequestInfo requestInfo, String applicationNumber) {
            return tradeLicenseRepository.searchByApplicationNumber(requestInfo, applicationNumber);
        }
}