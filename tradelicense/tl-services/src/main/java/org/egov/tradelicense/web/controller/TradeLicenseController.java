package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tradelicense.common.domain.model.RequestInfoWrapper;
import org.egov.tradelicense.domain.service.TradeLicenseService;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/tradelicense")
public class TradeLicenseController {
	
	@Autowired
	TradeLicenseService tradeLicenseService;
	
	
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(@Valid @RequestBody TradeLicenseRequest tradeLicenseRequest) throws Exception {

		return tradeLicenseService.createTradeLicense(tradeLicenseRequest);
	}
	
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public TradeLicenseResponse createTradelicense(
	@RequestBody RequestInfoWrapper requestInfo,
	@RequestParam(required = true) String tenantId,
	@RequestParam(required = false) Integer pageSize,
	@RequestParam(required = false) Integer pageNumber,
	@RequestParam(required = false) String sort,
	@RequestParam(required = false) String active,
	@RequestParam(required = false) String tradeLicenseId,
	@RequestParam(required = false) String applicationNumber,
	@RequestParam(required = false) String licenseNumber,
	@RequestParam(required = false) String oldLicenseNumber,
	@RequestParam(required = false) String mobileNumber,
	@RequestParam(required = false) String aadhaarNumber,
	@RequestParam(required = false) String eamilId,
	@RequestParam(required = false) String propertyAssesmentNo,
	@RequestParam(required = false) Integer revenueWard,
	@RequestParam(required = false) Integer locality,
	@RequestParam(required = false) String ownerName,
	@RequestParam(required = false) String tradeTitle,
	@RequestParam(required = false) String tradeType,
	@RequestParam(required = false) Integer tradeCategory,
	@RequestParam(required = false) Integer tradeSubCategory) throws Exception {

		return tradeLicenseService.getTradeLicense(requestInfo.getRequestInfo(), tenantId, pageSize, pageNumber, sort, active, tradeLicenseId, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber, eamilId, propertyAssesmentNo, revenueWard, locality, ownerName, tradeTitle, tradeType, tradeCategory, tradeSubCategory);
	}

}
