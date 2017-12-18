package org.egov.lcms.notification.model;

import java.util.List;

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
	
	@JsonProperty("nextHearingTime")
	private String nextHearingTime = null;

	@JsonProperty("attendees")
	private List<Attender> attendees = null;

	@JsonProperty("judges")
	private List<Attender> judges = null;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
	
	@JsonProperty("judgeMentDate")
	private Long judgeMentDate = null;
	
	@JsonProperty("advocateOpinion")
	private String advocateOpinion = null;
	
	@JsonProperty("furtherProcessDetails")
	private String furtherProcessDetails = null;
	
	@JsonProperty("darkhasthDueDate")
	private Long darkhasthDueDate = null;
	
	@JsonProperty("currentHearingDate")
	private Long currentHearingDate = null;

	@JsonProperty("currentHearingTime")
	private String currentHearingTime = null;
}