package org.egov.demand.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasonMaster {

	private Long id;

	private String name;

	private String code;

	private String service;

	private Boolean isDebit = false;

	private AuditDetail auditDetails;

	private String tenantId;

	private Boolean isActualDemand;
}
