package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <h1>AttributeNotFoundException</h1>
 * 
 * @author S Anilkumar
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;

}
