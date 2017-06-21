package org.egov.demand.web.contract.factory;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ResponseInfoFactory {

	public ResponseInfo getResponseInfo(RequestInfo requestInfo, HttpStatus status) {

		ResponseInfo responseInfo = new ResponseInfo();
		if (requestInfo != null) {
			responseInfo.setApiId(requestInfo.getApiId());
			responseInfo.setMsgId(requestInfo.getMsgId());
			// responseInfo.setResMsgId(requestInfo.get);
			responseInfo.setStatus(status.toString());
			responseInfo.setVer(requestInfo.getVer());
			responseInfo.setTs(requestInfo.getTs());
		}
		return responseInfo;
	}
}
