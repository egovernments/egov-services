package org.egov.hrms.model;

import javax.validation.constraints.NotNull;

import org.egov.hrms.model.enums.DeactivationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

	private AuditDetails auditDetails;
}
