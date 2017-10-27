package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.lams.common.web.contract.LandPossessionRequest;
import org.egov.lams.common.web.contract.LandPossessionResponse;
import org.egov.lams.common.web.contract.LandPossessionSearchCriteria;
import org.egov.lams.common.web.request.RequestInfoWrapper;
import org.egov.lams.common.web.response.LandAcquisitionResponse;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandPossessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid final LandPossessionRequest landPossessionRequest,
			final BindingResult bindingResult) {

		if (bindingResult.hasErrors())
			return new ResponseEntity<>(
					responseFactory.getErrorResponse(bindingResult, landPossessionRequest.getRequestInfo()),
					HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(landPossessionService.update(landPossessionRequest), HttpStatus.OK);
	}
	
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final LandPossessionSearchCriteria landPossessionSearchCriteria,
			final BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = responseFactory.getErrorResponse(bindingResult,
					requestInfoWrapper.getRequestInfo());
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		final LandPossessionResponse landAcquisitionResponse = landPossessionService
				.search(landPossessionSearchCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(landAcquisitionResponse, HttpStatus.OK);
	}
	
}
