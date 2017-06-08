package org.egov.id.exception;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.egov.models.AttributeNotFoundException;
import org.egov.models.Error;
import org.egov.models.ErrorRes;
import org.egov.models.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@Autowired
	Environment environment;
	@ExceptionHandler(value = {Exception.class })
	public ErrorRes unknownException(Exception ex, WebRequest req) {
		if(ex instanceof AttributeNotFoundException){
			Error error=new Error(HttpStatus.BAD_REQUEST.toString(),((AttributeNotFoundException) ex).getCustomMsg(),null,new HashMap<String,String>());
			ResponseInfo responseInfo=new ResponseInfo();
			responseInfo.setStatus(environment.getProperty("failed"));
			List<Error> errorList=new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo,errorList);
		} else {
			Error error=new Error(HttpStatus.BAD_REQUEST.toString(),ex.getMessage(),null,new HashMap<String,String>());
			ResponseInfo responseInfo=new ResponseInfo();
			responseInfo.setStatus(environment.getProperty("failed"));
			List<Error> errorList=new ArrayList<Error>();
			errorList.add(error);
			return new ErrorRes(responseInfo,errorList);
		}
	}

}
