package org.egov.user.utils;

import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.user.domain.v11.model.User;
import org.egov.user.web.contract.factory.ResponseInfoFactory;
import org.egov.user.web.v11.contract.UserResponse;
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
}
