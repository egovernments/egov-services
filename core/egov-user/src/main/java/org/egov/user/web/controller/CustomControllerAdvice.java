package org.egov.user.web.controller;

import org.egov.user.domain.search.NoSearchStrategyFoundException;
import org.egov.user.web.contract.Error;
import org.egov.user.web.contract.ErrorResponse;
import org.egov.user.web.contract.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ExceptionHandler(NoSearchStrategyFoundException.class)
    public ErrorResponse handleBadSearchRequest(NoSearchStrategyFoundException ex) {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .build();
        Error error = new Error(HttpStatus.BAD_REQUEST.value(), "Bad search request", "", null);

        return new ErrorResponse(responseInfo, error);
    }
}
