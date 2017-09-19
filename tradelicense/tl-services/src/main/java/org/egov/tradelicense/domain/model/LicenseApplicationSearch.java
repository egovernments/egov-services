package org.egov.tradelicense.domain.model;

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
public class LicenseApplicationSearch {

	private Long id;
	
	private String applicationNumber;
	
	private String tenantId;
	
	private String applicationType;
	
	private String status;
	
	private String state_id;
	
	private Timestamp applicationDate;
	
	private Long licenseId;
	
	private Double licenseFee;
	
	private String fieldInspectionReport;
	
	private AuditDetails auditDetails;
	
	private String statusName;
	
	@JsonProperty("feeDetails")
	private List<LicenseFeeDetailSearch> feeDetails;
	
	@JsonProperty("supportDocuments")
	private List<SupportDocumentSearch> supportDocuments;
}
