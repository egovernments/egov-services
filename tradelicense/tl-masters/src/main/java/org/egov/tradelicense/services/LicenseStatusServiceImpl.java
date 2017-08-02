package org.egov.tradelicense.services;

import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.LicenseStatus;
import org.egov.models.LicenseStatusRequest;
import org.egov.models.LicenseStatusResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.repository.LicenseStatusRepository;
import org.egov.tradelicense.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
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
public class LicenseStatusServiceImpl implements LicenseStatusService{


	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private LicenseStatusRepository licenseStatusRepository;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Override
	@Transactional
	public LicenseStatusResponse createLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
		for (LicenseStatus licenseStatus : licenseStatusRequest.getLicenseStatuses()) {

			Boolean isExists = utilityHelper.checkWhetherLicenseStatusExists(licenseStatus.
					getTenantId(), licenseStatus.getName(), licenseStatus.getCode(), null, ConstantUtility.LICENSE_STATUS_TABLE_NAME);


			if (isExists){
				throw new DuplicateIdException(propertiesManager.getLicenseStatusCustomMsg(), requestInfo);
			}

			try {
				licenseStatus.setAuditDetails(auditDetails);
				Long id = licenseStatusRepository.createLicenseStatus(licenseStatus);
				licenseStatus.setId(id);
			}
			catch(Exception e){
				throw new InvalidInputException(requestInfo);
			}
		}
		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		licenseStatusResponse.setLicenseStatuses( licenseStatusRequest.getLicenseStatuses() );
		licenseStatusResponse.setResponseInfo(responseInfo);


		return licenseStatusResponse;
	}

	@Override
	@Transactional
	public LicenseStatusResponse updateLicenseStatusMaster(LicenseStatusRequest licenseStatusRequest) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();
		for (LicenseStatus licenseStatus : licenseStatusRequest.getLicenseStatuses()) {

			Boolean isExists = utilityHelper.checkWhetherLicenseStatusExists(licenseStatus.getTenantId(), licenseStatus.getName(),
					licenseStatus.getCode() , licenseStatus.getId(), ConstantUtility.LICENSE_STATUS_TABLE_NAME);

			if (isExists)
				throw new DuplicateIdException(propertiesManager.getLicenseStatusCustomMsg(), requestInfo);

			
			try {

				AuditDetails auditDetails = licenseStatus.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				licenseStatus.setAuditDetails(auditDetails);
				licenseStatus = licenseStatusRepository.updateLicenseStatus(licenseStatus);

			} catch (Exception e) {

				throw new InvalidInputException(requestInfo);
			}
		}

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();

		licenseStatusResponse.setLicenseStatuses(licenseStatusRequest.getLicenseStatuses());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(licenseStatusRequest.getRequestInfo(),
				true);
		licenseStatusResponse.setResponseInfo(responseInfo);
		return licenseStatusResponse;
	}

	@Override
	public LicenseStatusResponse getLicenseStatusMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String code, String active, Integer pageSize, Integer offSet) {

		LicenseStatusResponse licenseStatusResponse = new LicenseStatusResponse();

		try {
			List<LicenseStatus> licenseStatuslst = licenseStatusRepository.searchLicenseStatus(tenantId, ids, name, code, active, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			licenseStatusResponse.setLicenseStatuses(licenseStatuslst);
			licenseStatusResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}

		return licenseStatusResponse;

	}
}
