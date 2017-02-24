package org.egov.boundary.web.contract;

import javax.validation.Valid;

import org.egov.boundary.persistence.entity.BoundaryType;
import org.egov.boundary.persistence.entity.HierarchyType;

import com.fasterxml.jackson.annotation.JsonProperty;


public class BoundaryTypeRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("BoundaryType")
	private BoundaryType boundaryType = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public BoundaryType getBoundaryType() {
		return boundaryType;
	}

	public void setBoundaryType(BoundaryType boundaryType) {
		this.boundaryType = boundaryType;
	}

	 
}
