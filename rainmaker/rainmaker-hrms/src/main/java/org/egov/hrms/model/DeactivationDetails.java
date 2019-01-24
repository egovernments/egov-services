package org.egov.hrms.model;

import javax.validation.constraints.NotNull;

import lombok.*;
import org.egov.hrms.model.enums.DeactivationType;

@EqualsAndHashCode(exclude = {"auditDetails"})
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
@Builder
public class DeactivationDetails {
	
	private String id;

	private String reasonForDeactivation;
	
	@NotNull
	private String orderNo;

	@NotNull
	private Long effectiveFrom;

	@NotNull
	private DeactivationType typeOfDeactivation;
	
	private String tenantId;

	private AuditDetails auditDetails;




}


