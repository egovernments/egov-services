package org.egov.web.wrapperfactory;

import java.util.Date;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.stereotype.Component;

@Component
public class ResponseInfoFactory {

    public ResponseInfo createResponseInfoFromRequestHeaders(final RequestInfo requestInfo) {
        final ResponseInfo responseInfo = new ResponseInfo();
        if (requestInfo != null) {
            responseInfo.setApiId(requestInfo.getApiId());
            responseInfo.setMsgId(requestInfo.getMsgId());
            // responseInfo.setResMsgId("");
            // responseInfo.setStatus(status);
            responseInfo.setTs(new Date().getTime());
            responseInfo.setVer("v1");
        }
        return responseInfo;
    }
}