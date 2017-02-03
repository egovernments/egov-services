package org.egov.pgr.wrapper;

import java.util.ArrayList;
import java.util.List;

import org.egov.pgr.entity.ComplaintType;
import org.egov.pgr.model.ResponseInfo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplaintTypeResponse {

	@JsonProperty("ResponseInfo")
	private ResponseInfo responseInfo = null;
	@JsonProperty("ComplaintType")
	private List<ComplaintType> complaintTypes = new ArrayList<ComplaintType>();

	public ResponseInfo getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(ResponseInfo responseInfo) {
		this.responseInfo = responseInfo;
	}

	public List<ComplaintType> getComplaintTypes() {
		return complaintTypes;
	}

	public void setComplaintTypes(List<ComplaintType> complaintTypes) {
		this.complaintTypes = complaintTypes;
	}

}
