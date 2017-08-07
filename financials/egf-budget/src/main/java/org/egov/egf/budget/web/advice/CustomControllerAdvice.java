package org.egov.egf.budget.web.advice;

import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.common.domain.exception.CustomBindException;
import org.egov.common.domain.exception.InvalidDataException;
import org.egov.common.domain.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParamsError(final Exception ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomBindException.class)
    public ErrorResponse handleBindingErrors(final CustomBindException ex) {
        final ErrorResponse errRes = new ErrorResponse();
        final BindingResult errors = ex.getErrors();
        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();
        if (errors.getGlobalError() != null) {
            error.setCode(Integer.valueOf(errors.getGlobalError().getCode()));
            error.setMessage(errors.getGlobalError().getObjectName());
            error.setDescription(errors.getGlobalError().getDefaultMessage());
        } else if (errors.getFieldErrorCount() > 0)
            error.setDescription("Missing fields");
        if (errors.hasFieldErrors()) {
            final List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
            for (final org.springframework.validation.FieldError errs : fieldErrors) {
                final ErrorField f = new ErrorField(errs.getCode(), errs.getDefaultMessage(), errs.getField());
                error.getFields().add(f);

            }
        }
        errRes.setError(error);
        return errRes;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ErrorResponse handleBindingErrors(final InvalidDataException ex) {
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();
        error.setCode(Integer.valueOf(InvalidDataException.code));
        error.setMessage(ex.getFieldName());
        error.setDescription(ex.getDefaultMessage());
        errRes.setError(error);

        return errRes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleThrowable(final Exception ex) {
        final ErrorResponse errRes = new ErrorResponse();
        ex.printStackTrace();
        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(500);
        error.setMessage("Internal Server Error");
        error.setDescription(ex.getMessage());
        return errRes;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleServerError(final Exception ex) {
        ex.printStackTrace();
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(500);
        error.setMessage("Internal Server Error");
        error.setDescription(ex.getMessage());
        errRes.setError(error);
        return errRes;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorResponse handleAuthenticationError(final UnauthorizedAccessException ex) {
        ex.printStackTrace();
        final ErrorResponse errRes = new ErrorResponse();

        final ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setStatus(HttpStatus.UNAUTHORIZED.toString());
        errRes.setResponseInfo(responseInfo);
        final Error error = new Error();

        error.setCode(404);
        error.setMessage("Un Authorized Access");
        error.setDescription(ex.getMessage());
        errRes.setError(error);
        return errRes;
    }

}
