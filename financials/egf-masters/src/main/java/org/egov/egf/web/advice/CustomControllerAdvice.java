package org.egov.egf.web.advice;

import java.util.List;

import org.egov.egf.domain.exception.CustomBindException;
import org.egov.egf.domain.exception.InvalidDataException;
import org.egov.egf.domain.exception.UnauthorizedAccessException;
import org.egov.egf.persistence.queue.contract.Error;
import org.egov.egf.persistence.queue.contract.ErrorResponse;
import org.egov.egf.persistence.queue.contract.FieldError;
import org.egov.egf.persistence.queue.contract.ResponseInfo;
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
	public String handleMissingParamsError(Exception ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomBindException.class)
	public ErrorResponse handleBindingErrors(CustomBindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		if (errors.getGlobalError() != null) {
			error.setCode(errors.getGlobalError().getCode());
			error.setMessage(errors.getGlobalError().getObjectName());
			error.setDescription(errors.getGlobalError().getDefaultMessage());
		} else {
			if (errors.getFieldErrorCount() > 0)
				error.setDescription("Missing fields");
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				FieldError f = new FieldError(errs.getField(), errs.getDefaultMessage());
				error.getFilelds().add(f);

			}
		}
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataException.class)
	public ErrorResponse handleBindingErrors(InvalidDataException ex) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(InvalidDataException.code);
		error.setMessage(ex.getFieldName());
		error.setDescription(ex.getDefaultMessage());
		errRes.setError(error);

		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public ErrorResponse handleThrowable(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode("Internal Server Error");
		error.setMessage("Throwable");
		error.setDescription(ex.getMessage());
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleServerError(Exception ex) {
		ex.printStackTrace();
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode("Internal Server Error");
		error.setMessage("500");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ErrorResponse handleAuthenticationError(UnauthorizedAccessException ex) {
		ex.printStackTrace();
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.UNAUTHORIZED.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode("Un Authorized Access");
		error.setMessage("404");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

}
