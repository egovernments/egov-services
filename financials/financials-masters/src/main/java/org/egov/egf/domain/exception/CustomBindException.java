package org.egov.egf.domain.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CustomBindException extends RuntimeException {
	private BindingResult errors;

	public CustomBindException(BindingResult errors) {
		this.errors=errors;
		 
	}
	

}
