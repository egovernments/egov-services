package org.egov.eis.service.exception;

import lombok.Getter;

public class UserUpdateException extends RuntimeException {

	private static final long serialVersionUID = -1068942413966014744L;
		@Getter
	    private Long userId;

	    public UserUpdateException(Long userId) {
	       super("invalid employeeId ");
	    	this.userId = userId;
	    }

}
