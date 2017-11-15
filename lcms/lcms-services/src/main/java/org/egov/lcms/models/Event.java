package org.egov.lcms.models;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	@JsonProperty("code")
	private String code;
	
	@JsonProperty("entity")
	private String entity;
	
	@JsonProperty("moduleName")
	private String moduleName;
	
	@JsonProperty("entityCode")
	private String entityCode;
	
	@JsonProperty("caseNo")
	private String caseNo;
	
	@JsonProperty("departmentConcernPerson")
	private String departmentConcernPerson;
	
	@JsonProperty("nextHearingTime")
	private String nextHearingTime;
	
	@JsonProperty("nextHearingDate")
	private Long nextHearingDate;
	
	@JsonProperty("hearingDetailsCode")
	private String hearingDetailsCode;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

	@Size(min = 4, max = 128)
	@JsonProperty("tenantId")
	private String tenantId;
}
