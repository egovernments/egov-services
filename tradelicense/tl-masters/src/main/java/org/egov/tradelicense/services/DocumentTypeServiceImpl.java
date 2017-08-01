package org.egov.tradelicense.services;

import java.util.Date;
import java.util.List;

import org.egov.models.AuditDetails;
import org.egov.models.DocumentType;
import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.egov.models.ResponseInfoFactory;
import org.egov.models.UserInfo;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.exception.DuplicateIdException;
import org.egov.tradelicense.exception.InvalidInputException;
import org.egov.tradelicense.repository.DocumentTypeRepository;
import org.egov.tradelicense.repository.ValidatorRepository;
import org.egov.tradelicense.repository.helper.UtilityHelper;
import org.egov.tradelicense.utility.ConstantUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * DocumentTypeService implementation class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {

	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	DocumentTypeRepository documentTypeRepository;

	@Autowired
	ResponseInfoFactory responseInfoFactory;
	
	
	@Autowired
	UtilityHelper utilityHelper;

	@Override
	public DocumentTypeResponse createDocumentType( DocumentTypeRequest documentTypeRequest)  {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
		AuditDetails auditDetails = getCreateDocumentTypeAuditDetails(requestInfo);
		for (DocumentType documentType : documentTypeRequest.getDocumentTypes()) {

			Boolean isExists = utilityHelper.checkWhetherDocumentTypeExists( documentType );

			if (isExists)
				throw new DuplicateIdException(propertiesManager.getDocumentTypeCustomMsg(),requestInfo);

			try {
				documentType.setAuditDetails(auditDetails);
				Long id = documentTypeRepository.createDocumentType(documentType);
				documentType.setId(id);
			} catch (Exception e) {
				throw new InvalidInputException(requestInfo);
			}
		}
		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		documentTypeResponse.setDocumentTypes(documentTypeRequest.getDocumentTypes());
		documentTypeResponse.setResponseInfo(responseInfo);

		return documentTypeResponse;
	}

	private AuditDetails getCreateDocumentTypeAuditDetails(RequestInfo requestInfo) {

		AuditDetails auditDetails = new AuditDetails();
		Long createdTime = new Date().getTime();
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedTime(createdTime);
		UserInfo userInfo = requestInfo.getUserInfo();
		if (userInfo != null) {
			auditDetails.setCreatedBy(userInfo.getUsername());
			auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUsername());
		}

		return auditDetails;
	}

	@Override
	public DocumentTypeResponse updateDocumentType(DocumentTypeRequest documentTypeRequest) {

		for (DocumentType documentType : documentTypeRequest.getDocumentTypes()) {

			Boolean isExists = utilityHelper.checkWhetherRecordExitswithName(documentType.getTenantId(), documentType.getName(),
					ConstantUtility.DOCUMENT_TYPE_TABLE_NAME, documentType.getId(), documentType.getApplicationType().toString());

			if (isExists)
				throw new DuplicateIdException(propertiesManager.getDocumentTypeCustomMsg(),documentTypeRequest.getRequestInfo());

			RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
			try {

				Long updatedTime = new Date().getTime();
				documentType.getAuditDetails().setLastModifiedTime(updatedTime);
				documentType.getAuditDetails().setLastModifiedBy(requestInfo.getUserInfo().getUsername());
				documentType = documentTypeRepository.updateDocumentType(documentType);

			} catch (Exception e) {
				throw new InvalidInputException(documentTypeRequest.getRequestInfo());

			}
		}

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();

		documentTypeResponse.setDocumentTypes(documentTypeRequest.getDocumentTypes());
		ResponseInfo responseInfo = responseInfoFactory
				.createResponseInfoFromRequestInfo(documentTypeRequest.getRequestInfo(), true);
		documentTypeResponse.setResponseInfo(responseInfo);

		return documentTypeResponse;

	}

	@Override
	public DocumentTypeResponse getDocumentType(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, Boolean enabled, String applicationType, Integer pageSize, Integer offSet) {


		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		try {
			List<DocumentType> documentTypes = documentTypeRepository.searchDocumentType(tenantId, ids, name, enabled, applicationType, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			documentTypeResponse.setDocumentTypes(documentTypes);
			documentTypeResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(requestInfo);
		}


		return documentTypeResponse;
	}

}