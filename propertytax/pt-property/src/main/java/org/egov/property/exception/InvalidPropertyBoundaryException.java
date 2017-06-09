package org.egov.property.exception;

import org.egov.models.RequestInfo;

public class InvalidPropertyBoundaryException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RequestInfo requestInfo;

	public InvalidPropertyBoundaryException(RequestInfo requestInfo2) {
		super();
		this.requestInfo = requestInfo2;
	}

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}
	 
}
