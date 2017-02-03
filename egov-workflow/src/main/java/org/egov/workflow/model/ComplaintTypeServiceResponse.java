package org.egov.workflow.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComplaintTypeServiceResponse {

	@JsonProperty("ComplaintType")
	private List<ComplaintTypeResponse> complaintType;

	public List<ComplaintTypeResponse> getComplaintType() {
		return complaintType;
	}

}
