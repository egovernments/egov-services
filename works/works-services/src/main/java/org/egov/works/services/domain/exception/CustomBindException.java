package org.egov.works.services.domain.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Getter
@Setter
public class CustomBindException extends RuntimeException {

	private static final long serialVersionUID = 8861914629969408745L;

	private BindingResult errors;

	public CustomBindException(BindingResult errors) {
		this.errors = errors;

	}

}
