package org.egov.lams.web.controller;

import java.util.List;

import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.egov.lams.service.DocumentTypeService;
import org.egov.lams.web.contract.DocumentTypeResponse;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agreements/document")
public class DocumentTypeController {

	@Autowired
	private DocumentTypeService documentTypeService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@PostMapping("_search")
	public ResponseEntity<?> getDocumentType(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@RequestParam(name = "tenantId", required = false) String tenantId,
			@RequestParam(name = "appilcationtype", required = false) Application application) {

		DocumentType documentType = new DocumentType();
		documentType.setApplication(application);

		List<DocumentType> documentTypes = documentTypeService.getDocumentTypes(documentType);
		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		documentTypeResponse.setResponseInfo(
				responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true));
		documentTypeResponse.setDocumentTypes(documentTypes);
		return new ResponseEntity<>(documentTypeResponse, HttpStatus.OK);
	}
}
