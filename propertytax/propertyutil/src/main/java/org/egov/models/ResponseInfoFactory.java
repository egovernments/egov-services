package org.egov.models;

import java.util.Date;

import org.springframework.stereotype.Service;

/**
 * <h1>ResponseInfoFactory</h1>
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
			ts =String.valueOf(new Date().getTime());
		String resMsgId = "uief87324"; // FIXME : Hard-coded
		String msgId = requestInfo.getMsgId();
		String responseStatus = success ? "SUCCESSFUL" :"";
			return new ResponseInfo(apiId, ver, ts, resMsgId, msgId,responseStatus);
	}
}
