package org.egov.pgrrest.read.web.controller;

import org.egov.pgr.common.model.exception.InvalidOtpValidationRequestException;
import org.egov.pgrrest.read.domain.exception.*;
import org.egov.pgrrest.read.web.adapters.error.*;
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
    @ExceptionHandler(InvalidServiceTypeSearchException.class)
    public ErrorResponse handleInvalidSearchTypeException(InvalidServiceTypeSearchException ex) {
        return new InvalidServiceTypeSearchExceptionAdapter().adapt(ex.getMessage());
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidServiceTypeCodeException.class)
    public ErrorResponse handleInvalidServiceTypeCode(InvalidServiceTypeCodeException ex) {
        return new InvalidServiceTypeExceptionAdapter().adapt(ex.getInvalidServiceTypeCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OtpValidationNotCompleteException.class)
    public ErrorResponse handleOtpValidationNotCompleteException() {
        return new OtpValidationNotCompleteExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidOtpValidationRequestException.class)
    public ErrorResponse handleInvalidOtpValidationRequestException() {
        return new InvalidOtpValidationRequestExceptionAdapter().adapt(null);
    }

    @ExceptionHandler(UpdateComplaintNotAllowed.class)
    public ErrorResponse handleUpdateComplaintException() {
        return new UpdateComplaintNotAllowedExceptionAdapter().adapt(null);
    }
}
