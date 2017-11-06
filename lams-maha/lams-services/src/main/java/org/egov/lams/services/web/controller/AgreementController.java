package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.request.AgreementRequest;
import org.egov.lams.services.service.AgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("agreements")
public class AgreementController {

	@Autowired
	private AgreementService agreementService;

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AgreementRequest agreementRequest) {
		return new ResponseEntity<>(agreementService.createAgreement(agreementRequest), HttpStatus.CREATED);
	}
	
	@PostMapping("/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid AgreementRequest agreementRequest) {

		return new ResponseEntity<>(agreementService.updateAgreement(agreementRequest), HttpStatus.CREATED);
	}
}
