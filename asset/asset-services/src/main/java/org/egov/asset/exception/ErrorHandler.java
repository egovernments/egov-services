package org.egov.asset.exception;

import org.egov.asset.web.wrapperfactory.ResponseInfoFactory;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
public class ErrorHandler {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;

	
	/*public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingHeaders(HttpHeaders headers) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing Header Informatin");
		error.setDescription("Required Header(s) Not Found");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestHeaders(headers, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}*/

	/*public ResponseEntity<ErrorResponse> getErrorResponseEntityForMissingParametes(BindingResult bindingResult, HttpHeaders headers) {
		Error error = new Error();
		error.setCode(400);
		error.setMessage("Missing Request Parameter");
		error.setDescription("Error While Binding Request");
		if (bindingResult.hasFieldErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				error.getFields().put(fieldError.getField(), fieldError.getRejectedValue());
			}
		}

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestHeaders(headers, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ErrorResponse> getResponseEntityForUnexpectedErrors(HttpHeaders headers) {
		Error error = new Error();
		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription("Unexpected Error Occurred");

		ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestHeaders(headers, false);

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setResponseInfo(responseInfo);
		errorResponse.setError(error);

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}*/
}
