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

	private Long id;

	private Installment installment;

	private String consumerCode;

	private String consumerType;

	private String businessService;

	private Owner owner;

	private List<DemandDetail> demandDetails = new ArrayList<>();

	private AuditDetail auditDetail;

	private String tenantId;

	private Double minimumAmountPayable;
}
