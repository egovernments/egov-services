package org.egov.lams.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.egov.lams.model.Agreement;
import org.egov.lams.model.AgreementCriteria;
import org.egov.lams.model.enums.Source;
import org.egov.lams.service.AgreementService;
import org.egov.lams.web.contract.AgreementRequest;
import org.egov.lams.web.contract.AgreementResponse;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.Error;
import org.egov.lams.web.errorhandlers.ErrorResponse;
import org.egov.lams.web.validator.AgreementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("agreements")
public class AgreementController {
	public static final Logger LOGGER = LoggerFactory.getLogger(AgreementController.class);
	
	@Autowired
	private AgreementService agreementService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	@Autowired
	private AgreementValidator agreementValidator;

	@PostMapping("_search")
	@ResponseBody
	public ResponseEntity<?> search(@ModelAttribute @Valid AgreementCriteria agreementCriteria,
			@RequestBody RequestInfoWrapper requestInfoWrapper, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		LOGGER.info("AgreementController:getAgreements():searchAgreementsModel:" + agreementCriteria);
		List<Agreement> agreements = agreementService.searchAgreement(agreementCriteria, requestInfo);
		LOGGER.info("before sending for response success");
		return getSuccessResponse(agreements, requestInfo);
	}

	private ResponseEntity<?> getSuccessResponse(List<Agreement> agreements, RequestInfo requestInfo) {
		AgreementResponse agreementResponse = getAgreementResponse(agreements, requestInfo);
		LOGGER.info("before returning from getsucces resposne ::" + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.OK);
	}

