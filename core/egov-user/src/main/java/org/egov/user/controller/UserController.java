package org.egov.user.controller;

import org.egov.user.entity.SecureUser;
import org.egov.user.entity.User;
import org.egov.user.model.Error;
import org.egov.user.model.ErrorResponse;
import org.egov.user.model.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private TokenStore tokenStore;

	@PostMapping(value = "/_details")
	@ResponseBody
	public ResponseEntity<?> getUser(@RequestParam(value = "access_token") String accessToken) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
		if (authentication != null)
			return new ResponseEntity<User>(((SecureUser) authentication.getPrincipal()).getUser(), HttpStatus.OK);
		else {
			ErrorResponse errRes = populateErrors();
			return new ResponseEntity<ErrorResponse>(errRes, HttpStatus.BAD_REQUEST);
		}
	}

	private ErrorResponse populateErrors() {
		ErrorResponse errRes = new ErrorResponse();

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		responseInfo.setApiId("");
		errRes.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(1);
		error.setDescription("Error while fetching user details");
		errRes.setError(error);
		return errRes;
	}
}
