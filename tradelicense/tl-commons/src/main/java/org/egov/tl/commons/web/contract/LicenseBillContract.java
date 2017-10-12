package org.egov.tl.commons.web.contract;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseBillContract {

	private Long id;

	private Long applicationId;

	private String billId;

	private String applicationBillId;
	
	private String tenantId;

	private AuditDetails auditDetails;

}