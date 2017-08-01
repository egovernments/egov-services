package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * SupportDocument
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportDocument {

	private Long id = null;

	@NotNull
	private Long licenseId = null;

	@NotNull
	private Long documentTypeId = null;

	@NotNull
	private Long fileStoreId = null;

	private String comments = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}