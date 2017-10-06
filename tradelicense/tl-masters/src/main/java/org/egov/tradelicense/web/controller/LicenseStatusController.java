package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tl.commons.web.requests.LicenseStatusRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.domain.services.LicenseStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license status master
 * 
 * @author Pavan Kumar Kamma
 *
 */
@RestController
@RequestMapping(path = "/status/v1")
public class LicenseStatusController {

	@Autowired
	LicenseStatusService licenseStatusService;

	/**
	 * Description : This api for creating LicenseStatus master
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public LicenseStatusResponse createLicenseStatusMaster(
			@Valid @RequestBody LicenseStatusRequest licenseStatusRequest) throws Exception {

		return licenseStatusService.createLicenseStatusMaster(licenseStatusRequest);
	}

	/**
	 * Description : This api for updating LicenseStatus master
	 * 
	 * 
	 * @param LicenseStatusRequest
	 * @return LicenseStatusResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public LicenseStatusResponse updateLicenseStatusMaster(
			@Valid @RequestBody LicenseStatusRequest licenseStatusRequest) throws Exception {

		return licenseStatusService.updateLicenseStatusMaster(licenseStatusRequest);
	}

	/**
	 * Description : This api for searching LicenseStatus master
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
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public LicenseStatusResponse getLicenseStatusMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String[] codes,
			@RequestParam(required = false) String moduleType, @RequestParam(required = false) String active,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer offSet)
			throws Exception {

		return licenseStatusService.getLicenseStatusMaster(requestInfo.getRequestInfo(), tenantId, ids,codes, name, 
				moduleType, active, pageSize, offSet);
	}
}