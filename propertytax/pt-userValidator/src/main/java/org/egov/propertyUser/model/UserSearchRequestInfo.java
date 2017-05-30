package org.egov.propertyUser.model;

import org.egov.models.RequestInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchRequestInfo {
	
	private String tenantId;
	
	private String username;
	
	private RequestInfo requestInfo;

}
