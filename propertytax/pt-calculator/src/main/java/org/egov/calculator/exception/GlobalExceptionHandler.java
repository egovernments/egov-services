package org.egov.calculator.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.calculator.config.PropertiesManager;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Description : Global exception handler for propertyTax calculator module
 * 
 * @author Pavan Kumar Kamma
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	PropertiesManager propertiesManager;

	/**
	 * Description : Null pointer exception handler
	 * 
	 * @param ex
	 * @return
	 */

	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes nullPointerException(NullPointerException ex) {
		ex.printStackTrace();
		Map<String, String> errors = new HashMap<String, String>();
		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(), null, errors);
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(error);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(propertiesManager.getFailed());
		return new ErrorRes(responseInfo, errorList);
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

		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(), null, errors);
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(error);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(propertiesManager.getFailed());
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
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(), null, null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidPenaltyDataException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					((InvalidPenaltyDataException) ex).getCustomMessage(),
					((InvalidPenaltyDataException) ex).getDescription(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidPenaltyDataException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidPenaltyDataException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidPenaltyDataException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof DuplicateIdException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ((DuplicateIdException) ex).getCustomMessage(),
					((DuplicateIdException) ex).getDescription(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateIdException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateIdException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateIdException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidSearchParameterException) {

			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidSearchParameterException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidSearchParameterException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidSearchParameterException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());

			Error error = new Error();
			error.setCode(HttpStatus.BAD_REQUEST.toString());
			error.setMessage(propertiesManager.getInvalidSearchParameterException());
			error.setDescription(propertiesManager.getInvalidSearchParameterException());
			Map<String, String> errors = new HashMap<String, String>();

			for (FieldError fieldError : ((InvalidSearchParameterException) ex).bindingResult.getFieldErrors()) {
				// ErrorField errorField = new ErrorField(fieldError.getCode(),
				// fieldError.getDefaultMessage(),fieldError.getField());
				// errorFields.add(errorField);
				errors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			error.setFileds(errors);
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), null,
					new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidInputException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidInputException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidInputException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}
	}

	@ExceptionHandler(InvalidTaxCalculationDataException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes processValidationError(InvalidTaxCalculationDataException exception) {

		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidTaxcalculation(),
				exception.getMessage(), null);
		List<Error> errorList = new ArrayList<Error>();
		errorList.add(error);
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(exception.getRequestInfo().getApiId());
		responseInfo.setVer(exception.getRequestInfo().getVer());
		responseInfo.setMsgId(exception.getRequestInfo().getMsgId());
		responseInfo.setTs(new Date().getTime());

		responseInfo.setStatus(propertiesManager.getFailed());
		return new ErrorRes(responseInfo, errorList);
	}

}