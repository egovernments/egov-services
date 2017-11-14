package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agency {
	
	@Size(min = 1, max = 100)
	@JsonProperty("name")
	private String name ;

	@JsonProperty("code")
	private String code;
	
	@JsonProperty("agencyAddress")
	private String agencyAddress;

	@NotNull
	@JsonProperty("isIndividual")
	private Boolean isIndividual ;

	@NotNull
	@JsonProperty("dateOfEmpanelment")
	private Long dateOfEmpanelment ;

	@NotNull
	@JsonProperty("standingCommitteeDecisionDate")
	private Long standingCommitteeDecisionDate ;

	@NotNull
	@JsonProperty("empanelmentFromDate")
	private Long empanelmentFromDate ;

	@NotNull
	@JsonProperty("newsPaperAdvertismentDate")
	private Long newsPaperAdvertismentDate ;

	@NotNull
	@JsonProperty("empanelmentToDate")
	private Long empanelmentToDate ;

	@JsonProperty("personDetails")
	private List<PersonDetails> personDetails;
	
	@JsonProperty("advocates")
	private List<Advocate> advocates;
	
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

	@NotNull
	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}
