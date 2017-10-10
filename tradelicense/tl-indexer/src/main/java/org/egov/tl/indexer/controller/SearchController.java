package org.egov.tl.indexer.controller;

import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.TradeLicenseSearchResponse;
import org.egov.tl.indexer.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Shubham pratap singh
 *
 */
@RestController
public class SearchController {

	@Autowired
	SearchService searchService;

	@RequestMapping(path = "/license/v1/_search", method = RequestMethod.POST)
	public TradeLicenseSearchResponse searchTradelicense(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) String sort,
			@RequestParam(required = false) String active, @RequestParam(required = false) String tradeLicenseId,
			@RequestParam(required = false) String applicationNumber,
			@RequestParam(required = false) String licenseNumber,
			@RequestParam(required = false) String oldLicenseNumber,
			@RequestParam(required = false) String mobileNumber, @RequestParam(required = false) String aadhaarNumber,
			@RequestParam(required = false) String emailId, @RequestParam(required = false) String propertyAssesmentNo,
			@RequestParam(required = false) String adminWard, @RequestParam(required = false) String locality,
			@RequestParam(required = false) String ownerName, @RequestParam(required = false) String tradeTitle,
			@RequestParam(required = false) String tradeType, @RequestParam(required = false) String tradeCategory,
			@RequestParam(required = false) String tradeSubCategory, @RequestParam(required = false) String legacy,
			@RequestParam(required = false) String status) throws Exception {

		return searchService.searchFromEs(requestInfo.getRequestInfo(), tenantId, pageSize, pageNumber, sort, active,
				tradeLicenseId, applicationNumber, licenseNumber, oldLicenseNumber, mobileNumber, aadhaarNumber,
				emailId, propertyAssesmentNo, adminWard, locality, ownerName, tradeTitle, tradeType, tradeCategory,
				tradeSubCategory, legacy, status);
	}

}
