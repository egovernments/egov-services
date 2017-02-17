package org.egov.eis.web.contract;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionHierarchyRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("PositionHierarchy")
	private PositionHierarchy positionHierarchy = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public PositionHierarchy getPositionHierarchy() {
		return positionHierarchy;
	}

	public void setPositionHierarchy(PositionHierarchy positionHierarchy) {
		this.positionHierarchy = positionHierarchy;
	}

}
