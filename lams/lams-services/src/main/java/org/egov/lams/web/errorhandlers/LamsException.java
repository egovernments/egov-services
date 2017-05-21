package org.egov.lams.web.errorhandlers;

public class LamsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	public LamsException() {
		super();
	}

	public LamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LamsException(String message, Throwable cause) {
		super(message, cause);
	}

	public LamsException(String message) {
		super(message);
	}

	public LamsException(Throwable cause) {
		super(cause);
	}
	

}
