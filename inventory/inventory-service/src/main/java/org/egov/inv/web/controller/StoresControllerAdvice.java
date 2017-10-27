package org.egov.inv.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.inv.domain.StoreException;
import org.egov.inv.web.adapters.error.CommonErrorAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class StoresControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StoreException.class)
    public ErrorResponse handleInvalidComplaintException(StoreException ex) {
        return new CommonErrorAdapter().adapt(ex.getErrorFields(), "Store Request is invalid");
    }
}
