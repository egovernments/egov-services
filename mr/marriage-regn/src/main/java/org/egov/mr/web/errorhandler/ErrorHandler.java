package org.egov.mr.web.errorhandler;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.mr.model.RegistrationUnit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorHandler {

	// Search
	public ResponseEntity<?> handleBindingErrorsForSearch(RequestInfo requestInfo,
			BindingResult bindingResultsForRequestInfo, BindingResult bindingResultsForSearchCriteria) {
		if (bindingResultsForRequestInfo != null && bindingResultsForRequestInfo.hasErrors()) {
			return getMissingParameterErrorResponse(bindingResultsForRequestInfo, requestInfo);
		}
		if (bindingResultsForSearchCriteria != null && bindingResultsForSearchCriteria.hasErrors()) {
			return getMissingParameterErrorResponse(bindingResultsForSearchCriteria, requestInfo);
		}
		return null;
	}

	// Create
	public ResponseEntity<?> handleBindingErrorsForCreate(RequestInfo requestInfo, BindingResult bindingResult) {
		if (bindingResult != null && bindingResult.hasErrors()) {
			return getMissingParameterErrorResponse(bindingResult, requestInfo);
		}
		return null;
	}

	// Update
	public ResponseEntity<?> handleBindingErrorsForUpdate(RequestInfo requestInfo, BindingResult bindingResult) {
		if (bindingResult != null && bindingResult.hasErrors()) {
			return getMissingParameterErrorResponse(bindingResult, requestInfo);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ResponseEntity<?> getMissingParameterErrorResponse(BindingResult bindingResults, RequestInfo requestInfo) {

		Error error = new Error();
		// Setting Error
		error.setCode(400);
		@SuppressWarnings("rawtypes")
		List fieldErrorsList = new ArrayList();
		if (bindingResults.hasFieldErrors()) {
			for (FieldError fieldError : bindingResults.getFieldErrors()) {
				fieldErrorsList.add(fieldError.getField());
			}
		}
		error.setMessage(fieldErrorsList + " : Required Query Parameter Missing ");
		error.setDescription("Error While Binding Results");
		error.setFields(fieldErrorsList);

		ResponseInfo responseInfo = new ResponseInfo();
		// Setting ResponseInfo
		responseInfo.setApiId(requestInfo.getApiId());
		//responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		//responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());

		ErrorResponse errRes = new ErrorResponse();
		// Setting ErrorResponse
		errRes.setError(error);
		errRes.setResponseInfo(responseInfo);

		return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getUnExpectedErrorResponse(BindingResult bindingResults, RequestInfo requestInfo,
			String errorCause) {
		Error error = new Error();
		// Setting Error
		error.setCode(500);
		error.setMessage("UnExpected ERROR! While Processing the Request");
		error.setDescription(errorCause);

		ResponseInfo responseInfo = new ResponseInfo();
		// Setting ResponseInfo
		responseInfo.setApiId(requestInfo.getApiId());
		//responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		//responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());

		ErrorResponse errRes = new ErrorResponse();
		// Setting ErrorResponse
		errRes.setError(error);
		errRes.setResponseInfo(responseInfo);

		return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<?> getUpdateErrorResponse(BindingResult bindingResults, RequestInfo requestInfo,
			RegistrationUnit registrationUnit) {
		Error error = new Error();
		// Setting Error
		error.setCode(400);
		error.setMessage("No Record Found!");
		error.setDescription("Record with Id = " + registrationUnit.getId() + " & tenantId = "
				+ registrationUnit.getTenantId() + " doesn't exist in DB");

		ResponseInfo responseInfo = new ResponseInfo();
		// Setting ResponseInfo
		responseInfo.setApiId(requestInfo.getApiId());
		//responseInfo.setKey(requestInfo.getKey());
		responseInfo.setResMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		//responseInfo.setTenantId(requestInfo.getTenantId());
		responseInfo.setTs(requestInfo.getTs());
		responseInfo.setVer(requestInfo.getVer());

		ErrorResponse errRes = new ErrorResponse();
		// Setting ErrorResponse
		errRes.setError(error);
		errRes.setResponseInfo(responseInfo);

		return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
	}
}
