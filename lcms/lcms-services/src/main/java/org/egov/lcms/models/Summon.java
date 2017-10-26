package org.egov.lcms.models;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.tomcat.jni.Address;
import org.hibernate.validator.constraints.NotEmpty;
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
	@NotEmpty
	@JsonProperty("caseNo")
	private String caseNo = null;

	@JsonProperty("summonDate")
	private Long summonDate = null;

	@NotNull
	@NotEmpty
	@JsonProperty("year")
	private String year = null;

	@NotNull
	@NotEmpty
	@JsonProperty("caseType")
	private String caseType = null;

	@NotNull
	@NotEmpty
	@JsonProperty("plantiffName")
	private String plantiffName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("dependentName")
	private String dependentName = null;

	@JsonProperty("caseCategory")
	private CaseCategory caseCategory = null;

	@JsonProperty("courtName")
	private Court courtName = null;

	@NotNull
	@NotEmpty
	@JsonProperty("departmentName")
	private String departmentName = null;

	@JsonProperty("sectionApplied")
	private String sectionApplied = null;

	@NotNull
	@JsonProperty("hearingDate")
	private Long hearingDate = null;

	@JsonProperty("hearingTime")
	private Long hearingTime = null;

	@NotNull
	@NotEmpty
	@JsonProperty("ward")
	private String ward = null;

	@JsonProperty("bench")
	private Bench bench = null;

	@JsonProperty("side")
	private Side side = null;

	@JsonProperty("stamp")
	private Stamp stamp = null;

	@NotNull
	@NotEmpty
	@JsonProperty("caseDetails")
	private String caseDetails = null;

	@JsonProperty("plantiffAddress")
	private Address plantiffAddress = null;

	@JsonProperty("workFlowDetails")
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@NotNull
	@NotEmpty
	@Size(min = 1, max = 128)
	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("stateId")
	private String stateId = null;

	@JsonProperty("documents")
	private List<String> documents = null;
}
