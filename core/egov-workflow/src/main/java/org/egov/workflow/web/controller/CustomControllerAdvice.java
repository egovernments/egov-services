package org.egov.workflow.web.controller;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.workflow.domain.exception.InvalidComplaintStatusException;
import org.egov.workflow.domain.exception.InvalidComplaintStatusSearchException;
import org.egov.workflow.web.adaptor.error.ComplaintStatusSearchErrorAdaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(InvalidComplaintStatusSearchException ex) {
        return new ComplaintStatusSearchErrorAdaptor().adapt(ex.getCriteria());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(InvalidComplaintStatusException ex) {
        Error error = Error.builder().message("Complaint status is invalid").build();
        return ErrorResponse.builder().error(error).build();
    }
}
