package org.egov.tracer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.egov.tracer.kafka.ErrorQueueProducer;
import org.egov.tracer.model.CustomBindingResultExceprion;
import org.egov.tracer.model.CustomException;
import org.egov.tracer.model.Error;
import org.egov.tracer.model.ErrorQueueContract;
import org.egov.tracer.model.ErrorRes;
import org.egov.tracer.model.ServiceCallException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

//import org.springframework.validation.ObjectError;

@ControllerAdvice
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionAdvise {

	@Autowired
	private ErrorQueueProducer  errorQueueProducer;
	
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public ResponseEntity<?> exceptionHandler(HttpServletRequest request ,Exception ex) {
	
		log.info("ExceptionAdvise exceptio  webRequest:");
		System.out.println(ex instanceof BindException);
		ex.printStackTrace();
		String body = readRequestBody(request);
		ErrorRes errorRes = new ErrorRes();
		List<Error> errors = new ArrayList<>();
		if (ex instanceof MethodArgumentNotValidException) {
			log.info("MethodArgumentNotValidException ");
			MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) ex;
			errorRes.setErrors(getBindingErrors(argumentNotValidException.getBindingResult(), errors));

		} else if (ex instanceof CustomBindingResultExceprion) {
			log.info("CustomBindingResultExceprion block");
			CustomBindingResultExceprion customBindingResultExceprion = (CustomBindingResultExceprion) ex;
			errorRes.setErrors(getBindingErrors(customBindingResultExceprion.getBindingResult(), errors));
		} else if (ex instanceof CustomException) {
			log.info("CustomException block");
			CustomException customException = (CustomException) ex;
			populateCustomErrros(customException, errors);
			errorRes.setErrors(errors);
		} else if (ex instanceof ServiceCallException) {
			log.info("ServiceCallException block");
			ServiceCallException serviceCallException = (ServiceCallException) ex;
			sendErrorMessage(body, ex, request.getRequestURL().toString(),errorRes);
			DocumentContext documentContext = JsonPath.parse(serviceCallException.getError());
			log.info("exceptionHandler:"+documentContext);
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
			log.info("bindException block:"+bindException);
			log.info("bindException.getBindingResult() block:"+bindException.getBindingResult());

			errorRes.setErrors(getBindingErrors(bindException.getBindingResult(), errors));

			//errorRes.setErrors(errors);
		}
		
		sendErrorMessage(body, ex, request.getRequestURL().toString(),errorRes);
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
	
	private String readRequestBody(HttpServletRequest httpServletRequest) {
		log.info("ExceptionAdvise readRequestBody");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = "";
		StringBuilder stringBuilder = new StringBuilder();
		try {
			while ( (line=reader.readLine()) != null ) {
			    stringBuilder.append(line).append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String body = stringBuilder.toString();
		System.out.println("readRequestBody body:"+body);
		return body;
	}
	
	public void sendErrorMessage(String body, Exception ex, String source, ErrorRes errorRes) {
		DocumentContext documentContext = JsonPath.parse(body);
		log.info("sendErrorMessage documentContext:"+documentContext.json());
		StackTraceElement elements[] = ex.getStackTrace();
		
		ErrorQueueContract errorQueueContract = ErrorQueueContract.builder().body(documentContext.json()).source(source).
				ts(new Date().getTime()).errorRes(errorRes).exception(Arrays.asList(elements)).message(ex.getMessage()).build();
		
		log.info("sendErrorMessage errorQueueContract:"+errorQueueContract);
		errorQueueProducer.sendMessage(errorQueueContract);
	}
	
}