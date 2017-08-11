package org.egov.tradelicense.domain.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportDocument {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("licenseId")
	private Long licenseId;

	@NotNull
	@JsonProperty("documentTypeId")
	private Long documentTypeId;

	@NotNull
	@JsonProperty("fileStoreId")
	private Long fileStoreId;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}