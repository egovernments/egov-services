package org.egov.tl.masters.web.advice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.tl.commons.web.contract.Error;
import org.egov.tl.commons.web.contract.ErrorRes;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.masters.web.adapters.error.DuplicateDocumentTypeAdapter;
import org.egov.tradelicense.config.PropertiesManager;
import org.egov.tradelicense.domain.exception.CustomBindException;
import org.egov.tradelicense.domain.exception.DuplicateCategoryCodeException;
import org.egov.tradelicense.domain.exception.DuplicateCategoryDetailException;
import org.egov.tradelicense.domain.exception.DuplicateCategoryNameException;
import org.egov.tradelicense.domain.exception.DuplicateDocumentTypeException;
import org.egov.tradelicense.domain.exception.DuplicateIdException;
import org.egov.tradelicense.domain.exception.DuplicateNameException;
import org.egov.tradelicense.domain.exception.DuplicateSubCategoryCodeException;
import org.egov.tradelicense.domain.exception.DuplicateSubCategoryNameException;
import org.egov.tradelicense.domain.exception.DuplicateUomCodeException;
import org.egov.tradelicense.domain.exception.DuplicateUomNameException;
import org.egov.tradelicense.domain.exception.EndPointException;
import org.egov.tradelicense.domain.exception.InvalidCategoryException;
import org.egov.tradelicense.domain.exception.InvalidDocumentTypeException;
import org.egov.tradelicense.domain.exception.InvalidInputException;
import org.egov.tradelicense.domain.exception.InvalidRangeException;
import org.egov.tradelicense.domain.exception.InvalidSubCategoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class CustomControllerAdvice {

	@Autowired
	PropertiesManager propertiesManager;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ErrorResponse handleMissingParamsError(Exception ex) {
		ErrorResponse errRes = new ErrorResponse();
		org.egov.common.contract.response.ResponseInfo responseInfo = new org.egov.common.contract.response.ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new org.egov.common.contract.response.Error();
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
		org.egov.common.contract.response.ResponseInfo responseInfo = new org.egov.common.contract.response.ResponseInfo();
		responseInfo.setApiId(((CustomBindException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((CustomBindException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((CustomBindException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new org.egov.common.contract.response.Error();
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
		org.egov.common.contract.response.ResponseInfo responseInfo = new 	org.egov.common.contract.response.ResponseInfo();
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new 	org.egov.common.contract.response.Error();
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
		org.egov.common.contract.response.ResponseInfo responseInfo = new 	org.egov.common.contract.response.ResponseInfo();
		responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new 	org.egov.common.contract.response.Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EndPointException.class)
	public ErrorResponse handleEndpointException(EndPointException ex) {
		ErrorResponse errRes = new ErrorResponse();
		org.egov.common.contract.response.ResponseInfo responseInfo = new 	org.egov.common.contract.response.ResponseInfo();
		responseInfo.setApiId(((EndPointException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((EndPointException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((EndPointException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new org.egov.common.contract.response.Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage(propertiesManager.getEndpointExceptionMsg());
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidRangeException.class)
	public ErrorResponse handleInvalidRangeErrors(InvalidRangeException ex) {
		ErrorResponse errRes = new ErrorResponse();
		org.egov.common.contract.response.ResponseInfo responseInfo = new 	org.egov.common.contract.response.ResponseInfo();
		responseInfo.setApiId(((InvalidRangeException) ex).getRequestInfo().getApiId());
		responseInfo.setVer(((InvalidRangeException) ex).getRequestInfo().getVer());
		responseInfo.setMsgId(((InvalidRangeException) ex).getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		errRes.setResponseInfo(responseInfo);
		org.egov.common.contract.response.Error error = new org.egov.common.contract.response.Error();
		error.setCode(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
		error.setMessage("Inavlid.Input");
		error.setDescription(ex.getCustomMsg());
		errRes.setError(error);
		return errRes;
	}

	

	/**
	 * Description : MethodArgumentNotValidException type exception handler
	 * 
	 * @param ex
	 * @return
	 */

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes processValidationError(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<String, String>();

		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}

		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(),
				errors.toString(), null);
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(error);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(propertiesManager.getFailedStatus());
		return new ErrorRes(responseInfo, errorList);
	}

	/**
	 * Description : General exception handler method
	 * 
	 * @param ex
	 * @param req
	 * @return
	 */

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes unknownException(Exception ex, WebRequest req) {
		if (ex instanceof InvalidInputException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(),
					((InvalidInputException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof DuplicateIdException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getDuplicateCode(),
					((DuplicateIdException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateIdException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateIdException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateIdException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof DuplicateCategoryDetailException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getDuplicateCategoryDetail(),
					((DuplicateCategoryDetailException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateCategoryDetailException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateCategoryDetailException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateCategoryDetailException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof DuplicateNameException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getDuplicateName(),
					((DuplicateNameException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateNameException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateNameException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateNameException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}  else if (ex instanceof DuplicateDocumentTypeException) {
			List<Error> errorList = new ArrayList<Error>();
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateDocumentTypeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateDocumentTypeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateDocumentTypeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			errorList.add(new DuplicateDocumentTypeAdapter().getErrorResponse(
					((DuplicateDocumentTypeException) ex).getCustomMsg(),
					((DuplicateDocumentTypeException) ex).getRequestInfo()));
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidCategoryException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidCategoryIdMsg(),
					((InvalidCategoryException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidCategoryException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidCategoryException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidCategoryException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidSubCategoryException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getSubCategoryCustomMsg(),
					((InvalidSubCategoryException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidSubCategoryException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidSubCategoryException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidSubCategoryException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

		else if (ex instanceof InvalidDocumentTypeException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidDocumentTypeIdMsg(),
					((InvalidDocumentTypeException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidDocumentTypeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidDocumentTypeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidDocumentTypeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		

		else if (ex instanceof DuplicateUomCodeException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getUomDuplicateCode(),
					((DuplicateUomCodeException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateUomCodeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateUomCodeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateUomCodeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else if (ex instanceof DuplicateUomNameException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getUomDuplicateName(),
					((DuplicateUomNameException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateUomNameException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateUomNameException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateUomNameException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else if (ex instanceof DuplicateCategoryCodeException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getCategoryDuplicateCode(),
					((DuplicateCategoryCodeException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateCategoryCodeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateCategoryCodeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateCategoryCodeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else if (ex instanceof DuplicateCategoryNameException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getCategoryDuplicateName(),
					((DuplicateCategoryNameException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateCategoryNameException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateCategoryNameException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateCategoryNameException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else if (ex instanceof DuplicateSubCategoryCodeException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getSubCategoryDuplicateCode(),
					((DuplicateSubCategoryCodeException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateSubCategoryCodeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateSubCategoryCodeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateSubCategoryCodeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else if (ex instanceof DuplicateSubCategoryNameException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getSubCategoryDuplicateName(),
					((DuplicateSubCategoryNameException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateSubCategoryNameException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateSubCategoryNameException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateSubCategoryNameException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		}
		
		else {

			Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), ex.getMessage(), null,
					new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

	}
}