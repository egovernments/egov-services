package org.egov.web.controller;

import org.egov.web.contract.Error;
import org.egov.web.contract.ErrorResponse;
import org.egov.web.exception.InvalidCreateMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(InvalidCreateMessageRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processValidationError(InvalidCreateMessageRequest ex) {
        List<Error> errors = ex.getErrors().stream()
                .map(e -> new Error(e.getCode(), e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(errors);
    }

}