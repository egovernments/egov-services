package org.egov.pgr.read.web.controller;

import org.egov.pgr.read.domain.exception.InvalidComplaintException;
import org.egov.pgr.read.domain.exception.InvalidComplaintTypeSearchException;
import org.egov.pgr.read.domain.exception.UnauthorizedAccessException;
import org.egov.pgr.read.web.adapters.error.SevaRequestErrorAdapter;
import org.egov.pgr.read.web.contract.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleAuthenticationError(UnauthorizedAccessException ex) {
        return ex.getMessage();
    }

}
