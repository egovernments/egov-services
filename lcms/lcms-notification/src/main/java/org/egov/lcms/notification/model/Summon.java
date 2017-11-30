package org.egov.lcms.notification.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.lcms.notification.enums.EntryType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Summon {
	@JsonProperty("code")
	private String code = null;

	@NotNull
	@JsonProperty("isSummon")
	private Boolean isSummon = null;

	@JsonProperty("summonReferenceNo")
	private String summonReferenceNo = null;

	@NotNull
	@JsonProperty("caseNo")
	private String caseNo = null;

	@JsonProperty("summonDate")
	private Long summonDate = null;

	@NotNull
	@JsonProperty("year")
	private String year = null;

	@NotNull
	@JsonProperty("caseType")
	private CaseType caseType = null; // TODO Need to change to model

	@NotNull
	@JsonProperty("plantiffName")
	private String plantiffName = null;

	@NotNull
	@JsonProperty("defendant")
	private String defendant = null;

	@JsonProperty("caseCategory")
	private CaseCategory caseCategory = null;

	@JsonProperty("courtName")
	private Court courtName = null;

	@NotNull
	@JsonProperty("departmentName")
	private Department departmentName = null;

	@JsonProperty("sectionApplied")
	private String sectionApplied = null;

	@NotNull
	@JsonProperty("hearingDate")
	private Long hearingDate = null;

	@JsonProperty("hearingTime")
	private String hearingTime = null;

	@NotNull
	@JsonProperty("ward")
	private String ward = null;

	@JsonProperty("bench")
	private Bench bench = null;

	@JsonProperty("side")
	private Side side = null;

	@JsonProperty("register")
	private Register register = null;

	@NotNull
	@JsonProperty("caseDetails")
	private String caseDetails = null;

	@JsonProperty("plantiffAddress")
	private Address plantiffAddress = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@NotNull
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("stateId")
	private String stateId = null;

	@JsonProperty("documents")
	private List<Document> documents = null;
	
	@JsonProperty("isUlbinitiated")
	private Boolean isUlbinitiated = null;
	
	@JsonProperty("amountReceived")
	private Double amountReceived = null;
	
	@JsonProperty("entryType")
	private EntryType entryType = null;
}
