package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in LicenseStatus
 * 
 * @author Shubham pratap Singh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseStatus {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	@Size(min = 4, max = 256)
	private String name = null;

	@NotNull
	@Size(min = 4, max = 50)
	private String code = null;

	private Boolean active = true;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}