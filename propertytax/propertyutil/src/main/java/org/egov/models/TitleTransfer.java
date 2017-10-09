package org.egov.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TitleTransfer {

	@JsonProperty("id")
	private Long id = null;

	@Valid
	@NotNull
	@JsonProperty("newOwners")
	private List<User> newOwners;

	@NotNull
	@JsonProperty("upicNo")
	private String upicNo;

	@NotNull
	@JsonProperty("transferReason")
	private String transferReason;

	@Size(min = 1, max = 15)
	@JsonProperty("registrationDocNo")
	private String registrationDocNo;

	@JsonProperty("registrationDocDate")
	private String registrationDocDate;

	@NotNull
	@JsonProperty("departmentGuidelineValue")
	private Double departmentGuidelineValue;

	@JsonProperty("partiesConsiderationValue")
	private Double partiesConsiderationValue;

	@JsonProperty("courtOrderNumber")
	private Long courtOrderNumber;

	@Size(min = 1, max = 15)
	@JsonProperty("subRegOfficeName")
	private String subRegOfficeName;

	@JsonProperty("titleTrasferFee")
	private Double titleTrasferFee;

	@JsonProperty("documents")
	private List<Document> documents;

	@JsonProperty("correspondenceAddress")
	private Address correspondenceAddress;

	@JsonProperty("stateId")
	private String stateId;

	@JsonProperty("receiptnumber")
	private String receiptnumber;

	@JsonProperty("receiptdate")
	private String receiptdate;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails;

	@JsonProperty("applicationNo")
	private String applicationNo;

	@NotNull
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("demandId")
	private String demandId;
}
