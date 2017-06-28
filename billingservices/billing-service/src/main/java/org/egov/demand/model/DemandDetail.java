package org.egov.demand.model;

import java.math.BigDecimal;

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
	private String taxHeadMasterCode;

	@NotNull
	private BigDecimal taxAmount;
	
	@NotNull
	private BigDecimal collectionAmount = BigDecimal.valueOf(0d);

	private AuditDetail auditDetail;

	private String tenantId;
}