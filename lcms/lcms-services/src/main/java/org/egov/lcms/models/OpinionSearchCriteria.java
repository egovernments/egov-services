package org.egov.lcms.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class OpinionSearchCriteria {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("opinionRequestDate")
	@NotNull
	@NotEmpty
	private Long opinionRequestDate = null;

	@JsonProperty("departmentName")
	@NotNull
	@NotEmpty
	private String departmentName = null;

	@JsonProperty("opinionOn")
	@NotNull
	@NotEmpty
	private String opinionOn = null;

	@JsonProperty("documents")
	private List<String> documents = null;

	@JsonProperty("opinionDescriptions")
	private List<String> opinionDescriptions = null;

	@JsonProperty("opinionsBy")
	private List<String> opinionsBy = null;

	@JsonProperty("inWardDate")
	private Long inWardDate = null;

	@JsonProperty("tenantId")
	@NotEmpty
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("workFlowDetails")
	@Valid
	private WorkFlowDetails workFlowDetails = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

	@JsonProperty("auditDetails")
	private String stateId;
}
