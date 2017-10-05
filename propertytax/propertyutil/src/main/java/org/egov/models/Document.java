package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This object holds list of documents attached during the transaciton for a
 * agreement Author : Narendra
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Document {
	@JsonProperty("id")
	private Long id = null;

	@JsonProperty("documentType")
	@NotNull
	private String documentType = null;

	@JsonProperty("fileStore")
	private String fileStore = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;

}
