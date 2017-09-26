package org.egov.propertytax.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.ResponseInfo;
import org.egov.propertytax.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Description : Global exception handler for property module
 * 
 * @author Narendra
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	@Autowired
	private PropertiesManager propertiesManager;

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
		} else {
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
