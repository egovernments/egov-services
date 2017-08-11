package org.egov.tradelicense.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.web.contract.TradeLicenseContract;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
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

	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@RequestBody TradeLicenseRequest tradeLicenseRequest,
			BindingResult errors) throws Exception {

		if (errors.hasErrors()) {
			throw new CustomBindException(errors);
		}

		ModelMapper model = new ModelMapper();
		TradeLicenseResponse tradeLicenseResponse = new TradeLicenseResponse();
		tradeLicenseResponse.setResponseInfo(getResponseInfo(tradeLicenseRequest.getRequestInfo()));
		List<TradeLicense> tradeLicenses = new ArrayList<>();
		TradeLicense tradeLicense;

		RequestInfo requestInfo = tradeLicenseRequest.getRequestInfo();
		for (TradeLicenseContract tradeLicenseContract : tradeLicenseRequest.getLicesnses()) {
			
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

		tradeLicenseRequest.setLicesnses(tradeLicenseContracts);
		tradeLicenseService.addToQue(tradeLicenseRequest);
		tradeLicenseResponse.setLicenses(tradeLicenseContracts);

		return tradeLicenseResponse;
	}

	private ResponseInfo getResponseInfo(RequestInfo requestInfo) {

		return ResponseInfo.builder().apiId(requestInfo.getApiId()).ver(requestInfo.getVer())
				.resMsgId(requestInfo.getMsgId()).resMsgId("placeholder").status("placeholder").build();
	}

	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId,
			@RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer pageNumber,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String active,
			@RequestParam(required = false) String tradeLicenseId,
			@RequestParam(required = false) String applicationNumber,
			@RequestParam(required = false) String licenseNumber,
			@RequestParam(required = false) String mobileNumber,
			@RequestParam(required = false) String aadhaarNumber,
			@RequestParam(required = false) String emailId,
			@RequestParam(required = false) String propertyAssesmentNo,
			@RequestParam(required = false) Integer revenueWard,
			@RequestParam(required = false) Integer locality,
			@RequestParam(required = false) String ownerName,
			@RequestParam(required = false) String tradeTitle,
			@RequestParam(required = false) String tradeType,
			@RequestParam(required = false) Integer tradeCategory,
			@RequestParam(required = false) Integer tradeSubCategory,
			@RequestParam(required = false) String legacy,
			@RequestParam(required = false) Integer status) throws Exception {

		return tradeLicenseService.getTradeLicense(requestInfo.getRequestInfo(), tenantId, pageSize, pageNumber, sort, active, tradeLicenseId, applicationNumber, licenseNumber, mobileNumber, aadhaarNumber, emailId, propertyAssesmentNo, revenueWard, locality, ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory, legacy, status);
	}


}
