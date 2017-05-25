package org.egov.lams.model;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkflowDetails {
	
  private String department;
  private String designation;

  @NotNull
  private Long assignee;
  private String action;
  private String status;
  private Long initiatorPosition;
}