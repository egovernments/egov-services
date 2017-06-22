package org.egov.demand.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandDetailCriteria {
	
	@NotNull
	private String tenantId;
	
	private Long demandId;
	
	private String taxHeadCode;

	private Long period;
}
