package org.egov.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.enums.ApplicationTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in PenalityRate
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenaltyRate {

	private Long id = null;

	@JsonProperty("tenantId")
	@NotNull
	@Size(min = 4, max = 128)
	private String tenantId = null;

	@NotNull
	private ApplicationTypeEnum applicationType = null;

	@NotNull
	private Long fromRange = null;

	@NotNull
	private Long toRange = null;

	@NotNull
	private Double rate = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}