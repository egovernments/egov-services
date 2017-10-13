package org.egov.property.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Description : Invalid input custom exception class
 * 
 * @author Narendra
 *
 */

@AllArgsConstructor
@Getter
@Setter

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// private String customMsg;

	private RequestInfo requestInfo;

}
