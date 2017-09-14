package org.egov.tl.masters.web.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
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
	public ErrorResponse handleMissingParamsError(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setMessage("tl.error.missingparams");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomBindException.class)
	public ErrorResponse handleBindingErrors(CustomBindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		BindingResult errors = ex.getErrors();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((CustomBindException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((CustomBindException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((CustomBindException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		if (errors.getGlobalError() != null) {
			error.setCode(Integer.valueOf(errors.getGlobalError().getCode()));
			error.setMessage(errors.getGlobalError().getObjectName());
			error.setDescription(errors.getGlobalError().getDefaultMessage());
		} else {
			if (errors.getFieldErrorCount() > 0) {
				error.setMessage("tl.error.missingfields");
				error.setCode(400);
				error.setDescription("Missing fields");
			}
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			error.setFields(new ArrayList<>());
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				String errorCode = "tl.error." + errs.getField().substring(errs.getField().indexOf(".") + 1) + "."
						+ errs.getCode();
				ErrorField f = new ErrorField(errorCode.toLowerCase(), errs.getDefaultMessage(), errs.getField());
				error.getFields().add(f);
			}
		}
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ErrorResponse handleCustomBindErrors(BindException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidInputException.class)
	public ErrorResponse handleInvalidInputErrors(InvalidInputException ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	public ErrorResponse handleThrowable(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		return errRes;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleServerError(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();

		error.setCode(500);
		error.setMessage("Internal Server Error");
		error.setDescription(ex.getMessage());
		errRes.setError(error);
		return errRes;
	}
}