package org.egov.tradelicense.web.controller;

import javax.validation.Valid;

import org.egov.tl.commons.web.requests.DocumentTypeRequest;
import org.egov.tl.commons.web.requests.RequestInfoWrapper;
import org.egov.tl.commons.web.response.DocumentTypeResponse;
import org.egov.tradelicense.domain.services.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller have the all api's related to trade license documentType
 * master
 * 
 * @author Pavan Kumar Kamma
 *
 */
@RestController
@RequestMapping(path = "/documenttype/v1")
public class DocumentTypeController {

	@Autowired
	DocumentTypeService documentTypeService;

	/**
	 * Description : This api for creating documentType master
	 * 
	 * @param tenantId
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_create", method = RequestMethod.POST)
	public DocumentTypeResponse createDocumentTypeMaster(@Valid @RequestBody DocumentTypeRequest documentTypeRequest)
			throws Exception {

		return documentTypeService.createDocumentTypeMaster(documentTypeRequest);
	}

	/**
	 * Description : This api for updating DocumentType master
	 * 
	 * @param DocumentTypeRequest
	 * @return DocumentTypeResponse
	 * @throws Exception
	 */
	@RequestMapping(path = "/_update", method = RequestMethod.POST)
	public DocumentTypeResponse updateDocumentTypeMaster(@Valid @RequestBody DocumentTypeRequest documentTypeRequest)
			throws Exception {

		return documentTypeService.updateDocumentTypeMaster(documentTypeRequest);
	}

	/**
	 * Description : This api for searching DocumentType master
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
	 * @throws Exception
	 */
	@RequestMapping(path = "/_search", method = RequestMethod.POST)
	public DocumentTypeResponse getDocumentTypeMaster(@RequestBody RequestInfoWrapper requestInfo,
			@RequestParam(required = true) String tenantId, @RequestParam(required = false) Integer[] ids,
			@RequestParam(required = false) String name, @RequestParam(required = false) String enabled,
			@RequestParam(required = false) String applicationType, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) Integer offSet) throws Exception {

		return documentTypeService.getDocumentTypeMaster(requestInfo.getRequestInfo(), tenantId, ids, name, enabled,
				applicationType, pageSize, offSet);
	}
}