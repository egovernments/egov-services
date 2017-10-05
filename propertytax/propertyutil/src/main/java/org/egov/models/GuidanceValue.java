package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuidanceValue {

	private Long id;

	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId;

	@Size(min = 4, max = 128)
	private String name;

	@NotNull
	@Size(min = 1, max = 64)
	private String boundary;

	@Size(min = 1, max = 64)
	private String structure;

	@Size(min = 1, max = 64)
	private String usage;

	@Size(min = 1, max = 64)
	private String subUsage;

	@Size(min = 1, max = 64)
	private String occupancy;

	private Double value;

	@NotNull
	private String fromDate;

	@NotNull
	private String toDate;

	private AuditDetails auditDetails;

}
