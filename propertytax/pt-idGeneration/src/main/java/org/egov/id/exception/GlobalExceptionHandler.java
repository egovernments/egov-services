package org.egov.id.exception;
import java.util.HashMap;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = {Exception.class })
	public ErrorRes unknownException(Exception ex, WebRequest req) {
		if(ex instanceof AttributeNotFoundException){
			Error error=new Error(HttpStatus.BAD_REQUEST.toString(),((AttributeNotFoundException) ex).getCustomMsg(),null,new HashMap<String,String>());
			ResponseInfo responseInfo=new ResponseInfo();
			responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
			return new ErrorRes(responseInfo,error);
		} else {
			Error error=new Error(HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),null,new HashMap<String,String>());
			ResponseInfo responseInfo=new ResponseInfo();
			responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
			return new ErrorRes(responseInfo,error);
		}
	}

}
