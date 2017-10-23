package org.egov.tradelicense.domain.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportDocument {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("tenantId")
	private String tenantId;
	
	@JsonProperty("applicationId")
	private Long applicationId;
	
	@NotNull
	@JsonProperty("documentTypeId")
	private Long documentTypeId;

	@NotNull
	@JsonProperty("fileStoreId")
	private String fileStoreId;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;

}