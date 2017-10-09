package org.egov.tradelicense.web.adapters.error;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.egov.common.contract.response.Error;
import org.egov.common.contract.response.ErrorField;
import org.egov.common.contract.response.ErrorResponse;
import org.egov.common.contract.response.ResponseInfo;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.springframework.http.HttpStatus;

public class IdNotFoundAdapter {

	private static int httpClientError = 400;
	private static String idNotFoundExceptionFieldCode = "tl.error.id.notfound";

	public ErrorResponse getErrorResponse(String customMsg, String fieldName, RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setMsgId(requestInfo.getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ErrorResponse(responseInfo, getError(customMsg, fieldName));
	}

	private Error getError(String customMsg, String fieldName) {
		return Error.builder().code(httpClientError).message(idNotFoundExceptionFieldCode)
				.description(customMsg).build();
	}

}