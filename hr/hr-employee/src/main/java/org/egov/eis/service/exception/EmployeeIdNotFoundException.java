package org.egov.eis.service.exception;

import lombok.Getter;

public class EmployeeIdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1068942413966014723L;
		@Getter
	    private Long employeeId;

	    public EmployeeIdNotFoundException(Long employeeId) {
	       super("invalid employeeId ");
	    	this.employeeId = employeeId;
	    }

}
