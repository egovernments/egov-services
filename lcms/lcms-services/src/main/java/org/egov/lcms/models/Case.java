package org.egov.lcms.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Case {
	@JsonProperty("code")
	private String code = null;
	
	@JsonProperty("tenantId")
	private String tenantId = null;

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

	@JsonProperty("advocatesDetails")
	private List<AdvocateDetails> advocatesDetails = null;

	@JsonProperty("parawiseComments")
	private List<ParaWiseComment> parawiseComments = null;
	
	@JsonProperty("pleaderEngagementDetails")
	private String pleaderEngagementDetails = null;
	
	@JsonProperty("receiptDate")
	private Long receiptDate = null;
	
	@JsonProperty("resolvation")
	private String resolvation;
	
	@JsonProperty("resolvationDate")
	private Long resolvationDate;
	
	@JsonProperty("advocateInfoDate")
	private Long advocateInfoDate;
	
	@JsonProperty("remarks")
	private String remarks = null;
}