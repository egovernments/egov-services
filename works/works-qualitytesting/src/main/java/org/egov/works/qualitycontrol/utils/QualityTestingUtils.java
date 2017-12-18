package org.egov.works.qualitycontrol.utils;

import org.egov.works.qualitycontrol.web.contract.AuditDetails;
import org.egov.works.qualitycontrol.web.contract.RequestInfo;
import org.egov.works.qualitycontrol.web.contract.ResponseInfo;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class QualityTestingUtils {

    public AuditDetails setAuditDetails(final RequestInfo requestInfo) {
        AuditDetails auditDetails = new AuditDetails();
        auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
        auditDetails.setCreatedTime(new Date().getTime());
        auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
        auditDetails.setLastModifiedTime(new Date().getTime());

        return auditDetails;
    }

    public ResponseInfo getResponseInfo(RequestInfo requestInfo) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setApiId(requestInfo.getApiId());
        responseInfo.setVer(requestInfo.getVer());
        responseInfo.setResMsgId(requestInfo.getMsgId());
        return responseInfo;
    }
}
