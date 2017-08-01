package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ApplicationTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * DocumentType
 * 
 * @author Shubham pratap Singh
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentType {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	@Size(max = 256)
	private String name = null;

	private Boolean mandatory = true;

	private Boolean enabled = true;
	
	@NotNull
	private ApplicationTypeEnum applicationType;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}