package org.egov.demand.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandDetail {

	private String id;
	
	private String demandId;	

	@NotNull
	private String taxHeadCode;

	@NotNull
	private Double taxAmount;
	
	@NotNull
	private Double collectionAmount = 0.0d;

	private AuditDetail auditDetail;

	@NotNull
	private String tenantId;
}