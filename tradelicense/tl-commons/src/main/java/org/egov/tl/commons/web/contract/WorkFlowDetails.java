package org.egov.tl.commons.web.contract;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDetails {

	@JsonProperty("type")
	private String type;

	@JsonProperty("businessKey")
	private String businessKey;

	@JsonProperty("department")
	private String department;

	@JsonProperty("designation")
	private String designation;

	@JsonProperty("assignee")
	private Long assignee;

	@JsonProperty("action")
	private String action;

	@JsonProperty("status")
	private String status;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("senderName")
	private String senderName;

	@JsonProperty("details")
	private String details;

	@JsonProperty("stateId")
	private String stateId;

}
