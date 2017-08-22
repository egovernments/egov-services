package org.egov.tradelicense.domain.services;

import java.util.List;

import org.egov.tl.commons.web.contract.DocumentType;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.commons.web.requests.DocumentTypeRequest;
import org.egov.tl.commons.web.requests.ResponseInfoFactory;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.services.validator.DocumentTypeValidator;
import org.egov.tradelicense.persistence.repository.DocumentTypeRepository;
import org.egov.tradelicense.persistence.repository.helper.UtilityHelper;
import org.egov.tradelicense.producers.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	DocumentTypeValidator documentTypeValidator;

	@Autowired
	UtilityHelper utilityHelper;

	@Autowired
	ResponseInfoFactory responseInfoFactory;

	@Autowired
	Producer producer;

	@Override
	public DocumentTypeResponse createDocumentTypeMaster(DocumentTypeRequest documentTypeRequest) {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
		documentTypeValidator.validateDocumentTypeRequest(documentTypeRequest, true);
		producer.send(propertiesManager.getCreateDocumentTypeValidated(), documentTypeRequest);
		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		documentTypeResponse.setDocumentTypes(documentTypeRequest.getDocumentTypes());
		documentTypeResponse.setResponseInfo(responseInfo);

		return documentTypeResponse;
	}

	@Override
	@Transactional
	public void createDocumentType(DocumentTypeRequest documentTypeRequest) {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();

		for (DocumentType documentType : documentTypeRequest.getDocumentTypes()) {
			try {

				documentTypeRepository.createDocumentType(documentType);

			} catch (Exception ex) {
				throw new InvalidInputException(ex.getLocalizedMessage(), requestInfo);
			}
		}
	}

	@Override
	public DocumentTypeResponse updateDocumentTypeMaster(DocumentTypeRequest documentTypeRequest) {

		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();
		documentTypeValidator.validateDocumentTypeRequest(documentTypeRequest, false);
		producer.send(propertiesManager.getUpdateDocumentTypeValidated(), documentTypeRequest);
		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		documentTypeResponse.setDocumentTypes(documentTypeRequest.getDocumentTypes());
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		documentTypeResponse.setResponseInfo(responseInfo);

		return documentTypeResponse;
	}

	@Override
	@Transactional
	public void updateDocumentType(DocumentTypeRequest documentTypeRequest) {
		RequestInfo requestInfo = documentTypeRequest.getRequestInfo();

		for (DocumentType documentType : documentTypeRequest.getDocumentTypes()) {

			try {

				documentTypeRepository.updateDocumentType(documentType);

			} catch (Exception ex) {
				throw new InvalidInputException(ex.getLocalizedMessage(), requestInfo);
			}
		}

	}

	@Override
	public DocumentTypeResponse getDocumentTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String enabled, String applicationType, Integer pageSize, Integer offSet) {

		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		try {
			List<DocumentType> documentTypes = documentTypeRepository.searchDocumentType(tenantId, ids, name, enabled,
					applicationType, pageSize, offSet);
			ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
			documentTypeResponse.setDocumentTypes(documentTypes);
			documentTypeResponse.setResponseInfo(responseInfo);

		} catch (Exception e) {
			throw new InvalidInputException(e.getLocalizedMessage(), requestInfo);
		}

		return documentTypeResponse;
	}

}