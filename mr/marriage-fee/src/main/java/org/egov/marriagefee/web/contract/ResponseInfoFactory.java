package org.egov.marriagefee.web.contract;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ResponseInfoFactory {

	public ResponseInfo getResponseInfo(RequestInfo requestInfo, HttpStatus status) {
  
		ResponseInfo responseInfo= new ResponseInfo();
		if (requestInfo != null) {
			responseInfo.setApiId(requestInfo.getApiId());
			responseInfo.setResMsgId(requestInfo.getMsgId());
			// responseInfo.setResMsgId(requestInfo.get);
			responseInfo.setStatus(status.toString());
			responseInfo.setVer(requestInfo.getVer());
			
		
		}
		return responseInfo;
	}
	
	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, boolean success) {

		String apiId = null;
		String ver = null;
		Long ts = null;
		String resMsgId = "uief87324"; // FIXME : Hard-coded
		String msgId = null;
		String tenantId = null;
		if (requestInfo != null) {
			apiId = requestInfo.getApiId();
			ver = requestInfo.getVer();
			ts = requestInfo.getTs();
			msgId = requestInfo.getMsgId();
			
		}
		String status = success ? "successful" : "failed";

		return new ResponseInfo(apiId, ver, ts, resMsgId, msgId, status);
	}

}