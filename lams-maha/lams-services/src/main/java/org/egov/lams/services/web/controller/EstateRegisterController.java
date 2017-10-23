package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.EstateRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/estates")
@Slf4j
public class EstateRegisterController {

	@Autowired
	private EstateRegisterService estateRegisterService;
	
	@Autowired
	private ResponseFactory responseFactory;
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final EstateRegisterRequest estateRegisterRequest,
			final BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) 
			return new ResponseEntity<>(responseFactory.getErrorResponse(bindingResult, estateRegisterRequest.getRequestInfo()), HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(estateRegisterService.createAsync(estateRegisterRequest),HttpStatus.CREATED);
	}
}
