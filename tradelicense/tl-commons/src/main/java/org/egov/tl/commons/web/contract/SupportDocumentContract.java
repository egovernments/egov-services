package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SupportDocumentContract {

	@JsonProperty("id")
	private Long id;

	@JsonProperty("licenseId")
	private Long licenseId;
	
	@JsonProperty("applicationId")
	private Long applicationId;

	@JsonProperty("tenantId")
	private String tenantId;
	
	@NotNull(message = "{error.license.documenttype}")
	@JsonProperty("documentTypeId")
	private Long documentTypeId;

	@NotNull(message = "{error.license.filestore}")
	@JsonProperty("fileStoreId")
	private String fileStoreId;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails;
}