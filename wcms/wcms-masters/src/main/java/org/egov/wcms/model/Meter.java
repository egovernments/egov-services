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
public class Meter {
	
	@NotNull
	private long id;
	
	@NotNull
	private String meterMake;
	
	@NotNull
	private String meterReading;
	
	@NotNull
	private AuditDetails auditDetails;
	
	@NotNull
	private String tenantId;
	

}
