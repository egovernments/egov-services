package org.egov.workflow.web.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo {

	private String apiId;

	private String ver;

	private String ts;

	private String action;

	private String did;

	private String key;

	private String msgId;

	private String requesterId;

	private String authToken;

	private String tenantId;
	
	private User userInfo;

}