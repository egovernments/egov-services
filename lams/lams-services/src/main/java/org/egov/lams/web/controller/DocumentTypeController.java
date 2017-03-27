package org.egov.lams.web.controller;

import java.util.List;

import org.egov.lams.web.contract.DocumentTypeResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.model.DocumentType;
import org.egov.lams.model.enums.Application;
import org.egov.lams.service.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class DocumentTypeController {

	@Autowired
	private DocumentTypeService documentTypeService;

	@PostMapping("_search")
	public ResponseEntity<?> getDocumentType(@RequestBody RequestInfo requestInfo,
			@RequestParam(name = "tenantId", required = false) String tenantId,
			@RequestParam(name = "appilcationtype", required = false) Application application) {

		DocumentType documentType = new DocumentType();
		documentType.setApplication(application);
		
		List<DocumentType> documentTypes = documentTypeService.getDocumentTypes(documentType);
		DocumentTypeResponse documentTypeResponse = new DocumentTypeResponse();
		documentTypeResponse.setDocumentTypes(documentTypes);
		return new ResponseEntity<DocumentTypeResponse>(documentTypeResponse,HttpStatus.OK);
	}
}
