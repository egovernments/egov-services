package org.egov.demand.web.controller;

import javax.validation.Valid;


import org.egov.common.contract.response.ResponseInfo;
import org.egov.demand.Exception.Error;
import org.egov.demand.Exception.ErrorResponse;
import org.egov.demand.model.TaxHeadMasterCriteria;
import org.egov.demand.service.TaxHeadMasterService;
import org.egov.demand.web.contract.RequestInfoWrapper;
import org.egov.demand.web.contract.TaxHeadMasterRequest;
import org.egov.demand.web.contract.TaxHeadMasterResponse;
import org.egov.demand.web.validator.TaxHeadMasterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxheads")
public class TaxHeadMasterController {

	private static final Logger logger = LoggerFactory.getLogger(TaxHeadMasterController.class);
	
	@Autowired
	private TaxHeadMasterService taxHeadMasterService;
	
	@Autowired
	private TaxHeadMasterValidator taxHeadMasterValidator;
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@RequestBody @Valid final RequestInfoWrapper requestInfoWrapper,
			@ModelAttribute @Valid final TaxHeadMasterCriteria taxHeadMasterCriteria, final BindingResult bindingResult) {
		logger.info("taxHeadMasterCriteria::" + taxHeadMasterCriteria + "requestInfoWrapper::" + requestInfoWrapper);

		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		System.out.println("::::taxHeadMasterCriteria::::"+taxHeadMasterCriteria);
		final TaxHeadMasterResponse taxHeadMasterResponse = taxHeadMasterService.getTaxHeads(taxHeadMasterCriteria, requestInfoWrapper.getRequestInfo());
		return new ResponseEntity<>(taxHeadMasterResponse, HttpStatus.OK);
	}
	
	
	@PostMapping("_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid final TaxHeadMasterRequest taxHeadMasterRequest,
			final BindingResult bindingResult) {

		logger.info("create tax Head Master:" + taxHeadMasterRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		// TODO Input field validation, it will be a part of phase-2
		taxHeadMasterValidator.validateTaxHeads(taxHeadMasterRequest);
		final TaxHeadMasterResponse taxHeadMasterRponse = taxHeadMasterService.create(taxHeadMasterRequest);
		return new ResponseEntity<>(taxHeadMasterRponse, HttpStatus.CREATED);
	}

	@PostMapping("_update")
	@ResponseBody
	public ResponseEntity<?> update(@RequestBody @Valid final TaxHeadMasterRequest taxHeadMasterRequest, final BindingResult bindingResult) {

		logger.info("update tax Head Master:" + taxHeadMasterRequest);
		if (bindingResult.hasErrors()) {
			final ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}


		final TaxHeadMasterResponse taxHeadMasterRponse = taxHeadMasterService.update(taxHeadMasterRequest);

		return new ResponseEntity<>(taxHeadMasterRponse, HttpStatus.OK);
	}
	
	
	private ErrorResponse populateErrors(final BindingResult errors) {
		
		final ErrorResponse errRes = new ErrorResponse();

		final ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);

		final Error error=new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors())
			for (final FieldError errs : errors.getFieldErrors())
				error.getFields().put(errs.getField(), errs.getRejectedValue());
		errRes.setError(error);
		return errRes;
	}
	
}
