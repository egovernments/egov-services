package org.egov.lcms.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds information about the advocate payment
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvocatePayment {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("advocateName")
	@NotEmpty
	@NotNull
	private String advocateName = null;

	@JsonProperty("demandDate")
	@NotEmpty
	@NotNull
	private Long demandDate = null;

	@JsonProperty("year")
	private String year = null;

	@JsonProperty("caseType")
	@NotEmpty
	@NotNull
	private String caseType = null;

	@JsonProperty("caseNo")
	@NotEmpty
	@NotNull
	private String caseNo = null;

	@JsonProperty("caseStatus")
	private String caseStatus = null;

	@JsonProperty("amountClaimed")
	@NotEmpty
	@NotNull
	private Double amountClaimed = null;

	@JsonProperty("amountRecived")
	@NotEmpty
	@NotNull
	private Double amountRecived = null;

	@JsonProperty("allowance")
	private Double allowance = null;

	@JsonProperty("isPartialPayment")
	@NotEmpty
	@NotNull
	private Boolean isPartialPayment = null;

	@JsonProperty("bankName")
	@NotEmpty
	@NotNull
	private String bankName = null;

	@JsonProperty("bankBranch")
	@NotEmpty
	@NotNull
	private String bankBranch = null;

	@JsonProperty("bankAccountNo")
	@NotEmpty
	@NotNull
	private String bankAccountNo = null;

	@JsonProperty("panNo")
	@NotEmpty
	@NotNull
	@Size(min=12,max=12)
	private String panNo = null;

	@JsonProperty("invoiceDoucment")
	private Document invoiceDoucment = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("isGenerateBill")
	private Boolean isGenerateBill = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("resolutionDate")
	private Long resolutionDate = null;

	@JsonProperty("resolutionNo")
	private String resolutionNo = null;

	@JsonProperty("resolutionRemarks")
	private String resolutionRemarks = null;
	
	private String stateId;

}
