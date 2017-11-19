package org.egov.works.qualitycontrol.utils;

import org.egov.works.qualitycontrol.web.contract.AuditDetails;
import org.egov.works.qualitycontrol.web.contract.RequestInfo;
import org.egov.works.qualitycontrol.web.contract.ResponseInfo;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QualityControlUtils {

    public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean isSuccess) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(requestInfo.getApiId());
        responseInfo.setVer(requestInfo.getVer());
        responseInfo.setTs(requestInfo.getTs()); //Fix me : set response time
        responseInfo.setResMsgId("uief87324"); //Fix me : do we need to generate a new response message id?
        responseInfo.setMsgId(requestInfo.getMsgId());
        responseInfo.setStatus(isSuccess ? ResponseInfo.StatusEnum.SUCCESSFUL : ResponseInfo.StatusEnum.FAILED);
        return responseInfo;
    }

    public AuditDetails getAuditDetails(final RequestInfo requestInfo, final Boolean isUpdate) {
        AuditDetails auditDetails = new AuditDetails();
        if (isUpdate) {
            auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
            auditDetails.setLastModifiedTime(new Date().getTime());
        } else {
            auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
            auditDetails.setCreatedTime(new Date().getTime());
            auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
            auditDetails.setLastModifiedTime(new Date().getTime());
        }

        return auditDetails;
    }
}
