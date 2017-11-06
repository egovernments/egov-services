package org.egov.lams.services.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.lams.common.web.contract.EstateSearchCriteria;
import org.egov.lams.common.web.request.EstateRegisterRequest;
import org.egov.lams.common.web.request.RequestInfoWrapper;
import org.egov.lams.common.web.response.EstateRegisterResponse;
import org.egov.lams.services.factory.ResponseFactory;
import org.egov.lams.services.service.EstateRegisterService;
import org.egov.tracer.model.CustomException;
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
@RequestMapping("/estates")
@Slf4j
public class EstateRegisterController {

	@Autowired
	private EstateRegisterService estateRegisterService;
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final EstateRegisterRequest estateRegisterRequest) {
		
		return new ResponseEntity<>(estateRegisterService.createAsync(estateRegisterRequest),HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final EstateRegisterRequest estateRegisterRequest) {
		
		return new ResponseEntity<>(estateRegisterService.updateAsync(estateRegisterRequest),HttpStatus.CREATED);
	}
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final EstateSearchCriteria estateRegisterCriteria) {
		
		final EstateRegisterResponse estateResponse = estateRegisterService.getEstates(estateRegisterCriteria,
				requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(estateResponse, HttpStatus.OK);
	}
}
