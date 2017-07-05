package org.egov.models;

import org.springframework.stereotype.Service;

/**
 * <h1>ResponseInfoFactory</h1>
 * 
 * @author Narendra
 *
 */
@Service
public class ResponseInfoFactory {
	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean success) {

		String apiId = requestInfo.getApiId();
		String ver = requestInfo.getVer();
		String ts = null;
		if (requestInfo.getTs() != null)
			ts = requestInfo.getTs().toString();
		String resMsgId = "uief87324"; // FIXME : Hard-coded
		String msgId = requestInfo.getMsgId();
		String responseStatus = success ? "SUCCESSFUL" : "FAILED";
		return new ResponseInfo(apiId, ver, Long.valueOf(ts), resMsgId, msgId, responseStatus);
	}
}
