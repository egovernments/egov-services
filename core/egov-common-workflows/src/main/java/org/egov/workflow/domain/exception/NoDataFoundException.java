package org.egov.workflow.domain.exception;

public class NoDataFoundException extends RuntimeException {
	public static final String code="002";
	String message;
	
	/**
	 * @param message
	 */
	public NoDataFoundException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
