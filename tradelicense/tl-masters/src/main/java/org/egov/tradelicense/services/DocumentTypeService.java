package org.egov.tradelicense.services;

import org.egov.models.DocumentTypeRequest;
import org.egov.models.DocumentTypeResponse;
import org.egov.models.RequestInfo;

/**
 * Service class for DocumentType master
 * 
 * @author Pavan Kumar Kamma
 *
 */
public interface DocumentTypeService {

	/**
	 * Description : service method for creating documentType
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 */
	public DocumentTypeResponse createDocumentType(DocumentTypeRequest documentTypeRequest);

	/**
	 * Description : service method for updating documentType
	 * 
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 */
	public DocumentTypeResponse updateDocumentType(DocumentTypeRequest documentTypeRequest);

	/**
	 * Description : service method for searching DocumentType master
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @param ids
	 * @param name
	 * @param enabled
	 * @param applicationType
	 * @param pageSize
	 * @param offSet
	 * @return DocumentTypeResponse
	 */
	public DocumentTypeResponse getDocumentType(RequestInfo requestInfo, String tenantId, Integer[] ids, String name,
			Boolean enabled, String applicationType, Integer pageSize, Integer offSet);
}