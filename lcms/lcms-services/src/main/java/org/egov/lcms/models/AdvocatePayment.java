package org.egov.lcms.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvocatePayment {

	@JsonProperty("code")
	private String code = null;

	@JsonProperty("advocate")
	private Advocate advocate = null;

	@NonNull
	@JsonProperty("demandDate")
	private Long demandDate = null;

	@JsonProperty("year")
	private String year = null;

	@JsonProperty("caseType")
	private CaseType caseType = null;

	@JsonProperty("caseStatus")
	private CaseStatus caseStatus = null;

	@JsonProperty("amountClaimed")
	private Double amountClaimed = null;

	@JsonProperty("amountRecived")
	private Double amountRecived = null;

	@JsonProperty("allowance")
	private Double allowance = null;

	@JsonProperty("isPartialPayment")
	private Boolean isPartialPayment = null;

	@NonNull
	@JsonProperty("invoiceDoucment")
	private Document invoiceDoucment = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("resolutionDate")
	private Long resolutionDate = null;

	@JsonProperty("resolutionNo")
	private String resolutionNo = null;
	
	@JsonProperty("resolution")
	private String resolution = null;

	@JsonProperty("resolutionRemarks")
	private String resolutionRemarks = null;

	@JsonProperty("advocateCharges")
	private List<AdvocateCharge> advocateCharges = null;

	@JsonProperty("modeOfPayment")
	private String modeOfPayment = null;

	@JsonProperty("instrumentNumber")
	private String instrumentNumber = null;

	@JsonProperty("instrumentDate")
	private Long instrumentDate = null;

	@JsonProperty("stateId")
	private String stateId = null;
	
	@JsonProperty("voucherNo")
	private String voucherNo = null;
	
	@JsonProperty("voucherDate")
	private Long voucherDate = null;

}
