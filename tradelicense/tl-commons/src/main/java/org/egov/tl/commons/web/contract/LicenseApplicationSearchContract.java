package org.egov.tl.commons.web.contract;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseApplicationSearchContract {

	private Long id;
	
	private String applicationNumber;
	
	private String tenantId;
	
	private String applicationType;
	
	private String status;
	
	private String state_id;
	
	private Timestamp applicationDate;
	
	private long licenseId;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("feeDetails")
	private List<LicenseFeeDetailContract> feeDetails;

	@JsonProperty("supportDocuments")
	private List<SupportDocumentSearchContract> supportDocuments;
	
	private WorkFlowDetails workFlowDetails;
}
