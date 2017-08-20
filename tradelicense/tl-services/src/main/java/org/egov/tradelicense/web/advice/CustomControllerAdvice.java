package org.egov.tradelicense.web.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tradelicense.common.domain.exception.AdhaarNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentDateNotFoundException;
import org.egov.tradelicense.common.domain.exception.AgreeMentNotFoundException;
import org.egov.tradelicense.common.domain.exception.CustomBindException;
import org.egov.tradelicense.common.domain.exception.DuplicateTradeLicenseException;
import org.egov.tradelicense.common.domain.exception.InvalidAdminWardException;
import org.egov.tradelicense.common.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidDocumentTypeException;
import org.egov.tradelicense.common.domain.exception.InvalidFeeDetailException;
import org.egov.tradelicense.common.domain.exception.InvalidInputException;
import org.egov.tradelicense.common.domain.exception.InvalidLocalityException;
import org.egov.tradelicense.common.domain.exception.InvalidPropertyAssesmentException;
import org.egov.tradelicense.common.domain.exception.InvalidRevenueWardException;
import org.egov.tradelicense.common.domain.exception.InvalidSubCategoryException;
import org.egov.tradelicense.common.domain.exception.InvalidUomException;
import org.egov.tradelicense.common.domain.exception.InvalidValidityYearsException;
import org.egov.tradelicense.common.domain.exception.LicenseNotFoundException;
import org.egov.tradelicense.common.domain.exception.PropertyAssesmentNotFoundException;
import org.egov.tradelicense.web.adapters.error.AdhaarNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.AgreeMentDateNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.AgreeMentNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.DuplicateTradeLicenseAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidAdminWardAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidCategoryAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidDocumentTypeAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidFeeDetailAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidLocalityAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidPropertyAssesmentAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidRevenueWardAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidSubCategoryAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidUomAdapter;
import org.egov.tradelicense.web.adapters.error.InvalidValidityYearsAdapter;
import org.egov.tradelicense.web.adapters.error.LicenseNotFoundAdapter;
import org.egov.tradelicense.web.adapters.error.PropertyAssesmentNotFoundAdapter;
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
				// error.setDescription("Missing fields");
			}
		}
		if (errors.hasFieldErrors()) {
			List<org.springframework.validation.FieldError> fieldErrors = errors.getFieldErrors();
			for (org.springframework.validation.FieldError errs : fieldErrors) {
				String errorCode = "tl.error." + errs.getField().substring(errs.getField().indexOf(".") + 1) + "."
						+ errs.getCode();
				ErrorField f = new ErrorField(errorCode.toLowerCase(), errs.getDefaultMessage(), errs.getField());
				error.setFields(new ArrayList<>());
				error.getFields().add(f);

			}
		}
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
		// error.setDescription(ex.getCustomMsg());
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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AdhaarNotFoundException.class)
	public ErrorResponse handleAdhaarNotFoundException(AdhaarNotFoundException ex) {
		return new AdhaarNotFoundAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AgreeMentDateNotFoundException.class)
	public ErrorResponse handleAgreeMentDateNotFoundException(AgreeMentDateNotFoundException ex) {
		return new AgreeMentDateNotFoundAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AgreeMentNotFoundException.class)
	public ErrorResponse handleAgreeMentNotFoundException(AgreeMentNotFoundException ex) {
		return new AgreeMentNotFoundAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DuplicateTradeLicenseException.class)
	public ErrorResponse handleDuplicateTradeLicenseException(DuplicateTradeLicenseException ex) {
		return new DuplicateTradeLicenseAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidAdminWardException.class)
	public ErrorResponse handleInvalidAdminWardException(InvalidAdminWardException ex) {
		return new InvalidAdminWardAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidCategoryException.class)
	public ErrorResponse handleInvalidCategoryException(InvalidCategoryException ex) {
		return new InvalidCategoryAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidDocumentTypeException.class)
	public ErrorResponse handleInvalidDocumentTypeException(InvalidDocumentTypeException ex) {
		return new InvalidDocumentTypeAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidFeeDetailException.class)
	public ErrorResponse handleInvalidFeeDetailException(InvalidFeeDetailException ex) {
		return new InvalidFeeDetailAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidLocalityException.class)
	public ErrorResponse handleInvalidLocalityException(InvalidLocalityException ex) {
		return new InvalidLocalityAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidPropertyAssesmentException.class)
	public ErrorResponse handleInvalidPropertyAssesmentException(InvalidPropertyAssesmentException ex) {
		return new InvalidPropertyAssesmentAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRevenueWardException.class)
	public ErrorResponse handleInvalidRevenueWardException(InvalidRevenueWardException ex) {
		return new InvalidRevenueWardAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidSubCategoryException.class)
	public ErrorResponse handleInvalidSubCategoryException(InvalidSubCategoryException ex) {
		return new InvalidSubCategoryAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidUomException.class)
	public ErrorResponse handleInvalidUomException(InvalidUomException ex) {
		return new InvalidUomAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidValidityYearsException.class)
	public ErrorResponse handleInvalidValidityYearsException(InvalidValidityYearsException ex) {
		return new InvalidValidityYearsAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(LicenseNotFoundException.class)
	public ErrorResponse handleLicenseNotFoundException(LicenseNotFoundException ex) {
		return new LicenseNotFoundAdapter().getErrorResponse(ex.getRequestInfo());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(PropertyAssesmentNotFoundException.class)
	public ErrorResponse handleInvalidpropertyAssementException(PropertyAssesmentNotFoundException ex) {
		return new PropertyAssesmentNotFoundAdapter().getErrorResponse(ex.getRequestInfo());
	}

}