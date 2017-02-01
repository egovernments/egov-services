package org.egov.boundary.web.wrapper;

import javax.validation.Valid;

import org.egov.boundary.model.CrossHierarchy;

import com.fasterxml.jackson.annotation.JsonProperty;


public class CrossHierarchyRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("CrossHierarchy")
	private CrossHierarchy  crossHierarchy  = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public CrossHierarchy getCrossHierarchy() {
		return crossHierarchy;
	}

	public void setCrossHierarchy(CrossHierarchy crossHierarchy) {
		this.crossHierarchy = crossHierarchy;
	}

	 

 

	 
}
