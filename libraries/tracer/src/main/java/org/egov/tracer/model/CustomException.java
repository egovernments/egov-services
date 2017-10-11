package org.egov.tracer.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CustomException extends RuntimeException{
	
	private static final long serialVersionUID = 8859144435338793971L;
	
	private String code;
	private String message;
	private Map<String, String> errors;
	
	public CustomException() {
		super();
	}
	public CustomException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public CustomException(Map<String, String> errors) {
		super();
		this.errors = errors;
	}
	
	
}
