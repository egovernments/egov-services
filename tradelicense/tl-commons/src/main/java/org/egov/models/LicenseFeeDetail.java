package org.egov.models;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * LicenseFeeDetail
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseFeeDetail {

	private Long id = null;

	@NotNull
	private Long licenseId = null;

	@NotNull
	private Long financialYear = null;

	@NotNull
	private Double amount = null;

	private Boolean paid = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}