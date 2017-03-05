package org.egov.web.controller;

import org.egov.domain.InvalidTokenRequestException;
import org.egov.web.contract.Error;
import org.egov.web.contract.ErrorField;
import org.egov.web.contract.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    private static final String INVALID_OTP_REQUEST = "Invalid OTP request";
    public static final String IDENTITY_MANDATORY_CODE = "OTP.IDENTITY_MANDATORY";
    public static final String IDENTITY_MANDATORY_MESSAGE = "Identity field is mandatory";
    public static final String IDENTITY_FIELD = "identity";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTokenRequestException.class)
    public ErrorResponse handleInvalidSearchTypeException() {
        final Error error = createIdentityMandatoryError();
        return new ErrorResponse(null, error);
    }

    private Error createIdentityMandatoryError() {
        final ErrorField errorField = ErrorField.builder()
                .field(IDENTITY_FIELD)
                .code(IDENTITY_MANDATORY_CODE)
                .message(IDENTITY_MANDATORY_MESSAGE)
                .build();
        final List<ErrorField> fields = Collections.singletonList(errorField);
        return new Error(HttpStatus.BAD_REQUEST.value(), INVALID_OTP_REQUEST, null, fields);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getMessage();
    }

}
