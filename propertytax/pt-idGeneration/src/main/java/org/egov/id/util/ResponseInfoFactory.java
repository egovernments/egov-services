package org.egov.id.util;

import java.util.Date;

import org.egov.models.RequestInfo;
import org.egov.models.ResponseInfo;
import org.springframework.stereotype.Component;

@Component
public class ResponseInfoFactory {
	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {

		String apiId = requestInfo.getApiId();
		String ver = requestInfo.getVer();
		long ts = new Date().getTime();
				String resMsgId = "uief87324"; // FIXME : Hard-coded
		String msgId = requestInfo.getMsgId();
		String responseStatus = success ? "SUCCESSFUL" :"FAILED";
		return new ResponseInfo(apiId, ver, ts, resMsgId, msgId, responseStatus);
	}
}
