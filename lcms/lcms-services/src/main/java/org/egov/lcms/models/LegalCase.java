package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * An object hold the information about summon/warrent information
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalCase {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("isSummon")
	@NotNull
	@NotEmpty
	private Boolean isSummon = null;

	@JsonProperty("referenceNo")
	private String referenceNo = null;

	@JsonProperty("caseNo")
	@NotNull
	@NotEmpty
	private String caseNo = null;

	@JsonProperty("summonDate")
	@NotNull
	@NotEmpty
	private Long summonDate = null;

	@JsonProperty("year")
	@NotNull
	@NotEmpty
	private String year = null;

	@JsonProperty("caseType")
	@NotNull
	@NotEmpty
	private String caseType = null;

	@JsonProperty("plantiffName")
	@NotNull
	@NotEmpty
	private String plantiffName = null;

	@JsonProperty("dependentName")
	@NotNull
	@NotEmpty
	private String dependentName = null;

	@JsonProperty("caseCategory")
	@NotNull
	@NotEmpty
	private String caseCategory = null;

	@JsonProperty("courtName")
	@NotNull
	@NotEmpty
	private String courtName = null;

	@JsonProperty("departmentName")
	@NotNull
	@NotEmpty
	private String departmentName = null;

	@JsonProperty("sectionApplied")
	@NotNull
	@NotEmpty
	private String sectionApplied = null;

	@JsonProperty("hearingDate")
	@NotNull
	@NotEmpty
	private Long hearingDate = null;

	@JsonProperty("ward")
	@NotNull
	@NotEmpty
	private String ward = null;

	@JsonProperty("bench")
	@NotNull
	@NotEmpty
	private String bench = null;

	@JsonProperty("side")
	@NotNull
	@NotEmpty
	private String side = null;

	@JsonProperty("stamp")
	@NotNull
	@NotEmpty
	private String stamp = null;

	@JsonProperty("caseDetails")
	@NotNull
	@NotEmpty
	private String caseDetails = null;

	@JsonProperty("plantiffAddress")
	@NotNull
	@NotEmpty
	private String plantiffAddress = null;

	@JsonProperty("workFlowDetails")
	@NotNull
	@NotEmpty
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("suitNo")
	private String suitNo = null;

	@JsonProperty("judgeDetails")
	private String judgeDetails = null;

	@JsonProperty("refernceCaseNo")
	private String refernceCaseNo = null;

	@JsonProperty("departmentPerson")
	private String departmentPerson = null;

	@JsonProperty("caseRegistrationDate")
	private Long caseRegistrationDate = null;

	@JsonProperty("vakalatnamaGenerationDate")
	private Long vakalatnamaGenerationDate = null;

	@JsonProperty("isVakalatnamaGenerated")
	private Boolean isVakalatnamaGenerated = null;

	@JsonProperty("witness")
	private List<String> witness = null;

	@JsonProperty("coName")
	private String coName = null;

	@JsonProperty("age")
	private String age = null;

	@JsonProperty("days")
	private Integer days = null;

	@JsonProperty("address")
	private String address = null;

	@JsonProperty("hearingDetails")
	private List<LeagleCaseHearingDetails> hearingDetails = null;

	@JsonProperty("advocates")
	private List<LegalCaseAdvocate> advocates = null;

	@JsonProperty("documents")
	private List<Document> documents = null;
	
	private String stateId;
}
