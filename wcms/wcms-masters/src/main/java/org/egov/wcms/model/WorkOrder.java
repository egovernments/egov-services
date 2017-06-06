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

public class WorkOrder {

	@NotNull
	private long id;
	
	@NotNull
	private long connectionId;
	
	@NotNull
	private String remarks;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
}
