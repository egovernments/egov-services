package org.egov.workflow.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssigneeRequestInfo {

	@JsonProperty("ResposneInfo")
	private ResponseInfo resposneInfo = null;
	
	@JsonProperty("AssigneeFilterInfo")
	private AssigneeFilterInfo assigneeFilterInfo = null;
	
	public AssigneeRequestInfo() {
		
	}

	public ResponseInfo getResposneInfo() {
		return resposneInfo;
	}

	public void setResposneInfo(ResponseInfo resposneInfo) {
		this.resposneInfo = resposneInfo;
	}

	public AssigneeFilterInfo getAssigneeFilterInfo() {
		return assigneeFilterInfo;
	}

	public void setAssigneeFilterInfo(AssigneeFilterInfo assigneeFilterInfo) {
		this.assigneeFilterInfo = assigneeFilterInfo;
	}
	
    
}
