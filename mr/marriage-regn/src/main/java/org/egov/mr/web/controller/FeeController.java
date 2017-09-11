package org.egov.mr.web.controller;

import javax.validation.Valid;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mr.service.FeeService;
import org.egov.mr.web.contract.FeeCriteria;
import org.egov.mr.web.contract.FeeRequest;
import org.egov.mr.web.contract.FeeResponse;
import org.egov.mr.web.contract.RequestInfoWrapper;
import org.egov.mr.web.errorhandler.ErrorHandler;
import org.egov.mr.web.validator.FeeValidator;
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
@RequestMapping("/fees")
public class FeeController {

	@Autowired
	private ErrorHandler errorHandler;
	
	@Autowired
	private FeeService feeService;
	
	@Autowired
	private FeeValidator feeValidator;
	
	@PostMapping("/_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid FeeCriteria feeCriteria,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {

		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForSearch(requestInfo,
				modelAttributeBindingResult, null);

		if (errorResponseEntity != null)
			return errorResponseEntity;
		
		return new ResponseEntity<FeeResponse>(feeService.getFee(feeCriteria, requestInfo), HttpStatus.OK);
	}
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final FeeRequest feeRequest,final BindingResult bindingResult) {

		feeValidator.validate(feeRequest, bindingResult);
		
		if (bindingResult.hasErrors())
			return new ResponseEntity<>(errorHandler.getErrorResponse(bindingResult, feeRequest.getRequestInfo()),
					HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<FeeResponse>(feeService.createAsync(feeRequest), HttpStatus.CREATED);
	}
	
	@PostMapping("_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final FeeRequest feeRequest,final BindingResult bindingResult) {

		ResponseEntity<?> errorResponseEntity = errorHandler.handleBindingErrorsForSearch(feeRequest.getRequestInfo(),
				bindingResult, null);

		if (errorResponseEntity != null)
			return errorResponseEntity;
		
		return new ResponseEntity<FeeResponse>(feeService.updateAsync(feeRequest), HttpStatus.CREATED);
	}
}
