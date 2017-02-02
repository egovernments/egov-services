package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssigneeFilterInfo {

	@JsonProperty("boundary_id")
	private Long boundaryId;
	
	@JsonProperty("complaint_type_code")
	private String complaintTypeCode;
	
	@JsonProperty("current_assignee_id")
	private Long currentAssigneeId;

	public Long getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(Long boundaryId) {
		this.boundaryId = boundaryId;
	}

	public String getComplaintTypeCode() {
		return complaintTypeCode;
	}

	public void setComplaintTypeCode(String complaintTypeCode) {
		this.complaintTypeCode = complaintTypeCode;
	}

	public Long getCurrentAssigneeId() {
		return currentAssigneeId;
	}

	public void setCurrentAssigneeId(Long currentAssigneeId) {
		this.currentAssigneeId = currentAssigneeId;
	}
	
}
