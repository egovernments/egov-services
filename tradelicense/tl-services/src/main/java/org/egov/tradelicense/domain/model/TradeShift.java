package org.egov.tradelicense.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeShift {

	private Long id;

	private String tenantId;

	private Long licenseId;

	private Integer shiftNo;

	private Long fromTime;

	private Long toTime;

	private String remarks;

	private AuditDetails auditDetails;
}