package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowDetails {

	private String	department;

	private String  designation;	

	private Integer	 assignee;	

	private String	 action;	

	private String	status;

}
