package org.egov.calculator.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Description : Invalid input custom exception class
 * 
 * @author Pavan Kumar Kamma
 *
 */

@AllArgsConstructor
@Getter
@Setter
public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private RequestInfo requestInfo;
}
