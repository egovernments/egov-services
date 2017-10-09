package org.egov.models;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
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
	private BigDecimal collectionAmount = BigDecimal.ZERO;

	private AuditDetails auditDetail;

	private String tenantId;
}
