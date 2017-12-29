package org.egov.lams.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.egov.lams.model.AgreementInfo;
import org.egov.lams.service.ReportService;
import org.egov.lams.web.contract.AgreementInfoResponse;
import org.egov.lams.web.contract.BaseRegisterRequest;
import org.egov.lams.web.contract.RenewalPendingRequest;
import org.egov.lams.web.contract.RequestInfo;
import org.egov.lams.web.contract.RequestInfoWrapper;
import org.egov.lams.web.contract.ResponseInfo;
import org.egov.lams.web.contract.factory.ResponseInfoFactory;
import org.egov.lams.web.errorhandlers.Error;
import org.egov.lams.web.errorhandlers.ErrorResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("agreements/reports")
public class ReportController {
	public static final Logger LOGGER = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private ReportService reportService;

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	/*
	 * Base register report
	 */
	@PostMapping("_baseregisterreport")
	@ResponseBody
	public ResponseEntity<?> baseregisterreport(@ModelAttribute @Valid BaseRegisterRequest baseRegisterReportRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		List<AgreementInfo> agreementList = null;

		try {
			agreementList = reportService.getAgreementDetails(baseRegisterReportRequest);
		} catch (Exception exception) {
			log.error("Error while processing request " + baseRegisterReportRequest, exception);
			ErrorResponse errorResponse = populateError(requestBodyBindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		return getSuccessResponseForSearch(agreementList, requestInfo);
	}

	@PostMapping("_renewalpending")
	@ResponseBody
	public ResponseEntity<?> renewalPendinReport(
			@ModelAttribute @Valid RenewalPendingRequest renewalPendingReportRequest,
			BindingResult modelAttributeBindingResult, @RequestBody @Valid RequestInfoWrapper requestInfoWrapper,
			BindingResult requestBodyBindingResult) {
		RequestInfo requestInfo = requestInfoWrapper.getRequestInfo();

		List<AgreementInfo> agreementList = null;

		try {
			agreementList = reportService.getAgreementRenewalPendingDetails(renewalPendingReportRequest);
		} catch (Exception exception) {
			log.error("Error while processing request " + renewalPendingReportRequest, exception);
			ErrorResponse errorResponse = populateError(requestBodyBindingResult);
			return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
		}

		return getSuccessResponseForSearch(agreementList, requestInfo);

	}

	private ErrorResponse populateError(BindingResult errors) {
		ErrorResponse errRes = new ErrorResponse();
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Internal Server Error");
		if (errors.hasFieldErrors()) {
			for (FieldError errs : errors.getFieldErrors()) {
				error.getFields().put(errs.getField(), errs.getDefaultMessage());
			}
		}
		errRes.setError(error);
		return errRes;
	}

	private ResponseEntity<?> getSuccessResponseForSearch(List<AgreementInfo> agreementList, RequestInfo requestInfo) {
		AgreementInfoResponse agreementInfoResponse = new AgreementInfoResponse();
		agreementInfoResponse.setAgreements(agreementList);
		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		agreementInfoResponse.setResponseInfo(responseInfo);
		return new ResponseEntity<>(agreementInfoResponse, HttpStatus.OK);
	}
}
