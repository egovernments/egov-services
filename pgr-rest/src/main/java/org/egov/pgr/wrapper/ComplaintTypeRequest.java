package org.egov.pgr.wrapper;

import javax.validation.Valid;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.model.RequestInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplaintTypeRequest {

	@Valid
	@JsonProperty("RequestInfo")
	private RequestInfo requestInfo = null;
	@Valid
	@JsonProperty("ComplaintType")
	private ComplaintType complaintType = null;

	public RequestInfo getRequestInfo() {
		return requestInfo;
	}

	public void setRequestInfo(RequestInfo requestInfo) {
		this.requestInfo = requestInfo;
	}

	public ComplaintType getComplaintType() {
		return complaintType;
	}

	public void setComplaintType(ComplaintType complaintType) {
		this.complaintType = complaintType;
	}

}
