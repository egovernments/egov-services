package org.egov.user.web.controller;

import org.egov.user.domain.exception.DuplicateUserNameException;
import org.egov.user.domain.exception.InvalidUserException;
import org.egov.user.domain.exception.OtpValidationPendingException;
import org.egov.user.domain.exception.UserNotFoundException;
import org.egov.user.web.adapters.errors.DuplicateUserNameErrorHandler;
import org.egov.user.web.adapters.errors.OtpValidationErrorAdapter;
import org.egov.user.web.adapters.errors.UserNotFoundErrorHandler;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateUserNameException.class)
    public ErrorRes handleDuplicateUserNameException(DuplicateUserNameException ex) {
        return new DuplicateUserNameErrorHandler().adapt(ex.getUser());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OtpValidationPendingException.class)
    public ErrorRes handleInvalidComplaintException(OtpValidationPendingException ex) {
        return new OtpValidationErrorAdapter().adapt(ex.getUser());
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorRes handleUserNotFoundException(UserNotFoundException ex) {
        return new UserNotFoundErrorHandler().adapt(ex.getUser());
    }
}
