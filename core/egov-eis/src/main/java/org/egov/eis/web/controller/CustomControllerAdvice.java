package org.egov.eis.web.controller;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.Date;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleError(Exception ex) {
		ErrorResponse response = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "",
				"Failed to get assignments");
		response.setResponseInfo(responseInfo);
		response.setError(new Error(500, "Failed to get positions", "todo", Collections.emptyList()));
		// TODO: Fill right values
		// error.setCode(400);
		// error.setDescription("Failed to get positions");
        return response;
    }
}
