package org.egov.tradelicense.domain.services.validator;

import org.egov.models.AuditDetails;
import org.egov.models.RequestInfo;
import org.egov.models.UOM;
import org.egov.models.UOMRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UOMValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	public void validateUOMRequest(UOMRequest uomRequest, Boolean isNewUOM) {

		RequestInfo requestInfo = uomRequest.getRequestInfo();

		for (UOM uom : uomRequest.getUoms()) {

			Long uomId = null;

			if (isNewUOM) {
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				uom.setAuditDetails(auditDetails);
			} else {

				AuditDetails auditDetails = uom.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				uom.setAuditDetails(auditDetails);
				uomId = uom.getId();

				if (uomId == null) {
					throw new InvalidInputException(propertiesManager.getInvalidUomIdMsg(), requestInfo);
				}
			}

			Boolean isExists = utilityHelper.checkWhetherDuplicateUomRecordExits(uom.getTenantId(), uom.getCode(),
					uom.getName(), ConstantUtility.UOM_TABLE_NAME, uomId);
			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getUomCustomMsg(), requestInfo);
			}

		}

	}

}
