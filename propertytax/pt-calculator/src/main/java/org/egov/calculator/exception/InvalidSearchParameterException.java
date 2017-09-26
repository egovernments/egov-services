package org.egov.calculator.exception;

import org.egov.models.RequestInfo;
import org.springframework.validation.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Yosadhara
 *
 */
@AllArgsConstructor
@Getter
@Setter
public class InvalidSearchParameterException extends Exception {
	
    private static final long serialVersionUID = 1L;
	
	Errors bindingResult;
	
	RequestInfo requestInfo;
}
