package org.egov.pgrrest.read.web.controller;

import org.egov.pgrrest.read.domain.exception.InvalidComplaintException;
import org.egov.pgrrest.read.domain.exception.InvalidComplaintTypeSearchException;
import org.egov.pgrrest.read.domain.exception.ServiceDefinitionNotFoundException;
import org.egov.pgrrest.read.web.adapters.error.ServiceDefinitionNotFoundExceptionAdapter;
import org.egov.pgrrest.read.web.adapters.error.SevaRequestErrorAdapter;
import org.egov.pgrrest.read.web.contract.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidComplaintTypeSearchException.class)
    public ErrorResponse handleInvalidSearchTypeException(Exception ex) {
        //TODO: Fix this
        return new ErrorResponse(null, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidComplaintException.class)
    public ErrorResponse handleInvalidComplaintException(InvalidComplaintException ex) {
        return new SevaRequestErrorAdapter().adapt(ex.getComplaint());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceDefinitionNotFoundException.class)
    public ErrorResponse handleInvalidServiceDefinitionCode() {
        return new ServiceDefinitionNotFoundExceptionAdapter().adapt(null);
    }

}
