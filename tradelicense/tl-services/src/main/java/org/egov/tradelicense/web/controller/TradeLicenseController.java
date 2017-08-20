package org.egov.tradelicense.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.contract.TradeLicenseContract;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.requests.TradeLicenseRequest;
import org.egov.tl.commons.web.requests.TradeLicenseResponse;
import org.egov.tl.commons.web.requests.TradeLicenseSearchResponse;
import org.egov.tradelicense.common.config.PropertiesManager;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;
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
	private SmartValidator validator;
	
	@Autowired
	PropertiesManager propertiesManager;

	private BindingResult validate(List<TradeLicenseContract> tradeLicenses, BindingResult errors) {

		try {
			Assert.notNull(tradeLicenses, "tradeLicenses to create must not be null");
			for (TradeLicenseContract tradeLicense : tradeLicenses) {
				validator.validate(tradeLicense, errors);
			}
		} catch (IllegalArgumentException e) {
			errors.addError(new ObjectError("Missing data", e.getMessage()));
		}

		return errors;

	}
	
	@RequestMapping(path = "/license/v1/_create", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

//		validate(tradeLicenseRequest.getLicenses(), errors);
		if (errors.hasErrors()) {
			throw new CustomBindException(errors, tradeLicenseRequest.getRequestInfo());
		}
		
		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		ModelMapper model = new ModelMapper();
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(requestInfo));
		if(tradeLicenseResponse.getResponseInfo() != null){
			tradeLicenseResponse.getResponseInfo().setStatus(propertiesManager.getLegacyCreateSuccessMessage());
		}
		List<TradeLicense> tradeLicenses = new ArrayList<>();
		TradeLicense tradeLicense;
		
		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicenses()) {

			tradeLicense = new TradeLicense();
			model.map(tradeLicenseContract, tradeLicense);
			AuditDetails auditDetails = new AuditDetails();
			auditDetails.setCreatedTime(new Date().getTime());
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
		tradeLicenseService.addToQue(tradeLicenseRequest);
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return tradeLicenseResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		// return
		// ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
		// .resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
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
				active, ids, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber,
				emailId, propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);
	}

}
