package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demand {

	private Long id = null;

	private String tenantId = null;

	private String consumerCode = null;

	private String consumerType = null;

	private String businessService = null;

	private Owner owner = null;

	private Long taxPeriodFrom = null;

	private Long taxPeriodTo = null;

	private List<DemandDetail> demandDetails = new ArrayList<>();

	private AuditDetail auditDetails = null;

	private Double minimumAmountPayable = null;
}