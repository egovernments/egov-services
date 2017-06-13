package org.egov.demand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandReason {

	private Long id;

	private Installment installment;

	private ReasonMaster reasonMaster;

	private String glCode;

	private AuditDetail auditDetails;

	private String tenantId;
}
