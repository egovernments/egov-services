package org.egov.user.web.controller;

import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.web.adapters.errors.UserRequestErrorAdapter;
import org.egov.user.web.contract.ErrorRes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class UserControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserException.class)
    public ErrorRes handleInvalidComplaintException(InvalidUserException ex) {
        return new UserRequestErrorAdapter().adapt(ex.getUser());
    }

}
