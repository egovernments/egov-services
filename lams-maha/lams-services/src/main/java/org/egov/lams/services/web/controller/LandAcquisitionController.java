package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.request.LandAcquisitionRequest;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandAcquisitionService;
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
@RequestMapping("/acquisition")
@Slf4j
public class LandAcquisitionController {
	@Autowired
	private LandAcquisitionService landAcquisitionService;

	@Autowired
	private ResponseFactory responseFactory;

	@PostMapping("_create")
	public ResponseEntity<?> create(@RequestBody @Valid final LandAcquisitionRequest landAcquisitionRequest,
			final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, landAcquisitionRequest.getRequestInfo()),HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(landAcquisitionService.create(landAcquisitionRequest),HttpStatus.CREATED);
	}

	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid final LandAcquisitionRequest landAcquisitionRequest,
			final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, landAcquisitionRequest.getRequestInfo()),HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(landAcquisitionService.update(landAcquisitionRequest),HttpStatus.OK);	}

	/*
	 * @PostMapping("_search") public ResponseEntity<?>
	 * search(@RequestBody @Valid final LandAcquisitionRequest
	 * landAcquisitionRequest, final BindingResult bindingResult) {
	 * 
	 * return null; }
	 */

}
