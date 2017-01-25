package org.egov.user.controller;

import java.util.Date;

import org.egov.user.model.Error;
import org.egov.user.model.ErrorRes;
import org.egov.user.model.RequestInfo;
import org.egov.user.model.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

	@Autowired
	private TokenStore tokenStore;

	@RequestMapping(value = "/rest/user/_logout", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public ResponseInfo logout(@RequestBody RequestInfo requestInfo) throws Exception {

		String authHeader = requestInfo.getAuthToken();
		if (authHeader != null) {
			String tokenValue = authHeader.replace("Bearer", "").trim();
			OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
			tokenStore.removeAccessToken(accessToken);
		}

		ResponseInfo responseInfo = new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(),
				new Date().toString(), requestInfo.getRequesterId(), requestInfo.getMsgId(), "Logout successfully");
		return responseInfo;

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorRes> handleError(Exception ex) {
		ex.printStackTrace();
		ErrorRes response = new ErrorRes();
		ResponseInfo responseInfo = new ResponseInfo("", "", new Date().toString(), "", "", "Logout failed");
		response.setResponseInfo(responseInfo);
		Error error = new Error();
		error.setCode(400);
		error.setDescription("Logout failed");
		response.setError(error);
		return new ResponseEntity<ErrorRes>(response, HttpStatus.BAD_REQUEST);
	}
}