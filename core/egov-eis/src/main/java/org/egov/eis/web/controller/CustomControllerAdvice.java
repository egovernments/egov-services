package org.egov.eis.web.controller;

import org.egov.eis.web.contract.Error;
import org.egov.eis.web.contract.ErrorRes;
import org.egov.eis.web.contract.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorRes handleError(Exception ex) {
        ex.printStackTrace();
        ErrorRes response = new ErrorRes();
        ResponseInfo responseInfo =
                new ResponseInfo("", "", new Date().toString(), "", "",
                        "Failed to get positions");
        response.setResponseInfo(responseInfo);
        //TODO: Fill right values
        Error error = new Error(null, null, null);
//        error.setCode(400);
//        error.setDescription("Failed to get positions");
        response.setError(error);
        return response;
    }
}
