package org.egov.tradelicense.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * fromRange and toRange validation custom Exception class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvalidRangeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private RequestInfo requestInfo;
}