package org.egov.wcms.model;

import javax.validation.constraints.NotNull;


import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class Timeline {
	
	@NotNull
	private long id;
	
	@NotNull
	private long connectionId;
	
	@NotNull
	private String remarks;
	
	@NotNull
	private User assigner;
	
	@NotNull
	private User asignee;
	
	@NotNull
	private WorkflowDetails workflowDetails;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	
	

	
}
