package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Case {
	@JsonProperty("code")
	private String code = null;

	@NotEmpty
	@NotNull
	@JsonProperty("summon")
	private Summon summon = null;

	@JsonProperty("oldCaseNo")
	private String oldCaseNo = null;

	@JsonProperty("suitNo")
	private String suitNo = null;

	@JsonProperty("judgeDetails")
	private String judgeDetails = null;

	@JsonProperty("caseRefernceNo")
	private String caseRefernceNo = null;

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
	private Address address = null;

	@JsonProperty("hearingDetails")
	private List<HearingDetails> hearingDetails = null;

	@JsonProperty("advocates")
	private List<Advocate> advocates = null;

	@JsonProperty("assignedDates")
	private List<Long> assignedDates = null;

	@JsonProperty("parawiseComments")
	private List<ParaWiseComment> parawiseComments = null;
}