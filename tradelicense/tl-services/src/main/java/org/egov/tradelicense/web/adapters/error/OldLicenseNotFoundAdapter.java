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

public class OldLicenseNotFoundAdapter {

	private static final int HTTP_CLIENT_ERROR_CODE = 400;
	private static final String LICENSE_NOT_FOUND_EXCEPTION_MESSAGE = "tl.error.oldlicensenumber.notfound";
	private static final String LICENSE_NOT_FOUND_EXCEPTION_FIELD = "oldLicenseNumber";
	private static final String LICENSE_NOT_FOUND_EXCEPTION_FIELD_CODE = "tl.error.oldLicensenumber.notfound";

	public ErrorResponse getErrorResponse(String customMsg, RequestInfo requestInfo) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setMsgId(requestInfo.getMsgId());
		responseInfo.setTs(new Date().toString());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new ErrorResponse(responseInfo, getError(customMsg));
	}

	private Error getError(String customMsg) {
		return Error.builder().code(HTTP_CLIENT_ERROR_CODE).message(LICENSE_NOT_FOUND_EXCEPTION_MESSAGE)
				.description(customMsg).build();
	}

}