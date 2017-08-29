package org.egov.tradelicense.domain.services.validator;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.LicenseStatus;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.LicenseStatusRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseStatusValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	public void validateLicenseStatusRequest(LicenseStatusRequest licenseStatusRequest, Boolean isNewLicenseStatus) {

		RequestInfo requestInfo = licenseStatusRequest.getRequestInfo();

		for (LicenseStatus licenseStatus : licenseStatusRequest.getLicenseStatuses()) {

			Long licenseStatusId = null;

			if (isNewLicenseStatus) {
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				licenseStatus.setAuditDetails(auditDetails);
			} else {

				AuditDetails auditDetails = licenseStatus.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				licenseStatus.setAuditDetails(auditDetails);
				licenseStatusId = licenseStatus.getId();

				if (licenseStatusId == null) {
					throw new InvalidInputException(propertiesManager.getInvalidLicenseStatusIdMsg(), requestInfo);
				}
			}
			
			Boolean duplicatExistsWithModuleType = utilityHelper.checkWhetherLicenseStatusDuplicateWithModuleType(licenseStatus.getTenantId(),
					 licenseStatus.getCode(), licenseStatus.getModuleType(),
					ConstantUtility.LICENSE_STATUS_TABLE_NAME);
			
			if (duplicatExistsWithModuleType) {
				throw new DuplicateIdException(propertiesManager.getLicenseStatusCustomMsg(), requestInfo);
			}
			

			Boolean isExists = utilityHelper.checkWhetherLicenseStatusExists(licenseStatus.getTenantId(),
					licenseStatus.getName(), licenseStatus.getCode(), licenseStatusId,
					ConstantUtility.LICENSE_STATUS_TABLE_NAME);

			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getLicenseStatusCustomMsg(), requestInfo);
			}

		}

	}

}
