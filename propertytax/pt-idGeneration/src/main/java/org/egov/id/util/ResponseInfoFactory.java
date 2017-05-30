package org.egov.id.util;

import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.springframework.stereotype.Component;

@Component
public class ResponseInfoFactory {
	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {

		String apiId = requestInfo.getApiId();
		String ver = requestInfo.getVer();
		String ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs().toString();
		String resMsgId = "uief87324"; // FIXME : Hard-coded
		String msgId = requestInfo.getMsgId();
		String responseStatus = success ? "201" : "400";
		return new ResponseInfo(apiId, ver, ts, resMsgId, msgId, responseStatus);
	}
}
