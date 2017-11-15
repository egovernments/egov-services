package org.egov.inv.domain.exception;

import org.springframework.validation.BindingResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomBindException extends RuntimeException {

	private static final long serialVersionUID = 8861914629969408745L;

	private String message;

	public CustomBindException(String message) {
	this.message = message;

	}

}
