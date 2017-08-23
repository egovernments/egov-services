package org.egov.tradelicense.domain.services;

import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.requests.DocumentTypeRequest;
import org.egov.tl.commons.web.response.DocumentTypeResponse;

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
	public DocumentTypeResponse createDocumentTypeMaster(DocumentTypeRequest documentTypeRequest);

	/**
	 * Description : service method for updating documentType
	 * 
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 */
	public DocumentTypeResponse updateDocumentTypeMaster(DocumentTypeRequest documentTypeRequest);

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
	public DocumentTypeResponse getDocumentTypeMaster(RequestInfo requestInfo, String tenantId, Integer[] ids,
			String name, String enabled, String applicationType, Integer pageSize, Integer offSet);

	/**
	 * Description : service method for creating documentType
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 */
	public void createDocumentType(DocumentTypeRequest documentTypeRequest);

	/**
	 * Description : service method for updating documentType
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 */
	public void updateDocumentType(DocumentTypeRequest documentTypeRequest);
}