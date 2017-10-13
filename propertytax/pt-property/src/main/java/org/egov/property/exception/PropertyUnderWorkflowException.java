package org.egov.property.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyUnderWorkflowException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;

}
