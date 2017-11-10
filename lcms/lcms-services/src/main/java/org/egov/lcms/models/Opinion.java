package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds information about the opninion
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opinion {
	@JsonProperty("code")
	private String code = null;

	@JsonProperty("opinionRequestDate")
	@NotNull
	private Long opinionRequestDate = null;

	@JsonProperty("departmentName")
	@NotNull
	private Department departmentName = null;

	@JsonProperty("opinionOn")
	@NotNull
	private String opinionOn = null;

	@JsonProperty("documents")
	private List<Document> documents = null;

	@JsonProperty("opinionDescription")
	private String opinionDescription = null;

	@JsonProperty("opinionsBy")
	private Advocate opinionsBy = null;

	@JsonProperty("additionalAdvocate")
	private String additionalAdvocate = null;

	@JsonProperty("inWardDate")
	private Long inWardDate = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("workFlowDetails")
	@Valid
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("stateId")
	private String stateId;
	
	@JsonProperty("caseDetails")
	private CaseDetails caseDetails;
}
