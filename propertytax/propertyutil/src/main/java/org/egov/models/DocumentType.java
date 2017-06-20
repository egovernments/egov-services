package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds type of documents to be uploaded during the transaction for
 * each application type. Author : Narendra
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentType {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("name")
	private String name = null;

	@JsonProperty("application")
	private ApplicationEnum application = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
