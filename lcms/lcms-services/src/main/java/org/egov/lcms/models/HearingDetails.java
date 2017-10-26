package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HearingDetails {
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("caseJudgeMent")
	private String caseJudgeMent = null;

	@JsonProperty("caseFinalDecision")
	private String caseFinalDecision = null;

	@JsonProperty("caseStatus")
	private CaseStatus caseStatus = null;

	@JsonProperty("nextHearingDate")
	private Long nextHearingDate = null;

	@JsonProperty("attendees")
	private List<Attender> attendees = null;

	@JsonProperty("judges")
	private List<Attender> judges = null;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}