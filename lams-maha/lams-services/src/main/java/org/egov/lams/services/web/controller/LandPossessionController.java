package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.contract.LandPossessionRequest;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandPossessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/possession")
@Slf4j
public class LandPossessionController {
	@Autowired
	private LandPossessionService landPossessionService;

	@Autowired
	private ResponseFactory responseFactory;
	
	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid final LandPossessionRequest landPossessionRequest,
			final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, landPossessionRequest.getRequestInfo()),HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(landPossessionService.create(landPossessionRequest),HttpStatus.CREATED);
	}
	
}
