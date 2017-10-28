package org.egov.lcms.workflow.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkFlowDetails {
	@JsonProperty("type")
	private String type = null;

	@JsonProperty("businessKey")
	private String businessKey = null;

	@JsonProperty("department")
	private String department = null;

	@JsonProperty("designation")
	private String designation = null;

	@JsonProperty("assignee")
	private Long assignee = null;

	@JsonProperty("action")
	private String action = null;

	@JsonProperty("status")
	private String status = null;

	@JsonProperty("comments")
	private String comments = null;

	@JsonProperty("senderName")
	private String senderName = null;

	@JsonProperty("details")
	private String details = null;

	@JsonProperty("stateId")
	private String stateId = null;

}
