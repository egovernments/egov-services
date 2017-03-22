package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.AgreementResponse;
import org.egov.lams.exception.Error;
import org.egov.lams.exception.ErrorResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.service.AgreementService;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.validator.AgreementValidator;
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
@RequestMapping("agreements")
public class AgreementController {
	public static final Logger LOGGER = LoggerFactory
			.getLogger(AgreementController.class);

	@Autowired
	private AgreementService agreementService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	@Autowired
	private AgreementValidator agreementValidator;
	
	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(
			@ModelAttribute @Valid AgreementCriteria agreementCriteria,
			@RequestBody RequestInfo requestInfo, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse,
					HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:"
				+ agreementCriteria);

		List<Agreement> agreements = agreementService
				.searchAgreement(agreementCriteria);
		return getSuccessResponse(agreements, requestInfo);
	}

	private ResponseEntity<?> getSuccessResponse(List<Agreement> agreements, RequestInfo requestInfo) {
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		agreementResponse.setResposneInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		return new ResponseEntity<>(agreementResponse, HttpStatus.OK);
	}

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(
			@RequestBody @Valid AgreementRequest agreementRequest,
			BindingResult errors) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes,
					HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("agreementRequest::" + agreementRequest);
		Agreement agreement = agreementRequest.getAgreement();
		agreementValidator.validateAgreement(agreementRequest);
		agreement = agreementService.createAgreement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);

		return new ResponseEntity<>(agreementResponse,
				HttpStatus.CREATED);
	}
	
	@PostMapping("_update/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("code") String code, @RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("AgreementController:getAgreements():update agreement:" + agreementRequest);

		Agreement agreement = agreementRequest.getAgreement();
		agreementValidator.validateAgreement(agreementRequest);
		agreement = agreementService.updateAgreement(agreement);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);

		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		/*
		 * ResponseInfo responseInfo = new ResponseInfo();
		 * responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		 * responseInfo.setApi_id(""); errRes.setResponseInfo(responseInfo);
		 */
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().add(errs.getField());
				error.getFields().add(errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}
