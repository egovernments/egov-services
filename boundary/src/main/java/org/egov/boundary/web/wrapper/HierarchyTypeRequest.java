package org.egov.boundary.web.wrapper;

import javax.validation.Valid;

import org.egov.boundary.model.HierarchyType;

import com.fasterxml.jackson.annotation.JsonProperty;


public class HierarchyTypeRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("HierarchyType")
	private HierarchyType hierarchyType = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public HierarchyType getHierarchyType() {
		return hierarchyType;
	}

	public void setHierarchyType(HierarchyType hierarchyType) {
		this.hierarchyType = hierarchyType;
	}

}
