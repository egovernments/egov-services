package org.egov.works.workorder.utils;

import java.util.Date;

import org.egov.works.workorder.web.contract.AuditDetails;
import org.egov.works.workorder.web.contract.RequestInfo;
import org.egov.works.workorder.web.contract.ResponseInfo;
import org.springframework.stereotype.Service;

@Service
public class WorkOrderUtils {

	public ResponseInfo createResponseInfoFromRequestInfo(RequestInfo requestInfo, Boolean isSuccess) {
		ResponseInfo responseInfo = new ResponseInfo();
		responseInfo.setApiId(requestInfo.getApiId());
		responseInfo.setVer(requestInfo.getVer());
		responseInfo.setTs(requestInfo.getTs()); // Fix me : set response time
		responseInfo.setResMsgId("uief87324"); // Fix me : do we need to
												// generate a new response
												// message id?
		responseInfo.setMsgId(requestInfo.getMsgId());
		responseInfo.setStatus(isSuccess ? ResponseInfo.StatusEnum.SUCCESSFUL : ResponseInfo.StatusEnum.FAILED);
		return responseInfo;
	}

	public AuditDetails setAuditDetails(final RequestInfo requestInfo, final Boolean isUpdate) {
		AuditDetails auditDetails = new AuditDetails();
		if (!isUpdate) {
			auditDetails.setCreatedBy(requestInfo.getUserInfo().getUserName());
			auditDetails.setCreatedTime(new Date().getTime());
		}
		auditDetails.setLastModifiedBy(requestInfo.getUserInfo().getUserName());
		auditDetails.setLastModifiedTime(new Date().getTime());

		return auditDetails;
	}

}
