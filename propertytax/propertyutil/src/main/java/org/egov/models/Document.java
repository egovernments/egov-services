package org.egov.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object holds list of documents attached during the transaciton for a
 * agreement Author : Narendra
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Document {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("documentType")
	private DocumentType documentType = null;

	@JsonProperty("fileStore")
	private String fileStore = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
