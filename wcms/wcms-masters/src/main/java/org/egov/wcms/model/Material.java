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

public class Material {

	@NotNull
	private long id;
	
	@NotNull
	private long connectionId;
	
	@NotNull
	private String name;
	
	@NotNull
	private long quantity;
	
	@NotNull
	private double size;
	
	@NotNull
	private double amountDetails;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	
	
}
