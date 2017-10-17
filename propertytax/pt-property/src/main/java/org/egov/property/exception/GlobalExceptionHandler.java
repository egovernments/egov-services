package org.egov.property.exception;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.common.contract.response.ErrorResponse;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.InvalidIDFormatException;
import org.egov.models.ResponseInfo;
import org.egov.property.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description : Global exception handler for property module
 * 
 * @author Narendra
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	public static final String MESSAGE = "One of Application number or Upic number is mandatory";
	public static final String DESCRIPTION = "Mandatory fields value missing";
	@Autowired
	private PropertiesManager propertiesManager;

	@Autowired
	private ObjectMapper objectMapper;

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

	@ExceptionHandler(DemandUpdateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse processDemandUpdateException(HttpStatusCodeException ex) {
		if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST))
			try {
				return objectMapper.readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
			} catch (IOException e) {

			}

		return ErrorResponse.builder().build();
	}

	@ExceptionHandler(NoticeMandatoryFieldException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorRes noticeMandatoryException(NoticeMandatoryFieldException exception) {
		Error error = new Error(HttpStatus.BAD_REQUEST.toString(), MESSAGE, DESCRIPTION, null);

		return new ErrorRes(null, Arrays.asList(error));
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
		} else if (ex instanceof InvalidPropertyBoundaryException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					((InvalidPropertyBoundaryException) ex).getCustomMsg(),
					((InvalidPropertyBoundaryException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidPropertyBoundaryException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidPropertyBoundaryException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidPropertyBoundaryException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof ValidationUrlNotFoundException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					((ValidationUrlNotFoundException) ex).getCustomMsg(),
					((ValidationUrlNotFoundException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((ValidationUrlNotFoundException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((ValidationUrlNotFoundException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((ValidationUrlNotFoundException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidIDFormatException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ((InvalidIDFormatException) ex).getCustomMsg(),
					null, new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidIDFormatException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidIDFormatException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidIDFormatException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof IdGenerationException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ((IdGenerationException) ex).getCustomMsg(),
					((IdGenerationException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((IdGenerationException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((IdGenerationException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((IdGenerationException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

		else if (ex instanceof InvalidUpdatePropertyException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					((InvalidUpdatePropertyException) ex).getCustomMsg(), null, new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidUpdatePropertyException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidUpdatePropertyException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidUpdatePropertyException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

		else if (ex instanceof PropertySearchException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidInput(), null, null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((PropertySearchException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((PropertySearchException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((PropertySearchException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);

		} else if (ex instanceof DuplicateIdException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getDuplicateCode(), null,
					null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((DuplicateIdException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((DuplicateIdException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((DuplicateIdException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof PropertyUnderWorkflowException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					((PropertyUnderWorkflowException) ex).getCustomMsg(), ex.getMessage(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((PropertyUnderWorkflowException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((PropertyUnderWorkflowException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((PropertyUnderWorkflowException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidCodeException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ((InvalidCodeException) ex).getCustomMsg(),
					ex.getMessage(), null);
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidCodeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidCodeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidCodeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidFloorException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidPropertyFloor(),
					((InvalidFloorException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidFloorException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidFloorException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidFloorException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidVacantLandException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidPropertyVacantland(),
					((InvalidVacantLandException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidVacantLandException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidVacantLandException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidVacantLandException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidGuidanceValueException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidGuidanceVal(),
					((InvalidGuidanceValueException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidGuidanceValueException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidGuidanceValueException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidGuidanceValueException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidFactorValueException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidFactorValue(),
					((InvalidFactorValueException) ex).getMsgDetails(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidFactorValueException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidFactorValueException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidFactorValueException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof PropertyTaxPendingException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidTaxMessage(),
					((PropertyTaxPendingException) ex).getCustomMsg(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((PropertyTaxPendingException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((PropertyTaxPendingException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((PropertyTaxPendingException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidVacancyRemissionPeriod) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), propertiesManager.getInvalidDateValidation(),
					((InvalidVacancyRemissionPeriod) ex).getCustomMsg(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidVacancyRemissionPeriod) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidVacancyRemissionPeriod) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidVacancyRemissionPeriod) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
			return new ErrorRes(responseInfo, errorList);
		} else if (ex instanceof InvalidPropertyTypeException) {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(),
					propertiesManager.getInvalidPropertyTypeException(),
					((InvalidPropertyTypeException) ex).getCustomMsg(), new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setApiId(((InvalidPropertyTypeException) ex).getRequestInfo().getApiId());
			responseInfo.setVer(((InvalidPropertyTypeException) ex).getRequestInfo().getVer());
			responseInfo.setMsgId(((InvalidPropertyTypeException) ex).getRequestInfo().getMsgId());
			responseInfo.setTs(new Date().getTime());
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			responseInfo.setStatus(propertiesManager.getFailed());
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
				errors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			error.setFileds(errors);
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

		else {
			Error error = new Error(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(), null,
					new HashMap<String, String>());
			ResponseInfo responseInfo = new ResponseInfo();
			responseInfo.setStatus(propertiesManager.getFailed());
			List<Error> errorList = new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo, errorList);
		}

	}

}
