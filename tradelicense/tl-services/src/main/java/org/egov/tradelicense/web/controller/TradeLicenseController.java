package org.egov.tradelicense.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.LicenseApplicationContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.TradeLicenseResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotEmptyException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotFoundException;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeLicenseController {

	@Autowired
	TradeLicenseService tradeLicenseService;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "/license/v1/_create", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

		// validate(tradeLicenseRequest.getLicenses(), errors);
		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// check for existence of licenses
		if (tradeLicenseRequest.getLicenses() == null) {
			throw new TradeLicensesNotFoundException(propertiesManager.getTradeLicensesNotFoundMsg(), requestInfo);
		} else if (tradeLicenseRequest.getLicenses().size() == 0) {
			throw new TradeLicensesNotEmptyException(propertiesManager.getTradeLicensesNotEmptyMsg(), requestInfo);
		}

		ModelMapper model = new ModelMapper();
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(requestInfo));
		List<TradeLicense> tradeLicenses = new ArrayList<>();
		TradeLicense tradeLicense;

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			tradeLicense = new TradeLicense();
			this.setApplicationContract(tradeLicenseContract);
			model.getConfiguration().setAmbiguityIgnored(true);
			model.map(tradeLicenseContract, tradeLicense);
			//preparing audit details
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedTime(new Date().getTime());
			auditDetails.setLastModifiedTime(new Date().getTime());
			if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
				auditDetails.setCreatedBy(requestInfo.getUserInfo().getId().toString());
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}

			tradeLicense.setAuditDetails(auditDetails);

			tradeLicenses.add(tradeLicense);
		}

		tradeLicenses = tradeLicenseService.add(tradeLicenses, requestInfo, errors);

		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<>();
		TradeLicenseContract contract;

		for (TradeLicense f : tradeLicenses) {
			contract = new TradeLicenseContract();
			model.map(f, contract);
			tradeLicenseContracts.add(contract);
		}

		tradeLicenseRequest.setLicenses(tradeLicenseContracts);
		tradeLicenseService.addToQue(tradeLicenseRequest, true);
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);

		// creating the success message for license create
		if (tradeLicenseResponse.getResponseInfo() != null && tradeLicenseResponse.getLicenses() != null
				&& tradeLicenseResponse.getLicenses().size() > 0) {

			List<TradeLicenseContract> licenses = tradeLicenseResponse.getLicenses();
			int licenseCount = licenses.size();
			String legacyText = "";
			String newTradeText = "";
			String licenseNumbers = " ";
			String applicationNumbers = " ";

			for (int i = 0; i < licenseCount; i++) {

				if (licenses.get(i).getIsLegacy()) {

					if (legacyText.isEmpty()) {
						legacyText = propertiesManager.getLegacyCreateSuccessMessage();
					}

					if (licenses.get(i).getLicenseNumber() != null) {
						licenseNumbers = licenseNumbers.concat(licenses.get(i).getLicenseNumber());
						if (i != (licenseCount - 1)) {
							licenseNumbers = licenseNumbers.concat(", ");
						}
					}
				} else {

					if (newTradeText.isEmpty()) {
						newTradeText = propertiesManager.getNewTradeLicenseCreateSuccessMessage();
					}

					if (licenses.get(i).getApplication().getApplicationNumber() != null) {
						applicationNumbers = applicationNumbers.concat(licenses.get(i).getApplication().getApplicationNumber());
						if (i != (licenseCount - 1)) {
							applicationNumbers = applicationNumbers.concat(", ");
						}
					}
				}
			}

			if (legacyText != null && !legacyText.isEmpty()) {

				legacyText = legacyText + licenseNumbers;
			}

			if (newTradeText != null && !newTradeText.isEmpty()) {

				newTradeText = newTradeText + applicationNumbers;
			}

			tradeLicenseResponse.getResponseInfo().setStatus(legacyText + newTradeText);
		}

		return tradeLicenseResponse;
	}

	@RequestMapping(path = "/license/v1/_update", method = RequestMethod.POST)
	public TradeLicenseResponse updateTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, requestInfo);
		}
		// check for existence of licenses
		if (tradeLicenseRequest.getLicenses() == null) {
			throw new TradeLicensesNotFoundException(propertiesManager.getTradeLicensesNotFoundMsg(), requestInfo);
		} else if (tradeLicenseRequest.getLicenses().size() == 0) {
			throw new TradeLicensesNotEmptyException(propertiesManager.getTradeLicensesNotEmptyMsg(), requestInfo);
		}

		ModelMapper model = new ModelMapper();
		model.getConfiguration().setAmbiguityIgnored(true);
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(requestInfo));
		List<TradeLicense> tradeLicenses = new ArrayList<>();
		TradeLicense tradeLicense;

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			tradeLicense = new TradeLicense();
			this.setApplicationContract(tradeLicenseContract);
			model.map(tradeLicenseContract, tradeLicense);
			AuditDetails auditDetails = tradeLicense.getAuditDetails();
			if (auditDetails == null) {
				auditDetails = new AuditDetails();
			}
			auditDetails.setLastModifiedTime(new Date().getTime());
			if (requestInfo != null && requestInfo.getUserInfo() != null && requestInfo.getUserInfo().getId() != null) {
				auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getId().toString());
			}

			tradeLicense.setAuditDetails(auditDetails);

			tradeLicenses.add(tradeLicense);
		}

		tradeLicenses = tradeLicenseService.update(tradeLicenses, requestInfo, errors);

		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<>();
		TradeLicenseContract contract;

		for (TradeLicense f : tradeLicenses) {
			contract = new TradeLicenseContract();
			model.map(f, contract);
			tradeLicenseContracts.add(contract);
		}

		tradeLicenseRequest.setLicenses(tradeLicenseContracts);
		tradeLicenseService.addToQue(tradeLicenseRequest, false);
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return tradeLicenseResponse;
	}

	/**
	 * Temporary method to populate applicationType, applicationNumber,applicationDate, feeDetails, supportDocuments from 
	 * TradeLicenseContract to ApplicaitonContract.
	 * @param license
	 */
	private void setApplicationContract( TradeLicenseContract license){
		
		LicenseApplicationContract applicationContract = license.getApplication();
		if( applicationContract == null ){
			 applicationContract = new LicenseApplicationContract();
		}
		
		if( license.getApplicationType() != null && applicationContract.getApplicationType() == null){
			applicationContract.setApplicationType( license.getApplicationType());
		}
		
		if( license.getApplicationDate() != null && applicationContract.getApplicationDate() == null ){
			applicationContract.setApplicationDate( license.getApplicationDate() );
		}
		
		if( license.getApplicationNumber() != null && applicationContract.getApplicationNumber() == null ){
			applicationContract.setApplicationNumber( license.getApplicationNumber() );
		}
		
		if( license.getFeeDetails() != null && applicationContract.getFeeDetails() == null ){
			applicationContract.setFeeDetails( license.getFeeDetails() );
		}
		
		if( license.getSupportDocuments() != null && applicationContract.getSupportDocuments() == null ){
			applicationContract.setSupportDocuments( license.getSupportDocuments() );
		}
		
		if( license.getTenantId() != null && applicationContract.getTenantId() == null ){
			applicationContract.setTenantId( license.getTenantId() );
		}
		
		if( license.getApplicationStatus() != null && applicationContract.getStatus() != null){
			applicationContract.setStatus( license.getApplicationStatus().toString());
		}
		
		// for legacy license field inspection details should not be allowed to update
		if( license.getIsLegacy()){
			applicationContract.setLicenseFee(null);
			applicationContract.setFieldInspectionReport(null);
		}
		license.setApplication(applicationContract);
		
	}
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
	}

	@RequestMapping(path = "/license/v1/_search", method = RequestMethod.POST)
	public TradeLicenseSearchResponse searchTradelicense(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) String sort,
			@RequestParam(required = false) String active, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String applicationNumber,
			@RequestParam(required = false) String licenseNumber,
			@RequestParam(required = false) String oldLicenseNumber,
			@RequestParam(required = false) String mobileNumber, @RequestParam(required = false) String aadhaarNumber,
			@RequestParam(required = false) String emailId, @RequestParam(required = false) String propertyAssesmentNo,
			@RequestParam(required = false) Integer adminWard, @RequestParam(required = false) Integer locality,
			@RequestParam(required = false) String ownerName, @RequestParam(required = false) String tradeTitle,
			@RequestParam(required = false) String tradeType, @RequestParam(required = false) Integer tradeCategory,
			@RequestParam(required = false) Integer tradeSubCategory, @RequestParam(required = false) String legacy,
			@RequestParam(required = false) Integer status) throws Exception {

		return tradeLicenseService.getTradeLicense(requestInfo.getRequestInfo(), tenantId, pageSize, pageNumber, sort,
				active, ids, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, emailId,
				propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);
	}

}
