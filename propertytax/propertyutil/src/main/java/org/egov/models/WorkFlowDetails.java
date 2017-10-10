package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds info about the assignee and other workflow related info
 * Author : Narendra
 * 
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkFlowDetails {
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
}
