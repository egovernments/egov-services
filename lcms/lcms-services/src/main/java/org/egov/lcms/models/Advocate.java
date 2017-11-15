package org.egov.lcms.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Advocate
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advocate extends PersonDetails {

	@JsonProperty("isIndividual")
	private Boolean isIndividual = true;

	@NotNull
	@JsonProperty("dateOfEmpanelment")
	private Long dateOfEmpanelment;

	@NotNull
	@JsonProperty("standingCommitteeDecisionDate")
	private Long standingCommitteeDecisionDate;

	@NotNull
	@JsonProperty("empanelmentFromDate")
	private Long empanelmentFromDate;

	@NotNull
	@JsonProperty("newsPaperAdvertismentDate")
	private Long newsPaperAdvertismentDate;

	@NotNull
	@JsonProperty("empanelmentToDate")
	private Long empanelmentToDate;

	@NotNull
	@JsonProperty("bankName")
	private String bankName;

	@NotNull
	@JsonProperty("bankBranch")
	private String bankBranch;

	@NotNull
	@JsonProperty("bankAccountNo")
	private String bankAccountNo;

	@NotNull
	@JsonProperty("ifscCode")
	private String ifscCode;

	@NotNull
	@JsonProperty("micr")
	private String micr;

	@JsonProperty("isActive")
	private Boolean isActive = true;

	@JsonProperty("isTerminate")
	private Boolean isTerminate = false;

	@JsonProperty("inActiveDate")
	private Long inActiveDate;

	@JsonProperty("terminationDate")
	private Long terminationDate;

	@JsonProperty("reasonOfTermination")
	private String reasonOfTermination;
	
	@JsonProperty("status")
	private String status;
}