	@PostMapping("_commonsearch")
	@ResponseBody
	public ResponseEntity<?> commonSearch(@ModelAttribute @Valid AgreementCriteria agreementCriteria,
			@RequestBody RequestInfoWrapper requestInfoWrapper, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		List<Agreement> agreements = null;
		AgreementRequest agreementRequest = new AgreementRequest();
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if (agreementCriteria.getAgreementNumber() != null && agreementCriteria.getTenantId() != null) {
			agreements = agreementService.getAgreementsByAgreementNumber(agreementCriteria,requestInfo);
		}
		if (agreements != null && !agreements.isEmpty()) {
			agreements.sort((agreement1, agreement2) -> agreement2.getId().compareTo(agreement1.getId()));
			agreementRequest.setRequestInfo(requestInfo);
			agreementRequest.setAgreement(agreements.get(0));
			agreementValidator.validateAgreementForWorkFLow(agreementRequest, bindingResult,
					agreementCriteria.getAction());

			if (bindingResult.hasErrors()) {
				ErrorResponse errorResponse = populateValidationErrors(bindingResult);
				return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
			} else
				return getSuccessResponse(agreements, requestInfo);
		} else {
			Error error = new Error();
			error.setCode(1);
			error.setDescription("No Agreements Found!");
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/_create")
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest::" + agreementRequest);
		agreementValidator.validateCreate(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createAgreement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info(agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}
	@PostMapping("/_modify")
	@ResponseBody
	public ResponseEntity<?> modify(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest::" + agreementRequest);
		agreementValidator.validateModify(agreementRequest, errors);
		agreementValidator.validateModifiedData(agreementRequest, errors);

		if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.modifyAgreement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info(agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}
	

	@PostMapping("/_renew")
	@ResponseBody
	public ResponseEntity<?> renew(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest::" + agreementRequest);
		agreementValidator.validateRenewal(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createRenewal(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement renewal response:" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("/_eviction")
	@ResponseBody
	public ResponseEntity<?> eviction(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest::" + agreementRequest);
		agreementValidator.validateEviction(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createEviction(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement eviction response:" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("/_cancel")
	@ResponseBody
	public ResponseEntity<?> cancel(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest cancel ::" + agreementRequest);
		agreementValidator.validateCancel(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createCancellation(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement cancellation response :" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("/_objection")
	@ResponseBody
	public ResponseEntity<?> objection(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		LOGGER.info("agreementRequest cancel ::" + agreementRequest);
		agreementValidator.validateObjection(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createObjection(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement objection response:" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("/_courtjudgement")
	@ResponseBody
	public ResponseEntity<?> courtjudgement(@RequestBody @Valid AgreementRequest agreementRequest,
			BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		LOGGER.info("agreementRequest cancel ::" + agreementRequest);
		agreementValidator.validateJudgement(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = agreementService.createJudgement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement judgement response:" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("/_remission")
	@ResponseBody
	public ResponseEntity<?> remission(@RequestBody @Valid AgreementRequest agreementRequest, BindingResult errors) {

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		agreementValidator.validateRemissionDetails(agreementRequest, errors);

		if (errors.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		} else if (errors.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(errors);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		Agreement agreement = agreementService.createRemission(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("agreement remission response:" + agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("_update/{code}")
	@ResponseBody
	public ResponseEntity<?> update(@PathVariable("code") String code, @RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if(agreementRequest.getAgreement().getSource().equals(Source.DATA_ENTRY)){
			agreementValidator.validatePartialCollection(agreementRequest, bindingResult);
		}
		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateValidationErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (agreementRequest.getAgreement().getSource().equals(Source.SYSTEM)) {
			agreementValidator.validateUpdate(agreementRequest, bindingResult);
		}
		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		if (!(code.equals(agreementRequest.getAgreement().getAcknowledgementNumber())
				|| code.equals(agreementRequest.getAgreement().getAgreementNumber())
						&& agreementService.isAgreementExist(code)))
			throw new RuntimeException("code mismatch or no agreement found for this value");

		Agreement agreement = null;
		agreement = agreementService.updateAgreement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("renewal/_update")
	@ResponseBody
	public ResponseEntity<?> updateRenewal(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException(" no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);

		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = null;
		agreement = agreementService.updateRenewal(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("cancel/_update")
	@ResponseBody
	public ResponseEntity<?> updateCancellation(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException("no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);

		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = null;
		agreement = agreementService.updateCancellation(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("eviction/_update")
	@ResponseBody
	public ResponseEntity<?> updateEviction(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException("no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);

		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		
		Agreement agreement = null;
		agreement = agreementService.updateEviction(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("objection/_update")
	@ResponseBody
	public ResponseEntity<?> updateObjection(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException("no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);
		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = null;
		agreement = agreementService.updateObjectionAndJudgement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("judgement/_update")
	@ResponseBody
	public ResponseEntity<?> updateJudgement(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException("no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);

		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = null;
		agreement = agreementService.updateObjectionAndJudgement(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info("the response form update agreement call : " + agreementResponse);
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("remission/_update")
	@ResponseBody
	public ResponseEntity<?> updateRmission(@RequestBody AgreementRequest agreementRequest,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		if (!agreementService.isAgreementExist(agreementRequest.getAgreement().getAcknowledgementNumber()))
			throw new RuntimeException(" no agreement found with given AcknowledgementNumber");

		agreementValidator.validateUpdate(agreementRequest, bindingResult);

		if (bindingResult.hasFieldErrors()) {
			ErrorResponse errRes = populateErrors(bindingResult);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}
		Agreement agreement = null;
		agreement = agreementService.updateRemission(agreementRequest);
		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}

	@PostMapping("demands/_prepare")
	@ResponseBody
	public ResponseEntity<?> prepareDemand(@RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult errors, @RequestParam(name = "agreementNumber", required = false) String agreementNumber,
			@RequestParam(name = "tenantId", required = true) String tenantId) {

		if (errors.hasErrors()) {
			ErrorResponse errRes = populateErrors(errors);
			return new ResponseEntity<>(errRes, HttpStatus.BAD_REQUEST);
		}

		AgreementRequest agreementRequest = new AgreementRequest();
		agreementRequest.setRequestInfo(requestInfoWrapper.getRequestInfo());

		AgreementCriteria agreementCriteria = new AgreementCriteria();
		agreementCriteria.setTenantId(tenantId);
		agreementCriteria.setAgreementNumber(agreementNumber);

		LOGGER.info("before search : " + agreementNumber);
		Agreement agreement = agreementService.searchAgreement(agreementCriteria, requestInfoWrapper.getRequestInfo())
				.get(0);
		LOGGER.info("after search " + agreement);
		agreementRequest.setAgreement(agreement);
		if (Source.DATA_ENTRY.equals(agreement.getSource())) {
			agreement.setLegacyDemands(agreementService.prepareLegacyDemands(agreementRequest));

		} else {
			agreement.setLegacyDemands(agreementService.prepareDemands(agreementRequest));
		}
		LOGGER.info("after prepare denmands : " + agreement.getLegacyDemands());

		List<Agreement> agreements = new ArrayList<>();
		agreements.add(agreement);
		AgreementResponse agreementResponse = getAgreementResponse(agreements, agreementRequest.getRequestInfo());
		LOGGER.info(agreementResponse.toString());
		return new ResponseEntity<>(agreementResponse, HttpStatus.CREATED);
	}
	
	@PostMapping("dcb/_view")
	@ResponseBody
	public ResponseEntity<?> viewDcb(@ModelAttribute @Valid AgreementCriteria agreementCriteria,
			@RequestBody RequestInfoWrapper requestInfoWrapper, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			ErrorResponse errorResponse = populateErrors(bindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
		List<Agreement> agreements = null;
		AgreementRequest agreementRequest = new AgreementRequest();
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();
		if ((agreementCriteria.getAgreementNumber() != null || agreementCriteria.getAcknowledgementNumber()!=null ) && agreementCriteria.getTenantId() != null) {
			agreements = agreementService.getAgreementsByAgreementNumber(agreementCriteria,requestInfo);
		}
		if (agreements != null && !agreements.isEmpty()) {
			Agreement agreement = agreements.get(0); 
			agreementRequest.setRequestInfo(requestInfo);
			agreementRequest.setAgreement(agreement);
			agreement.setLegacyDemands(agreementService.getDemands(agreementRequest));

				return getSuccessResponse(agreements, requestInfo);
		} else {
			Error error = new Error();
			error.setCode(1);
			error.setDescription("No Agreements Found!");
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setError(error);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}
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
				error.getFields().put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}

	private AgreementResponse getAgreementResponse(List<Agreement> agreements, RequestInfo requestInfo) {
		AgreementResponse agreementResponse = new AgreementResponse();
		agreementResponse.setAgreement(agreements);
		agreementResponse.setResponseInfo(responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true));
		return agreementResponse;
	}

	private ErrorResponse populateValidationErrors(BindingResult errors) {
		String errorMsg = "";
		if (errors.getFieldError() != null) {
			errorMsg = errors.getFieldError().getDefaultMessage();
		}
		ErrorResponse errRes = new ErrorResponse();
		ObjectError validationError = errors.getGlobalError();

		Error error = new Error();
		error.setCode(1);
		error.setMessage(validationError != null ? validationError.getDefaultMessage() : errorMsg);
		error.setDescription(validationError != null ? validationError.getDefaultMessage() : errorMsg);
		errRes.setError(error);
		return errRes;
	}
}
