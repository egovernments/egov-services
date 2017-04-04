package org.egov.user.web.controller;

import org.egov.user.domain.exception.*;
import org.egov.user.web.adapters.errors.*;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidAccessTokenException.class)
    public ErrorRes accessTokenException(Exception ex) {
        return new InvalidAccessTokenErrorHandler().adapt();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserDetailsException.class)
    public ErrorRes userDetailsException(Exception ex) {
        return new UserDetailsErrorHandler().adapt();
    }
}