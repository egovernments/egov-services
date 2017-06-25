package org.egov.web.controller;

import org.egov.domain.model.DuplicateMessageIdentityException;
import org.egov.domain.model.MessagePersistException;
import org.egov.web.contract.Error;
import org.egov.web.contract.ErrorResponse;
import org.egov.web.exception.InvalidMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice {

    private static final String DUPLICATE_MESSAGE_IDENTITY = "core.DUPLICATE_MESSAGE_IDENTITY";
    private static final String UNIQUE_MESSAGE = "Combination of tenant, locale, module and code should be unique.";

    @ExceptionHandler(InvalidMessageRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processValidationError(InvalidMessageRequest ex) {
        List<Error> errors = ex.getErrors().stream()
                .map(e -> new Error(e.getCode(), e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(DuplicateMessageIdentityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDuplicateMessageIdentityException() {
        final Error error = new Error(DUPLICATE_MESSAGE_IDENTITY, DUPLICATE_MESSAGE_IDENTITY, UNIQUE_MESSAGE);
        List<Error> errors = Collections.singletonList(error);
        return new ErrorResponse(errors);
    }

    @ExceptionHandler(MessagePersistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMessagePersistException() {
        final Error error = new Error(DUPLICATE_MESSAGE_IDENTITY, DUPLICATE_MESSAGE_IDENTITY, UNIQUE_MESSAGE);
        List<Error> errors = Collections.singletonList(error);
        return new ErrorResponse(errors);
    }

}