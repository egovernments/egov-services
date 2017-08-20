package org.egov.tradelicense.domain.services.validator;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.DocumentTypeRequest;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.util.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeValidator {

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	PropertiesManager propertiesManager;

	public void validateDocumentTypeRequest(DocumentTypeRequest documentTypeRequest, Boolean isNewDocumentType) {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
		for (DocumentType documentType : documentTypeRequest.getDocumentTypes()) {

			Long documentTypeId = null;

			if (isNewDocumentType) {
				AuditDetails auditDetails = utilityHelper.getCreateMasterAuditDetails(requestInfo);
				documentType.setAuditDetails(auditDetails);
			} else {

				AuditDetails auditDetails = documentType.getAuditDetails();
				auditDetails = utilityHelper.getUpdateMasterAuditDetails(auditDetails, requestInfo);
				documentType.setAuditDetails(auditDetails);
				documentTypeId = documentType.getId();

				if (documentTypeId == null) {
					throw new InvalidInputException(propertiesManager.getInvalidDocumentTypeIdMsg(), requestInfo);
				}
			}

			Boolean isExists = utilityHelper.checkDocumentTypeDuplicate(documentType.getTenantId(),
					documentType.getName(), ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, documentTypeId,
					documentType.getApplicationType().name());

			if (isExists) {
				throw new DuplicateIdException(propertiesManager.getDocumentTypeCustomMsg(), requestInfo);
			}

		}
	}

}
