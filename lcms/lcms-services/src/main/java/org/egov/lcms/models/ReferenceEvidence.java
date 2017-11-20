package org.egov.lcms.models;

import java.util.List;

import javax.validation.constraints.NotNull;
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
public class ReferenceEvidence {

	@JsonProperty("code")
	private String code;

	@NotNull
	@JsonProperty("referenceType")
	private String referenceType;

	@NotNull
	@JsonProperty("referenceDate")
	private Long referenceDate;

	@JsonProperty("caseNo")
	private String caseNo;

	@NotNull
	@Size(min = 15, max = 100)
	@JsonProperty("description")
	private String description;

	@JsonProperty("documents")
	private List<Document> documents;

	@JsonProperty("tenantId")
	private String tenantId;

	@JsonProperty("caseCode")
	private String caseCode;
	
	@JsonProperty("referenceCaseNo")
	private String referenceCaseNo;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
