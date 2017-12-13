package org.egov.works.services.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.works.services.domain.service.DocumentDetailsService;
import org.egov.works.services.web.contract.DocumentDetail;
import org.egov.works.services.web.contract.DocumentDetailRequest;
import org.egov.works.services.web.contract.DocumentDetailResponse;
import org.egov.works.services.web.contract.DocumentDetailSearchCriteria;
import org.egov.works.services.web.contract.RequestInfo;
import org.egov.works.services.web.contract.factory.ResponseInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documentdetails")
public class DocumentDetailController {

	@Autowired
	private DocumentDetailsService documentDetailsService;

	@PostMapping
	@RequestMapping("/_create")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> createDocuments(@Valid @RequestBody DocumentDetailRequest documentDetailRequest)
			throws Exception {
        DocumentDetailResponse documentDetailResponse = documentDetailsService.createDocuments(documentDetailRequest);
		return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);

	}

	@PostMapping
	@RequestMapping("/_update")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> updateDocuments(@Valid @RequestBody DocumentDetailRequest documentDetailRequest)
			throws Exception {
		DocumentDetailResponse documentDetailResponse = documentDetailsService.updateDocuments(documentDetailRequest);
		return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);

	}

	@PostMapping
	@RequestMapping("/_search")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> searchDocuments(@Valid @ModelAttribute DocumentDetailSearchCriteria documentDetailSearchCriteria,
			@RequestBody RequestInfo requestInfo) {

        DocumentDetailResponse documentDetailResponse = documentDetailsService
				.searchDocuments(documentDetailSearchCriteria,requestInfo);
		return new ResponseEntity<>(documentDetailResponse, HttpStatus.OK);
	}
}
