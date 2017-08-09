package org.egov.tradelicense.domain.service;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tradelicense.web.requests.TradeLicenseRequest;
import org.egov.tradelicense.web.requests.TradeLicenseResponse;

/**
 * Service class for Category master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public interface TradeLicenseService {

	
	/**
	 * Description : service method for creating TradeLicense
	 * 
	 * @param TradeLicenseRequest
	 * @return TradeLicenseResponse
	 */
	
	public TradeLicenseResponse createTradeLicense(TradeLicenseRequest tradeLicenseRequest);



	/**
	 * Description : service method for searching category master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param pageSize
	 * @param pageNumber
	 * @param sort
	 * @param active
	 * @param tradeLicenseId
	 * @param applicationNumber
	 * @param licenseNumber
	 * @param oldLicenseNumber
	 * @param mobileNumber
	 * @param aadhaarNumber
	 * @param eamilId
	 * @param propertyAssesmentNo
	 * @param revenueWard
	 * @param locality
	 * @param ownerName
	 * @param tradeTitle
	 * @param tradeType
	 * @param tradeCategory
	 * @param tradeSubCategory
	 * @return TradeLicenseResponse
	 * 
	 */
	public TradeLicenseResponse getTradeLicense(RequestInfo requestInfo, String tenantId,
			Integer pageSize, Integer pageNumber, String sort, String active, String tradeLicenseId,
			String applicationNumber, String licenseNumber, String oldLicenseNumber,String mobileNumber,
			String aadhaarNumber, String eamilId, String propertyAssesmentNo, Integer revenueWard,
			Integer locality, String ownerName, String tradeTitle, String tradeType, Integer tradeCategory,
			Integer tradeSubCategory);

	
}