package org.egov.mr.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.service.MarriageRegnService;
import org.egov.mr.web.contract.MarriageRegnCriteria;
import org.egov.mr.web.contract.MarriageRegnRequest;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.egov.mr.web.validator.MarriageRegnValidator;
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

@Slf4j
@RestController
@RequestMapping("/marriageRegns/appl/")
public class MarriageRegnController {

	@Autowired
	private MarriageRegnService marriageRegnService;
	
	@Autowired
	private ErrorHandler errorHandler;
	
	@Autowired
	private MarriageRegnValidator marriageRegnValidator;
	
	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid MarriageRegnCriteria marriageRegnCriteria,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		if (modelAttributeBindingResult.hasErrors())
			return new ResponseEntity<>(errorHandler.getErrorResponse(modelAttributeBindingResult, requestInfo),
					HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<>(marriageRegnService.getMarriageRegns(marriageRegnCriteria, requestInfo),HttpStatus.OK);
	}
	
	@PostMapping(value = "/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid MarriageRegnRequest marriageRegnRequest,
			BindingResult bindingResult) {
		marriageRegnValidator.validate(marriageRegnRequest, bindingResult);
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(
					errorHandler.getErrorResponse(bindingResult, marriageRegnRequest.getRequestInfo()),
					HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(marriageRegnService.createAsync(marriageRegnRequest),HttpStatus.CREATED);
	}

	@PostMapping(value = "/_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid MarriageRegnRequest marriageRegnRequest, BindingResult bindingResult) {
		log.debug("marriageRegnRequest::" + marriageRegnRequest);

		//esponseEntity<?> errorResponseEntity = validateMarriageRegnRequest(marriageRegnRequest, bindingResult, false);
		RequestInfo requestInfo = marriageRegnRequest.getRequestInfo();
		
		/*ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForCreate(requestInfo,
				bindingResult);*/
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(
					errorHandler.getErrorResponse(bindingResult, marriageRegnRequest.getRequestInfo()),
					HttpStatus.BAD_REQUEST);

		return new ResponseEntity<>(marriageRegnService.updateAsync(marriageRegnRequest),HttpStatus.CREATED);
	}

}
