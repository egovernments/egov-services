package org.egov.tradelicense.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseBill {

	private Long id;

	private Long applicationId;

	private String billId;

	private String applicationBillId;

	private String tenantId;

	private AuditDetails auditDetails;

}