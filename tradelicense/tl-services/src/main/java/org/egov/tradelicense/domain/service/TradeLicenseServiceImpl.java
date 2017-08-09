package org.egov.tradelicense.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.egov.tradelicense.domain.model.TradeLicense;
import org.egov.tradelicense.web.requests.ResponseInfo;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CategoryService implementation class
 * 
 * @author Shubham Pratap Singh
 *
 */
@Service
public class TradeLicenseServiceImpl implements TradeLicenseService {

	public TradeLicenseResponse createTradeLicense(TradeLicenseRequest tradeLicenseRequest) {
		
		
		ResponseInfo responseInfo = new ResponseInfo();
		List<TradeLicense> tradeLicenses =tradeLicenseRequest.getLicesnses();
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		TradeLicenseResponse.setLicenses(tradeLicenses);
		TradeLicenseResponse.setResponseInfo(responseInfo);
		return TradeLicenseResponse;
	}

	public TradeLicenseResponse getTradeLicense(org.egov.tradelicense.web.requests.RequestInfo requestInfo,
			String tenantId, Integer pageSize, Integer pageNumber, String sort, String active, String tradeLicenseId,
			String applicationNumber, String licenseNumber, String oldLicenseNumber, String mobileNumber,
			String aadhaarNumber, String eamilId, String propertyAssesmentNo, Integer revenueWard, Integer locality,
			String ownerName, String tradeTitle, String tradeType, Integer tradeCategory, Integer tradeSubCategory) {
		
		ResponseInfo responseInfo = new ResponseInfo();
		List<TradeLicense> tradeLicenses = new ArrayList<TradeLicense>();
		TradeLicense tradeLicense = new TradeLicense();
		tradeLicense.setActive(true);
		tradeLicense.setId(1l);
		tradeLicenses.add(tradeLicense);
		TradeLicenseResponse TradeLicenseResponse = new TradeLicenseResponse();
		TradeLicenseResponse.setLicenses(tradeLicenses);
		TradeLicenseResponse.setResponseInfo(responseInfo);
		
		return TradeLicenseResponse;
	}

	
}