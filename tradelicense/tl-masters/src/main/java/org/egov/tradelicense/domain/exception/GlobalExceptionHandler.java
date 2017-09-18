/*package org.egov.tradelicense.domain.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.Error;
import org.egov.tl.commons.web.contract.ErrorRes;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.egov.tl.masters.web.adapters.error.DuplicateDocumentTypeAdapter;
import org.egov.tradelicense.config.PropertiesManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

*//**
 * Description : Global exception handler for property module
 * 
 * @author Pavan Kumar Kamma
 *
 *//*
@Service
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private PropertiesManager propertiesManager;

	*//**
	 * Description : Null pointer exception handler
	 * 
	 * @param ex
	 * @return
	 *//*

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes nullPointerException(NullPointerException ex) {
		ex.printStackTrace();

		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(), null, null);
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(error);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(propertiesManager.getFailedStatus());
		return new ErrorRes(responseInfo, errorList);
	}

	*//**
	 * Description : MethodArgumentNotValidException type exception handler
	 * 
	 * @param ex
	 * @return
	 *//*

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

	*//**
	 * Description : General exception handler method
	 * 
	 * @param ex
	 * @param req
	 * @return
	 *//*

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

		} else if (ex instanceof InvalidRangeException) {

			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidRangeCode(),
					((InvalidRangeException) ex).getCustomMsg(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidRangeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidRangeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidRangeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} 
		else if (ex instanceof DuplicateDocumentTypeException) {
			List<Error> errorList = new ArrayList<Error>();
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateDocumentTypeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateDocumentTypeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateDocumentTypeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailedStatus());
			errorList.add(new DuplicateDocumentTypeAdapter().getErrorResponse(((DuplicateDocumentTypeException) ex).getCustomMsg(), ((DuplicateDocumentTypeException) ex).getRequestInfo()));
			return new ErrorRes(responseInfo, errorList);
		} 
		else if (ex instanceof InvalidCategoryException) {

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
		} 
		else if (ex instanceof InvalidSubCategoryException) {

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
				((InvalidSubCategoryException) ex).getCustomMsg(), null);
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

}*/