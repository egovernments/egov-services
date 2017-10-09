package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ApplicationEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds type of documents to be uploaded during the transaction for
 * each application type. Author : Narendra
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentType {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("tenantId")
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("code")
	@NotNull
	@Size(min = 4, max = 64)
	private String code = null;

	@JsonProperty("application")
	private ApplicationEnum application = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
