package org.egov.tradelicense.domain.model;

import java.util.List;

import org.egov.tl.commons.web.contract.LicenseBill;
import org.egov.tl.commons.web.contract.WorkFlowDetails;

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
	
	private Long licenseId;
	
	private AuditDetails auditDetails;
	
	@JsonProperty("feeDetails")
	private List<LicenseFeeDetail> feeDetails;
	
	@JsonProperty("licenseFee")
	private Double licenseFee;
	
	@JsonProperty("fieldInspectionReport")
	private String fieldInspectionReport;
	
	@JsonProperty("supportDocuments")
	private List<SupportDocument> supportDocuments;
	
	@JsonProperty("licenseBills")
	private List<LicenseBill> licenseBills;
	
	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;
}
