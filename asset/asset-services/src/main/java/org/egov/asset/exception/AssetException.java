package org.egov.asset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AssetException {
	
	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
		
		ErrorResponse errorResponse = new ErrorResponse();
		Error error=new Error();
		error.setCode(400);
		error.setMessage(e.getMessage());
		errorResponse.setError(error);
		e.printStackTrace();
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
