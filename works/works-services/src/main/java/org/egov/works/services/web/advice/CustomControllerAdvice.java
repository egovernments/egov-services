package org.egov.works.services.web.advice;

import org.egov.works.commons.web.contract.ErrorRes;
import org.egov.works.commons.web.contract.ResponseInfo;
import org.egov.works.services.domain.exception.CustomBindException;
import org.egov.works.services.domain.exception.ErrorCode;
import org.egov.works.services.domain.exception.InvalidDataException;
import org.egov.works.services.domain.exception.UnauthorizedAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

	private static final Logger LOG = LoggerFactory.getLogger(CustomControllerAdvice.class);

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMissingParamsError(Exception ex) {
		return ex.getMessage();
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomBindException.class)
	public ErrorRes handleBindingErrors(CustomBindException ex) {
		ErrorRes errRes = new ErrorRes();
		BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();
		if (errors.getGlobalError() != null) {
			if (errors.getGlobalError().getCode() != null) {
				error.setCode(errors.getGlobalError().getCode());
				error.setMessage(errors.getGlobalError().getObjectName());
				error.setDescription(errors.getGlobalError().getDefaultMessage());
			} else if (ErrorCode.getError(errors.getGlobalError().getCode()) != null) {
				error.setCode(ErrorCode.getError(errors.getGlobalError().getDefaultMessage()).getCode());
				String message = MessageFormat.format(
						ErrorCode.getError(errors.getGlobalError().getDefaultMessage()).getMessage(),
						errors.getGlobalError().getObjectName());
				error.setMessage(message);
				String desc = MessageFormat.format(
						ErrorCode.getError(errors.getGlobalError().getDefaultMessage()).getDescription(),
						errors.getGlobalError().getObjectName());
				error.setDescription(desc);
			}
			errRes.getErrors().add(error);

		}

		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				org.egov.works.commons.web.contract.Error err = new org.egov.works.commons.web.contract.Error();
				if (ErrorCode.getError(errs.getCode()) != null) {
					err.setCode(errs.getCode());
					String message = MessageFormat.format(ErrorCode.getError(errs.getCode()).getMessage(),
							errs.getField(), errs.getRejectedValue());
					err.setMessage(message);
					String desc = MessageFormat.format(ErrorCode.getError(errs.getCode()).getDescription(),
							errs.getField(), errs.getRejectedValue());
					err.setDescription(desc);
					err.getParams().add(errs.getField());
					err.getParams().add((String) errs.getRejectedValue());
				} else {
					err.setCode(errs.getCode());
					err.setMessage(errs.getDefaultMessage());
					err.setDescription(errs.getField());
				}
				errRes.getErrors().add(err);
			}

		}

		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDataException.class)
	public ErrorRes handleBindingErrors(InvalidDataException ex) {
		ErrorRes errRes = new ErrorRes();

		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();
		error.setCode(ex.getMessageKey());
		System.out.println(ErrorCode.getError("non.unique.value"));
		if (ErrorCode.getError(ex.getMessageKey()) != null) {
			String message = MessageFormat.format(ErrorCode.getError(ex.getMessageKey()).getMessage(),
					ex.getFieldName(), ex.getFieldValue());
			error.setMessage(message);
			String desc = MessageFormat.format(ErrorCode.getError(ex.getMessageKey()).getDescription(),
					ex.getFieldName(), ex.getFieldValue());
			error.setDescription(desc);
		} else {
			LOG.warn("error code is not defined for " + ex.getMessageKey());
		}
		List<org.egov.works.commons.web.contract.Error> errors = new ArrayList<org.egov.works.commons.web.contract.Error>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(org.apache.kafka.common.errors.TimeoutException.class)
	public ErrorRes handleThrowable(org.apache.kafka.common.errors.TimeoutException ex) {
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();

		error.setCode(ErrorCode.KAFKA_TIMEOUT_ERROR.getCode());
		error.setMessage(ErrorCode.KAFKA_TIMEOUT_ERROR.getMessage());
		error.setDescription(ErrorCode.KAFKA_TIMEOUT_ERROR.getDescription());
		List<org.egov.works.commons.web.contract.Error> errors = new ArrayList<org.egov.works.commons.web.contract.Error>();
		errors.add(error);
		errRes.setErrors(errors);
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public ErrorRes handleThrowable(Exception ex) {
		ErrorRes errRes = new ErrorRes();
		ex.printStackTrace();
		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();

		error.setCode("500");
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorRes handleServerError(Exception ex) {
		ex.printStackTrace();
		ErrorRes errRes = new ErrorRes();

		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();

		error.setCode("500");
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		errRes.addErrorsItem(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedAccessException.class)
	public ErrorRes handleAuthenticationError(UnauthorizedAccessException ex) {
		ex.printStackTrace();
		ErrorRes errRes = new ErrorRes();

		ResponseInfo responseInfo = new ResponseInfo();
		errRes.setResponseInfo(responseInfo);
		org.egov.works.commons.web.contract.Error error = new org.egov.works.commons.web.contract.Error();

		error.setCode("404");
		error.setMessage("Un Authorized Access");
		error.setDescription(ex.getMessage());
		errRes.addErrorsItem(error);
		return errRes;
	}

}