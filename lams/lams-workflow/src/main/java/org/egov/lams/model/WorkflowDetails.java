package org.egov.lams.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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