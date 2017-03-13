package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.egov.lams.contract.AgreementRequest;
import org.egov.lams.contract.AgreementResponse;
import org.egov.lams.exception.Error;
import org.egov.lams.exception.ErrorResponse;
import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.RequestInfo;
import org.egov.lams.model.ResponseInfo;
import org.egov.lams.service.AgreementService;
import org.egov.lams.validator.AgreementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgreementController {
	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementController.class);

	@Autowired
	AgreementService agreementService;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid AgreementCriteria agreementCriteria,@RequestBody RequestInfo requestInfo,
			 BindingResult bindingResult) {

		if(bindingResult.hasErrors()){
			ErrorResponse errorResponse=populateErrors(bindingResult);
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:" + agreementCriteria);
		
		List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria);
		return getSuccessResponse(agreements);			
	}
	
	private ResponseEntity<?> getSuccessResponse(List<Agreement> agreements) {
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		agreementResponse.setResposneInfo(
				new ResponseInfo("Get Agreement", "ver", new Date(), "GET", "did", "key", "msgId", "rqstID"));
		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.OK);
	}

	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AgreementRequest agreementRequest,BindingResult errors){
		
		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("agreementRequest::"+agreementRequest);
		Agreement agreement=agreementRequest.getAgreement();
		AgreementValidator.validateAgreement(agreement);
		agreement=agreementService.createAgreement(agreement);
		List<Agreement>agreements=new ArrayList<Agreement>();
		agreements.add(agreement);
		AgreementResponse agreementResponse=new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		
		return new ResponseEntity<AgreementResponse>(agreementResponse, HttpStatus.CREATED);
	}
	
	private ErrorResponse populateErrors(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();

		/*ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApi_id("");
		errRes.setResponseInfo(responseInfo);*/
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while binding request");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFilelds().add(errs.getField());
				error.getFilelds().add(errs.getRejectedValue());
			}
		}
		errRes.setError(error);
		return errRes;
	}

}
