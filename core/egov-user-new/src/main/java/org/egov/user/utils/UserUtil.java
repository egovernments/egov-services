package org.egov.user.utils;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.model.User;
import org.egov.user.web.contract.UserResponse;
import org.egov.user.web.contract.factory.ResponseInfoFactory;
import org.egov.user.web.errorhandlers.Error;
import org.egov.user.web.errorhandlers.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

	@Autowired
	private ResponseInfoFactory responseInfoFactory;
	
	public ResponseEntity<?> createSuccessResponse(final RequestInfo requestInfo, List<User> users) {
		final ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);
		responseInfo.setStatus(HttpStatus.OK.toString());
		UserResponse userResponse = new UserResponse(responseInfo, users);
		return new ResponseEntity<>(userResponse, HttpStatus.OK);
	}
	
	public ResponseEntity<?> createFailureResponse(Exception e) {
		ErrorResponse errorResponse = new ErrorResponse();
		Error error = Error.builder().code(HttpStatus.BAD_REQUEST.value()).message(e.getMessage()).build();
		errorResponse.setError(error);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
