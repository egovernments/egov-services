package org.egov.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaxDetails {

	private Integer id;

	private String tenantId;

	private String fromDate;

	private String toDate;

	private String code;

	private String periodType;

	private String financialYear;

	private AuditDetails auditDetails;
}
