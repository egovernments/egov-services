package org.egov.tenant.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.tenant.domain.exception.InvalidTenantDetailsException;
import org.egov.tenant.web.adapter.TenantCreateRequestErrorAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class TenantControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidTenantDetailsException.class)
    public ErrorResponse handleInvalidTenantDetailsException(InvalidTenantDetailsException ex) {
        return new TenantCreateRequestErrorAdapter().adapt(ex.getTenant());
    }
}
