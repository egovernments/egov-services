package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.contract.LandTransferRequest;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transfer")
@Slf4j
@Component
public class LandTransferController {
	@Autowired
	private LandTransferService landTransferService;

	@Autowired
	private ResponseFactory responseFactory;
	
	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid final LandTransferRequest landTransferRequest,
			final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, landTransferRequest.getRequestInfo()),HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(landTransferService.create(landTransferRequest),HttpStatus.CREATED);
	}

}
