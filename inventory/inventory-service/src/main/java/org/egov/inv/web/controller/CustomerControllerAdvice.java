package org.egov.inv.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.inv.domain.MaterialException;
import org.egov.inv.web.adapters.error.CommonErrorAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@ControllerAdvice
@RestController
public class CustomerControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaterialException.class)
    public ErrorResponse handleInvalidComplaintException(MaterialException ex) {
        return new CommonErrorAdapter().adapt(ex.getErrorFields(), "Material Request is invalid");
    }


}
