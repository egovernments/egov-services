package org.egov.lams.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowDetails {

	private String department;
	private String designation;
	private String nextDesignation;
	private Long assignee;
	private String action;
	private String status;
	private Long initiatorPosition;
	private String comments;
}