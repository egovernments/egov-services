package org.egov.user.utils;

import java.util.Date;

import org.egov.common.contract.request.AuditDetails;

public class AuditDetailsPopulator {

	public static AuditDetails getAuditDetail(String userId, boolean isUpdateRequest) {
		Long timeStamp = new Date().getTime();
		if(!isUpdateRequest)
			return AuditDetails.builder().createdBy(userId).createdTime(timeStamp).lastModifiedBy(userId).lastModifiedTime(timeStamp).build();
		else
			return AuditDetails.builder().lastModifiedBy(userId).lastModifiedTime(timeStamp).build();
	}
	
	
}
