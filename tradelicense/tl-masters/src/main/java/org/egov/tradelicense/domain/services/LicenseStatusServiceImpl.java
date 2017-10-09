package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.LicenseStatusRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.LicenseStatusResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.LicenseStatusValidator;
import org.egov.tradelicense.persistence.repository.LicenseStatusRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * LicenseStatusService implementation class
 * 
 * @author Shubham pratap singh
 *
 */
@Service
public class LicenseStatusServiceImpl implements LicenseStatusService {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LicenseStatusRepository licenseStatusRepository;

	@Autowired
	LicenseStatusValidator licenseStatusValidator;

	@Autowired
	Producer producer;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Override
	public LicenseStatusResponse createLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		licenseStatusValidator.validateLicenseStatusRequest(licenseStatusRequest, true);
		producer.send(propertiesManager.getCreateLicenseStatusValidated(), licenseStatusRequest);
		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		licenseStatusResponse.setLicenseStatuses(licenseStatusRequest.getLicenseStatuses());
		licenseStatusResponse.setResponseInfo(responseInfo);

		return licenseStatusResponse;
	}

	@Override
	@Transactional
	public void createLicenseStatus(LicenseStatusRequest licenseStatusRequest) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		for (LicenseStatus licenseStatus : licenseStatusRequest.getLicenseStatuses()) {

			try {
				licenseStatusRepository.createLicenseStatus(licenseStatus);
			} catch (Exception ex) {
				throw new InvalidInputException(ex.getMessage(), requestInfo);
			}
		}

	}

	@Override
	public LicenseStatusResponse updateLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		licenseStatusValidator.validateLicenseStatusRequest(licenseStatusRequest, false);
		producer.send(propertiesManager.getUpdateLicenseStatusValidated(), licenseStatusRequest);
		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		licenseStatusResponse.setLicenseStatuses(licenseStatusRequest.getLicenseStatuses());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		licenseStatusResponse.setResponseInfo(responseInfo);
		return licenseStatusResponse;
	}

	@Override
	@Transactional
	public void updateLicenseStatus(LicenseStatusRequest licenseStatusRequest) {
		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		for (LicenseStatus licenseStatus : licenseStatusRequest.getLicenseStatuses()) {

			try {
				licenseStatusRepository.updateLicenseStatus(licenseStatus);
			} catch (Exception ex) {
				throw new InvalidInputException(ex.getMessage(), requestInfo);
			}
		}

	}

	@Override
	public LicenseStatusResponse getLicenseStatusMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String[] codes, String name,  String moduleType, String active, Integer pageSize, Integer offSet) {

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();

		try {
			List<LicenseStatus> licenseStatuslst = licenseStatusRepository.searchLicenseStatus(tenantId, ids, codes, name,
					 moduleType, active, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			licenseStatusResponse.setLicenseStatuses(licenseStatuslst);
			licenseStatusResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return licenseStatusResponse;

	}
}
