package org.egov.calculator.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * 
 * @author Yosadhara 
 * Duplicate Tenant ID & FeeFactor combination custom Exception class
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateIdException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private String customMessage;
	private String description;
	private RequestInfo requestInfo;
}
