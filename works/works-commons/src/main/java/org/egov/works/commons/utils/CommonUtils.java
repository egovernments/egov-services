package org.egov.works.commons.utils;

import org.egov.works.commons.web.contract.RequestInfo;
import org.egov.works.commons.web.contract.ResponseInfo;
import org.springframework.stereotype.Service;

/**
 * Created by ramki on 1/11/17.
 */

@Service
public class CommonUtils {
    public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean isSuccess) {
        return new ResponseInfo(requestInfo.getApiId(), requestInfo.getVer(), requestInfo.getTs(), "uief87324", requestInfo.getMsgId(), isSuccess ? ResponseInfo.StatusEnum.SUCCESSFUL : ResponseInfo.StatusEnum.FAILED);
    }
}
