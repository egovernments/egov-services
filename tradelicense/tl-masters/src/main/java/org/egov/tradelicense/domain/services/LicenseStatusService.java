package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.LicenseStatusRequest;
import org.egov.tl.commons.web.response.LicenseStatusResponse;

/**
 * Service class for LicenseStatus master
 * 
 * @author Shubham pratap singh
 */

public interface LicenseStatusService {

	/**
	 * Description : service method for creating LicenseStatus master
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 */
	public LicenseStatusResponse createLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest);

	/**
	 * Description : service method for updating LicenseStatus master
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 */
	public LicenseStatusResponse updateLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest);

	/**
	 * Description : service method for searching LicenseStatus master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param code
	 * @param active
	 * @param pageSize
	 * @param offSet
	 * @return LicenseStatusResponse
	 * @throws Exception
	 */
	public LicenseStatusResponse getLicenseStatusMaster(RequestInfo requestInfo, String tenantId, Integer[] ids, String[] codes,
			String name, String moduleType, String active, Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating LicenseStatus master
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 */
	public void createLicenseStatus(LicenseStatusRequest licenseStatusRequest);

	/**
	 * Description : service method for updating LicenseStatus master
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 */
	public void updateLicenseStatus(LicenseStatusRequest licenseStatusRequest);

}
