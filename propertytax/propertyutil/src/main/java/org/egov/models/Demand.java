package org.egov.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Demand {
	private String id;

	@NotNull
	private String tenantId;

	@NotNull
	private String consumerCode;

	@NotNull
	private String consumerType;

	@NotNull
	private String businessService;

	@NotNull
	private Owner owner;

	@NotNull
	private Long taxPeriodFrom;

	@NotNull
	private Long taxPeriodTo;

	@Valid
	@NotNull
	private List<DemandDetail> demandDetails = new ArrayList<DemandDetail>();

	private BigDecimal minimumAmountPayable = BigDecimal.ZERO;

	private AuditDetails auditDetail;
}
