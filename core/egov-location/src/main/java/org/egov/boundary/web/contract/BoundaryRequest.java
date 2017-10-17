package org.egov.boundary.web.contract;

import javax.validation.Valid;

import org.egov.boundary.domain.model.Boundary;
import org.egov.common.contract.request.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BoundaryRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("Boundary")
	private Boundary  boundary  = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public Boundary getBoundary() {
		return boundary;
	}

	public void setBoundary(Boundary boundary) {
		this.boundary = boundary;
	}

}
