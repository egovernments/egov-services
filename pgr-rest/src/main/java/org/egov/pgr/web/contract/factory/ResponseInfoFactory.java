package org.egov.pgr.web.contract.factory;

import org.egov.pgr.web.contract.ResponseInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

public class ResponseInfoFactory {
    public static ResponseInfo createResponseInfoFromRequestHeaders(@RequestHeader HttpHeaders headers, Boolean success) {
        String api_id = headers.getFirst("api_id");
        String ver = headers.getFirst("ver");
        String ts = headers.getFirst("ts");
        String msg_id = headers.getFirst("msg_id");
        String responseStatus = success ? "successful" : "failed";
        return new ResponseInfo(api_id, ver, ts, "uief87324", msg_id, responseStatus);
    }
}
