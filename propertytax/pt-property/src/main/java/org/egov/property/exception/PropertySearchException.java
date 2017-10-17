package org.egov.property.exception;

import org.egov.models.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Prasad
 * 
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertySearchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;

}
