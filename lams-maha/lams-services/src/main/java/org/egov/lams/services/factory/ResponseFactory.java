package org.egov.lams.services.factory;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

@Configuration
public class ResponseFactory {

	public ResponseInfo getResponseInfo(RequestInfo requestInfo, HttpStatus status) {

		ResponseInfo responseInfo = new ResponseInfo();
		if (requestInfo != null) {
			responseInfo.setApiId(requestInfo.getApiId());
			responseInfo.setMsgId(requestInfo.getMsgId());
			// responseInfo.setResMsgId(requestInfo.get);
			responseInfo.setStatus(status.toString());
			responseInfo.setVer(requestInfo.getVer());
			responseInfo.setTs(requestInfo.getTs());
		}
		return responseInfo;
	}

	public ErrorResponse getErrorResponse(Errors bindingResult, RequestInfo requestInfo) {

		org.egov.common.contract.response.Error error = new Error();
		error.setCode(400);
		error.setMessage("Mandatory Fields Missing");
		error.setDescription("Mandatory Fields Missing");
		List<ErrorField> errorFields =  new ArrayList<ErrorField>();
		error.setFields(errorFields);
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			ErrorField errorField = new ErrorField(fieldError.getCode(), fieldError.getDefaultMessage(),fieldError.getField());
			errorFields.add(errorField);
		}
		return new ErrorResponse(getResponseInfo(requestInfo, HttpStatus.BAD_REQUEST), error);
	}
}
