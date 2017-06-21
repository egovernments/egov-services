package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds info about the assignee and other workflow related info
 * Author : Narendra
 * 
 */

@Data
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
}
