package org.egov.demand.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.demand.model.DemandCriteria;
import org.egov.demand.model.DemandDetailCriteria;
import org.egov.demand.service.DemandService;
import org.egov.demand.web.contract.DemandRequest;
import org.egov.demand.web.contract.DemandResponse;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.egov.demand.web.contract.factory.ResponseFactory;
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
@RequestMapping("/demand")
@Slf4j
public class DemandController {

	@Autowired
	private DemandService demandService;

	@Autowired
	private ResponseFactory responseFactory;

	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {

		log.info("the demand request object : " + demandRequest);
		RequestInfo requestInfo = demandRequest.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		DemandResponse demandResponse =  demandService.create(demandRequest);
		System.err.println(demandResponse);
		return new ResponseEntity<>(demandResponse, HttpStatus.CREATED);
	}

	@PostMapping("_update")
	public ResponseEntity<?> update(@RequestBody @Valid DemandRequest demandRequest, BindingResult bindingResult) {

		RequestInfo requestInfo = demandRequest.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(demandService.updateAsync(demandRequest), HttpStatus.CREATED);
	}

	@PostMapping("_search")
	public ResponseEntity<?> search(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemandCriteria demandCriteria, BindingResult bindingResult) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(demandService.getDemands(demandCriteria, requestInfo), HttpStatus.OK);
	}

	@PostMapping("/demanddetail/_search")
	public ResponseEntity<?> demandDetailSearch(@RequestBody RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid DemandDetailCriteria demandDetailCriteria, BindingResult bindingResult) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, requestInfo), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(demandService.getDemandDetails(demandDetailCriteria, requestInfo), HttpStatus.OK);
	}

}
