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

public class MeterReading {
	
	@NotNull
	private long id;
	
	@NotNull
	private long connectionId;
	
	@NotNull
	private long reading;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	
	

}
