package org.egov.tradelicense.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.LicenseApplicationContract;
import org.egov.tl.commons.web.contract.LicenseSearchContract;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.contract.TradeLicenseSearchContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.response.TradeLicenseResponse;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.CustomDataMigrationBindException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotEmptyException;
import org.egov.tradelicense.common.domain.exception.TradeLicensesNotFoundException;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseSearch;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.web.mapper.TradeLicenseMapper;
import org.egov.tradelicense.web.repository.TradeLicenseSearchContractRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradeLicenseController {

	@Autowired
	TradeLicenseService tradeLicenseService;

	@Autowired
	TradeLicenseSearchContractRepository tradeLicenseSearchContractRepository;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	PropertiesManager propertiesManager;

	@RequestMapping(path = "/license/v1/_create", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		// check for field validation errors
		if (errors.hasErrors()) {

			validateDataMigrationFieldErrors(tradeLicenseRequest, errors);
			
		}
		// check for existence of licenses
		validateExistanceOfLicenses(tradeLicenseRequest, requestInfo);
		// set default values for the licenses
		setDefaultValuesForLicense(tradeLicenseRequest, true);
		// instantiating model mapper
		ModelMapper model = new ModelMapper();
		// converting license contract models to domain models
		List<TradeLicense> tradeLicenses = new ArrayList<>();

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			TradeLicense tradeLicense = new TradeLicense();
			this.setApplicationContract(tradeLicenseContract);
			model.getConfiguration().setAmbiguityIgnored(true);
			model.map(tradeLicenseContract, tradeLicense);
			tradeLicenses.add(tradeLicense);
		}
		// processing the license create request to service
		tradeLicenseService.add(tradeLicenses, requestInfo, errors);
		// converting license domain models to contract models
		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<>();

		for (TradeLicense domain : tradeLicenses) {

			TradeLicenseContract contract = new TradeLicenseContract();
			model.map(domain, contract);
			tradeLicenseContracts.add(contract);
		}
		// pushing license request to kafka queue
		tradeLicenseRequest.setLicenses(tradeLicenseContracts);
		tradeLicenseService.addToQue(tradeLicenseRequest, true);
		// preparing license response object
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(requestInfo));
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);
		// creating the success message for license create
		if (tradeLicenseResponse.getResponseInfo() != null && tradeLicenseResponse.getLicenses() != null
				&& tradeLicenseResponse.getLicenses().size() > 0) {

			getLicenseCreateSuccessMessage(tradeLicenseResponse);
		}

		return tradeLicenseResponse;
	}

	@RequestMapping(path = "/license/v1/_update", method = RequestMethod.POST)
	public TradeLicenseResponse updateTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		// check for field validation errors
		if (errors.hasErrors()) {

			throw new CustomBindException(errors, requestInfo);
		}
		// check for existence of licenses
		validateExistanceOfLicenses(tradeLicenseRequest, requestInfo);
		// set default values for the licenses
		setDefaultValuesForLicense(tradeLicenseRequest, false);
		// instantiating model mapper
		ModelMapper model = new ModelMapper();
		model.getConfiguration().setAmbiguityIgnored(true);
		// converting license contract models to domain models
		List<TradeLicense> tradeLicenses = new ArrayList<>();

		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			TradeLicense tradeLicense = new TradeLicense();
			this.setApplicationContract(tradeLicenseContract);
			model.map(tradeLicenseContract, tradeLicense);
			// preparing license update audit details
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
		// processing the license update request to service
		tradeLicenses = tradeLicenseService.update(tradeLicenses, requestInfo, errors);
		// converting license domain models to contract models
		List<TradeLicenseContract> tradeLicenseContracts = new ArrayList<>();

		for (TradeLicense domain : tradeLicenses) {

			TradeLicenseContract contract = new TradeLicenseContract();
			model.map(domain, contract);
			tradeLicenseContracts.add(contract);
		}
		// pushing license request to kafka queue
		tradeLicenseRequest.setLicenses(tradeLicenseContracts);
		tradeLicenseService.addToQue(tradeLicenseRequest, false);
		// preparing license response object
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(requestInfo));
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return tradeLicenseResponse;
	}

	@RequestMapping(path = "/license/v1/_search", method = RequestMethod.POST)
	public TradeLicenseSearchResponse searchTradeLicense(
			@ModelAttribute @Valid final LicenseSearchContract licenseSearchContract,
			@RequestBody final RequestInfoWrapper requestInfoWrapper, final BindingResult errors) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (errors.hasErrors()) {

			throw new CustomBindException(errors, requestInfo);
		}

		final TradeLicenseMapper mapper = new TradeLicenseMapper();
		final LicenseSearch domain = mapper.toSearchDomain(licenseSearchContract);
		final List<TradeLicense> licenses = tradeLicenseService.search(domain);
		List<TradeLicenseSearchContract> tradeLicenseSearchContracts = new ArrayList<TradeLicenseSearchContract>();

		if (licenses != null && licenses.size() > 0) {

			tradeLicenseSearchContracts = tradeLicenseSearchContractRepository.toSearchContractList(requestInfo,
					licenses);
		}

		final TradeLicenseSearchResponse response = new TradeLicenseSearchResponse();
		response.setLicenses(tradeLicenseSearchContracts);
		response.setResponseInfo(getResponseInfo(requestInfo));

		return response;
	}

	private void setDefaultValuesForLicense(TradeLicenseRequest tradeLicenseRequest, Boolean isNewRecord) {
		
		// this method will set the default values for the license object
		if(tradeLicenseRequest != null && tradeLicenseRequest.getLicenses() != null
				&& tradeLicenseRequest.getLicenses().size() > 0){
			
			for( TradeLicenseContract tradeLicenseContract: tradeLicenseRequest.getLicenses()){
				
				if(tradeLicenseContract.getIsLegacy() == null){
					tradeLicenseContract.setIsLegacy(false);
				}
				if(tradeLicenseContract.getIsPropertyOwner() == null){
					tradeLicenseContract.setIsPropertyOwner(false);
				}
				if(tradeLicenseContract.getActive() == null){
					tradeLicenseContract.setActive(true);
				}
				if(tradeLicenseContract.getIsDataPorting() == null){
					tradeLicenseContract.setIsDataPorting(false);
				}
				//set validity years to 1 only in case of legacy creation and data porting is true and validity years is null
				if(isNewRecord && tradeLicenseContract.getIsLegacy() 
						&& tradeLicenseContract.getIsDataPorting() 
						&& (tradeLicenseContract.getValidityYears() == null)){
					
					tradeLicenseContract.setValidityYears(1l);
				}
			}
		}
		
	}

	private void validateDataMigrationFieldErrors(TradeLicenseRequest tradeLicenseRequest, BindingResult errors) {
		
		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		int optionalFieldCount = 0;

		if (errors.hasFieldErrors()) {
			
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();

			for (org.springframework.validation.FieldError err : fieldErrors) {
				
				TradeLicenseContract tlContract = null;
				String errField = err.getField().substring(err.getField().indexOf(".") + 1);
				String licenseIndex = err.getField().substring((err.getField().indexOf("[") + 1),
						(err.getField().indexOf("[") + 2));

				if (licenseIndex != null && !licenseIndex.isEmpty()) {

					Integer intVal = Integer.valueOf(licenseIndex);

					if (intVal != null && tradeLicenseRequest.getLicenses() != null
							&& tradeLicenseRequest.getLicenses().get(intVal) != null) {

						tlContract = tradeLicenseRequest.getLicenses().get(intVal);
					}

					if (tlContract != null && tlContract.getIsLegacy() != null
							&& tlContract.getIsLegacy() == Boolean.TRUE
							&& tlContract.getIsDataPorting() != null 
							&& tlContract.getIsDataPorting() == Boolean.TRUE) {

						if (errField != null && err.getCode() != null 
								&& (err.getCode().equalsIgnoreCase("NotEmpty") || err.getCode().equalsIgnoreCase("NotNull"))
								&& (errField.equalsIgnoreCase("ownerMobileNumber")
								|| errField.equalsIgnoreCase("fatherspousename")
								|| errField.equalsIgnoreCase("ownerEmailid")
								|| errField.equalsIgnoreCase("ownerAddress")
								|| errField.equalsIgnoreCase("ownerShipType")
								|| errField.equalsIgnoreCase("uom")
								|| errField.equalsIgnoreCase("tradeCommencementDate")
								|| errField.equalsIgnoreCase("adminWard")
								|| errField.equalsIgnoreCase("category")
								|| errField.equalsIgnoreCase("subCategory")
								|| errField.equalsIgnoreCase("validityYears"))) {

							optionalFieldCount++;

						}
					}
				}
			}

			if (optionalFieldCount != fieldErrors.size()) {
				
				throw new CustomDataMigrationBindException(errors, requestInfo, tradeLicenseRequest);
			}

		}
	}
	
	// validating the existence of licenses
	private void validateExistanceOfLicenses(TradeLicenseRequest tradeLicenseRequest, RequestInfo requestInfo) {

		if (tradeLicenseRequest.getLicenses() == null) {

			throw new TradeLicensesNotFoundException(propertiesManager.getTradeLicensesNotFoundMsg(), requestInfo);

		} else if (tradeLicenseRequest.getLicenses().size() == 0) {

			throw new TradeLicensesNotEmptyException(propertiesManager.getTradeLicensesNotEmptyMsg(), requestInfo);

		}
	}

	/**
	 * Temporary method to populate applicationType,
	 * applicationNumber,applicationDate, feeDetails, supportDocuments from
	 * TradeLicenseContract to ApplicaitonContract.
	 * 
	 * @param license
	 */
	private void setApplicationContract(TradeLicenseContract license) {

		LicenseApplicationContract applicationContract = license.getApplication();
		if (applicationContract == null) {
			applicationContract = new LicenseApplicationContract();
		}

		if (license.getApplicationType() != null && applicationContract.getApplicationType() == null) {
			applicationContract.setApplicationType(license.getApplicationType());
		}

		if (license.getApplicationDate() != null && applicationContract.getApplicationDate() == null) {
			applicationContract.setApplicationDate(license.getApplicationDate());
		}

		if (license.getApplicationNumber() != null && applicationContract.getApplicationNumber() == null) {
			applicationContract.setApplicationNumber(license.getApplicationNumber());
		}

		if (license.getFeeDetails() != null && applicationContract.getFeeDetails() == null) {
			applicationContract.setFeeDetails(license.getFeeDetails());
		}

		if (license.getSupportDocuments() != null && applicationContract.getSupportDocuments() == null) {
			applicationContract.setSupportDocuments(license.getSupportDocuments());
		}

		if (license.getTenantId() != null && applicationContract.getTenantId() == null) {
			applicationContract.setTenantId(license.getTenantId());
		}

		// for legacy license field inspection details should not be allowed to
		// update
		if (license.getIsLegacy()) {
			applicationContract.setLicenseFee(null);
			applicationContract.setFieldInspectionReport(null);
		}
		license.setApplication(applicationContract);

	}

	// get responseinfo from requestinfo
	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
	}

	// preparing the success message for the trade license creation
	private void getLicenseCreateSuccessMessage(TradeLicenseResponse tradeLicenseResponse) {

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
					applicationNumbers = applicationNumbers
							.concat(licenses.get(i).getApplication().getApplicationNumber());
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

}
