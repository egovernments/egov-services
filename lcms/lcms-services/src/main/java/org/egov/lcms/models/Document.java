package org.egov.lcms.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object holds list of documents attached during the transaciton for a
 * property
 */

public class Document {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("documentName")
	private String documentName = null;

	@JsonProperty("fileStore")
	private String fileStore = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}
