package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds information about the leagle case hearing details
 */

public class LeagleCaseHearingDetails {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("caseJudgeMent")

	@NotEmpty
	@NotNull
	private String caseJudgeMent = null;

	@JsonProperty("caseFinalDecision")
	private String caseFinalDecision = null;

	@JsonProperty("caseStatus")
	@NotEmpty
	@NotNull
	private String caseStatus = null;

	@JsonProperty("nextHearingDate")
	private Long nextHearingDate = null;

	@JsonProperty("attendees")
	@Valid
	private List<LeagleCaseAttenderDetails> attendees = null;

	@JsonProperty("judges")
	@Valid
	private List<LeagleCaseJudgeDetails> judges = null;

	@JsonProperty("tenantId")
	@NotNull
	@NotEmpty
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("referenceNo")
	@NotNull
	@NotEmpty
	private String referenceNo = null;
}
