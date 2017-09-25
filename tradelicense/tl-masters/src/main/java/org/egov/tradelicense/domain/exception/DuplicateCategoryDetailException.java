package org.egov.tradelicense.domain.exception;

import org.egov.tl.commons.web.contract.RequestInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Duplicate Tenant & code combination custom Exception class
 * 
 * @author Pavan Kumar Kamma
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateCategoryDetailException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String customMsg;

	private RequestInfo requestInfo;

}