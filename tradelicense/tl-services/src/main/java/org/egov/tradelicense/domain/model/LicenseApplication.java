package org.egov.tradelicense.domain.model;

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
public class LicenseApplication {

	private Long id;
	
	private String applicationNumber;
	
	private String tenantId;
	
	private String applicationType;
	
	private String status;
	
	private String state_id;
	
	private Long applicationDate;
	
	private long licenseId;
	
	private AuditDetails auditDetails;
	
	@JsonProperty("feeDetails")
	private List<LicenseFeeDetail> feeDetails;
	
	@JsonProperty("licenseFee")
	private Double licenseFee;
	
	@JsonProperty("fieldInspectionReport")
	private String fieldInspectionReport;
	
	@JsonProperty("supportDocuments")
	private List<SupportDocument> supportDocuments;
}
