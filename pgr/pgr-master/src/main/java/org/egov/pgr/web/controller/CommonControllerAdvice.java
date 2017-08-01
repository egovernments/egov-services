package org.egov.pgr.web.controller;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.pgr.domain.exception.ServiceCodeMandatoryException;
import org.egov.pgr.domain.exception.TenantIdMandatoryException;
import org.egov.pgr.web.adapters.error.ServiceCodeMandatoryExceptionAdapter;
import org.egov.pgr.web.adapters.error.TenantIdMandatoryExceptionAdapter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CommonControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TenantIdMandatoryException.class)
    public ErrorResponse handleTenantIdMandatoryException() {
        return new TenantIdMandatoryExceptionAdapter().adapt(null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceCodeMandatoryException.class)
    public ErrorResponse handleServiceCodeMandatoryException() {
        return new ServiceCodeMandatoryExceptionAdapter().adapt(null);
    }
}
