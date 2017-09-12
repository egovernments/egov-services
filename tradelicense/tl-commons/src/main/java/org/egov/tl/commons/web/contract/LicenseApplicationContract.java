package org.egov.tl.commons.web.contract;

import java.util.List;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LicenseApplicationContract {

	private Long id;
	
	@JsonProperty("applicationNumber")
	private String applicationNumber;
	
	private String tenantId;
	
	private ApplicationTypeEnum applicationType;
	
	private String status;
	
	private String state_id;
	
	@JsonProperty("applicationDate")
	private Long applicationDate;
	
	private long licenseId;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
	
	@JsonProperty("licenseFee")
	private Double licenseFee;
	
	@JsonProperty("fieldInspectionReport")
	private String fieldInspectionReport;
	
	@JsonProperty("feeDetails")
	private List<LicenseFeeDetailContract> feeDetails;

	@JsonProperty("supportDocuments")
	private List<SupportDocumentContract> supportDocuments;
	
	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;
}
