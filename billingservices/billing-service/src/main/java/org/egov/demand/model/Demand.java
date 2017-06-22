package org.egov.demand.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import org.egov.demand.model.enums.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
	
	@NotNull
	private Type type;

	private List<DemandDetail> demandDetails = new ArrayList<>();

	private AuditDetail auditDetail;

	private BigDecimal minimumAmountPayable;
}