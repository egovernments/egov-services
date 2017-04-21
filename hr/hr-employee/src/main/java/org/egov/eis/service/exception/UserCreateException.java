package org.egov.eis.service.exception;

import lombok.Getter;

public class UserCreateException extends RuntimeException {

	private static final long serialVersionUID = -1068942413966014777L;
		@Getter
	    private Long userId;

	    public UserCreateException(Long userId) {
	       super("invalid employeeId ");
	    	this.userId = userId;
	    }

}
