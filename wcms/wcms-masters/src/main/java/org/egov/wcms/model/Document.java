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
public class Document {
	
	@NotNull
	private long id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String description;
	
	@NotNull
	private boolean active;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	

}
