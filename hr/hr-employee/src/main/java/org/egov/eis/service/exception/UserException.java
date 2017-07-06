package org.egov.eis.service.exception;

import org.egov.common.contract.request.RequestInfo;
import org.egov.eis.web.errorhandler.UserErrorResponse;

import lombok.Getter;

public class UserException extends RuntimeException {

	private static final long serialVersionUID = -1068942413966014777L;

	@Getter
	private UserErrorResponse userErrorResponse;

	@Getter
	private RequestInfo requestInfo;

	public UserException(UserErrorResponse userErrorResponse, RequestInfo requestInfo) {
		super(userErrorResponse != null ? userErrorResponse.getError().getMessage()
				: "Unknown Error Occurred While Calling User Service");
		this.userErrorResponse = userErrorResponse;
		this.requestInfo = requestInfo;
	}

}
