package org.egov.lams.services.web.controller;

import org.egov.lams.common.web.request.LandRegisterRequest;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.LandRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/lands")
@Slf4j
public class LandRegisterController {

	@Autowired
	private LandRegisterService landRegisterService;
	
	@Autowired
	private ResponseFactory responseFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/_create")
	public ResponseEntity<?> create(@RequestBody LandRegisterRequest landRegisterRequest, BindingResult bindingResult) {
		log.debug("Land Register RequestInfo:" + landRegisterRequest.getRequestInfo());
		log.debug("Land Register :" + landRegisterRequest.getLandRegisters());

		if (bindingResult.hasErrors()) {
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, landRegisterRequest.getRequestInfo()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(landRegisterService.createAsync(landRegisterRequest), HttpStatus.CREATED);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/_update")
	public ResponseEntity<?> update(@RequestBody LandRegisterRequest landRegisterRequest, BindingResult bindingResult) {
		log.debug("Land Register RequestInfo:" + landRegisterRequest.getRequestInfo());
		log.debug("Land Register :" + landRegisterRequest.getLandRegisters());

		if (bindingResult.hasErrors()) {

		}
		return new ResponseEntity(landRegisterService.updateAsync(landRegisterRequest), HttpStatus.OK);
	}
}
