package org.egov.web.controller;

import org.egov.domain.exception.*;
import org.egov.web.adapter.error.*;
import org.egov.web.contract.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTokenRequestException.class)
    public ErrorResponse handleInvalidSearchTypeException(InvalidTokenRequestException ex) {
        return new TokenRequestErrorAdapter().adapt(ex.getTokenRequest());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTokenValidateRequestException.class)
    public ErrorResponse handleInvalidSearchTypeException(InvalidTokenValidateRequestException ex) {
        return new TokenValidationRequestErrorAdapter().adapt(ex.getValidateRequest());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenUpdateException.class)
    public ErrorResponse handleTokenUpdateException(TokenUpdateException ex) {
        return new TokenUpdateErrorAdapter().adapt(ex.getToken());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTokenSearchCriteriaException.class)
    public ErrorResponse handleInvalidSearchException(InvalidTokenSearchCriteriaException ex) {
        return new TokenSearchErrorAdapter().adapt(ex.getTokenSearchCriteria());
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(TokenValidationFailureException.class)
	public ErrorResponse handleTokenValidationFailureException() {
		return new TokenValidationFailureAdapter().adapt(null);
	}

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

}
