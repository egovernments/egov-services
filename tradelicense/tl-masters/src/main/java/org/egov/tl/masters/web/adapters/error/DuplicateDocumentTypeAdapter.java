package org.egov.tl.masters.web.adapters.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egov.tl.commons.web.contract.Error;
import org.egov.tl.commons.web.contract.RequestInfo;
import org.egov.tl.commons.web.contract.ResponseInfo;
import org.springframework.http.HttpStatus;

public class DuplicateDocumentTypeAdapter {

	private static final String HTTP_CLIENT_ERROR_CODE = "400";
	private static final String DUPLICATE_DOCUMENT_TYPE_EXCEPTION_MESSAGE = "tl.error.duplicatedocumenttype.found";
	private static final String DUPLICATE_DOCUMENT_TYPE_EXCEPTION_FIELD = "name";
	private static final String DUPLICATE_TRADE_LICENSE_EXCEPTION_FIELD_CODE = "tl.error.name.alreadyexists";

	public Error getErrorResponse(String customMsg, RequestInfo requestInfo) {

		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setMsgId(requestInfo.getMsgId());
		responseInfo.setTs(new Date().getTime());
		responseInfo.setStatus(HttpStatus.BAD_REQUEST.toString());
		return new Error(HTTP_CLIENT_ERROR_CODE, DUPLICATE_DOCUMENT_TYPE_EXCEPTION_MESSAGE,
				customMsg, null);
	}

	private Error getError(String customMsg) {
		final List<Error> fields = Collections.singletonList(getErrorField(customMsg));
		return new Error(HTTP_CLIENT_ERROR_CODE, DUPLICATE_DOCUMENT_TYPE_EXCEPTION_MESSAGE,
				DUPLICATE_TRADE_LICENSE_EXCEPTION_FIELD_CODE,
				null);
	}

	private List<Error> getErrorFields(String customMsg) {
		List<Error> errorFields = new ArrayList<>();
		errorFields.add(getError(customMsg));
		return errorFields;
	}

	private Error getErrorField(String customMsg) {
		Map<String,String> hm = new HashMap<String,String>();
		hm.put(DUPLICATE_DOCUMENT_TYPE_EXCEPTION_FIELD, DUPLICATE_DOCUMENT_TYPE_EXCEPTION_FIELD);
		return Error.builder().code(DUPLICATE_TRADE_LICENSE_EXCEPTION_FIELD_CODE)
				.fileds(hm).message(customMsg).build();
	}
}