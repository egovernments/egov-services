package org.egov.demand.model;

import java.util.ArrayList;
import java.util.List;
import org.egov.common.contract.request.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demand {

	private Long id;

	private String tenantId;

	private String consumerCode;

	private String consumerType;

	private String businessService;

	private User owner;

	private Long taxPeriodFrom;

	private Long taxPeriodTo;

	private List<DemandDetail> demandDetails = new ArrayList<>();

	private AuditDetail auditDetail;

	private Double minimumAmountPayable;
}