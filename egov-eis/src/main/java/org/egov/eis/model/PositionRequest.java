package org.egov.eis.model;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("Position")
	private Position position = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
