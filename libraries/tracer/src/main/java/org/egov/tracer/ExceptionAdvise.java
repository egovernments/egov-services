package org.egov.tracer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.egov.tracer.kafka.ErrorQueueProducer;
import org.egov.tracer.model.CustomBindingResultExceprion;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorQueueContract;
import org.egov.tracer.model.ErrorRes;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;


@ControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionAdvise {

	@Autowired
	private ErrorQueueProducer  errorQueueProducer;

	@Value("${tracer.errors.sendToKafka:false}")
    boolean sendErrorsToKafka;

    @Value("${tracer.errors.provideExceptionInDetails:false}")
    boolean provideExceptionInDetails;


    @ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseEntity<?> exceptionHandler(HttpServletRequest request ,Exception ex) {

		log.info("ExceptionAdvise exception  webRequest:");
		String contentType = request.getContentType();
		boolean isJsonContentType = (contentType != null && contentType.toLowerCase().contains("application/json"));
		
		String body = "";
		try {
			ServletInputStream stream = request.getInputStream();
			body = IOUtils.toString(stream, "UTF-8");
			
		} catch (IOException e) {
			log.error("Exception: "+e);
		}
		ErrorRes errorRes = new ErrorRes();
		List<Error> errors = new ArrayList<>();

		try {
			if (ex instanceof HttpMediaTypeNotSupportedException) {
				errorRes.setErrors(new ArrayList<>(Arrays.asList(new Error("UnsupportedMediaType", "An unssuporrted media Type was used - " + request.getContentType(), null, null))));
			} else if (ex instanceof ResourceAccessException) {
				Error err = new Error();
				err.setCode("ResourceAccessError");
				err.setMessage("An error occured while accessing a underlying resource");
				errors.add(err);
				errorRes.setErrors(errors);
			} else if (ex instanceof HttpMessageNotReadableException) {
				Error err = new Error();
				String message = ex.getMessage();
	
				if (ex.getCause() instanceof JsonMappingException) {
					
					Pattern pattern = Pattern.compile("(.+)Can not deserialize instance of ([a-z]+\\.){1,}(?<objecttype>[^ ]+).*\\[\"(?<objectname>[^\"]+)\"\\].*",
							Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
					Matcher match = pattern.matcher(message);
					boolean matched = match.find();
					if (matched) {
						err.setMessage("Failed to parse field - " + match.group("objectname") + ". Expected type is " + match.group("objecttype"));
					} else {
						err.setMessage("Failed to deseriliaze certain JSON fields");
					}
					
					err.setCode("JsonMappingException");
					
				} else if (ex.getCause() instanceof JsonParseException) {
					err.setCode("JsonParseException");
					message = ex.getCause().getMessage().replaceAll("Source: [^;]+; ", "").replaceAll(" \\(code \\d+\\)", "").replaceAll("\\n", "");
					err.setMessage(message);
				} else {
					try {
						err.setMessage("JSON body has errors or is missing");
						JsonPath.parse(request).json();
					} catch (Exception jsonParseException) {
						ex.printStackTrace();
					}
 					err.setCode("MissingJsonException");
				}
				errors.add(err);
				errorRes.setErrors(errors);
			} else if (ex instanceof MethodArgumentNotValidException) {
				log.error("MethodArgumentNotValidException ");
				MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) ex;
				errorRes.setErrors(getBindingErrors(argumentNotValidException.getBindingResult(), errors));
	
			} else if (ex instanceof CustomBindingResultExceprion) {
				log.error("CustomBindingResultExceprion block");
				CustomBindingResultExceprion customBindingResultExceprion = (CustomBindingResultExceprion) ex;
				errorRes.setErrors(getBindingErrors(customBindingResultExceprion.getBindingResult(), errors));
			} else if (ex instanceof CustomException) {
				log.error("CustomException block");
				CustomException customException = (CustomException) ex;
				populateCustomErrros(customException, errors);
				errorRes.setErrors(errors);
			} else if (ex instanceof ServiceCallException) {
				log.error("ServiceCallException block");
				ServiceCallException serviceCallException = (ServiceCallException) ex;
				sendErrorMessage(body, ex, request.getRequestURL().toString(),errorRes, isJsonContentType);
				DocumentContext documentContext = JsonPath.parse(serviceCallException.getError());
				log.error("exceptionHandler:"+documentContext);
				LinkedHashMap<Object, Object> linkedHashMap = documentContext.json();
				return new ResponseEntity<>(linkedHashMap, HttpStatus.BAD_REQUEST);
				
			} else if (ex instanceof MissingServletRequestParameterException) {
				MissingServletRequestParameterException exception = (MissingServletRequestParameterException) ex;
				Error error = new Error();
				error.setCode("");
				error.setMessage(exception.getMessage());
				//error.setDescription(exception.getCause().toString());
				List<String> params = new ArrayList<>();
				params.add(exception.getParameterName());
				error.setParams(params);
				errors.add(error);
				errorRes.setErrors(errors);
			} else if (ex instanceof BindException) {
				BindException bindException = (BindException) ex;
				bindException.getBindingResult();
				log.error("bindException block:"+bindException);
				log.error("bindException.getBindingResult() block:"+bindException.getBindingResult());
	
				errorRes.setErrors(getBindingErrors(bindException.getBindingResult(), errors));
	
				//errorRes.setErrors(errors);
			}

            String exceptionName = ex.getClass().getSimpleName();
			String exceptionMessage = ex.getMessage();

			if (errorRes.getErrors() == null || errorRes.getErrors().size() == 0) {
				errorRes.setErrors(new ArrayList<>(Arrays.asList(new Error(exceptionName, "An unhandled exception occurred on the server", exceptionMessage, null))));
			} else if (provideExceptionInDetails && errorRes.getErrors() != null && errorRes.getErrors().size() > 0) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);
                errorRes.getErrors().get(0).setDescription(sw.toString());
            }

			sendErrorMessage(body, ex, request.getRequestURL().toString(),errorRes, isJsonContentType);
		} catch (Exception tracerException) {
			tracerException.printStackTrace();
			errorRes.setErrors(new ArrayList<>(Arrays.asList(new Error("TracerException", "An unhandled exception occurred in tracer handler", null, null))));
		}
		return new ResponseEntity<>(errorRes, HttpStatus.BAD_REQUEST);
	}

	private List<Error> getBindingErrors(BindingResult bindingResult, List<Error> errors) {

		List<ObjectError> objectErrors = bindingResult.getAllErrors();

		for (ObjectError objectError : objectErrors) {
			Error error = new Error();
			String[] codes = objectError.getCodes();
			error.setCode(codes[0]);
			error.setMessage(objectError.getDefaultMessage());
			errors.add(error);
		}

		return errors;
	}

	private void populateCustomErrros(CustomException customException, List<Error> errors) {
		Map<String, String> map = customException.getErrors();
		if (map != null && !map.isEmpty()) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				Error error = new Error();
				error.setCode(entry.getKey());
				error.setMessage(entry.getValue());
				errors.add(error);
			}
		} else {
			Error error = new Error();
			error.setCode(customException.getCode());
			error.setMessage(customException.getMessage());
			errors.add(error);
		}

	}
		
	public void sendErrorMessage(String body, Exception ex, String source, ErrorRes errorRes, boolean isJsonContentType) {
		DocumentContext documentContext;
		String bodyJSON = body;

		if (isJsonContentType) {
            try {
                documentContext = JsonPath.parse(body);
                bodyJSON = documentContext.json().toString();
                log.error("sendErrorMessage documentContext: " + bodyJSON);
            } catch (Exception exception) {
                bodyJSON = body;
                log.error("sendErrorMessage documentContext: Failed to parse JSON");
            }
        }

		if (sendErrorsToKafka) {
            StackTraceElement elements[] = ex.getStackTrace();

            ErrorQueueContract errorQueueContract = ErrorQueueContract.builder().body(bodyJSON).source(source).
                ts(new Date().getTime()).errorRes(errorRes).exception(Arrays.asList(elements)).message(ex.getMessage()).build();

            log.error("sendErrorMessage errorQueueContract:" + errorQueueContract);
            errorQueueProducer.sendMessage(errorQueueContract);
        }
	}
	
}