package org.egov.demand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandDetail {

	private Long id;

	private String taxHeadCode;

	private Double taxAmount;

	private Double collectionAmount = 0.0d;

	private AuditDetail auditDetails;

	private String tenantId;
}