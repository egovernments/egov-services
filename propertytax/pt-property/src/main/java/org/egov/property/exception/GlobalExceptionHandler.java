package org.egov.property.exception;
import org.egov.property.model.Error;
import org.egov.property.model.ErrorResponse;
import org.egov.property.model.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@Autowired
	private Environment environment;
	@ExceptionHandler(value = { Exception.class })
    
    public ErrorResponse unknownException(Exception ex, WebRequest req) {
		if(ex instanceof InvalidInputException){
			Error error=new Error(HttpStatus.BAD_REQUEST.toString(),environment.getProperty("invalidInput"),environment.getProperty("invalidInput"));
			return new ErrorResponse(error,new ResponseInfo());
		}
		else{
			Error error=new Error(HttpStatus.INTERNAL_SERVER_ERROR.toString(),ex.getMessage(),ex.getMessage());
			return new ErrorResponse(error,new ResponseInfo());
		}
        
    }

}
