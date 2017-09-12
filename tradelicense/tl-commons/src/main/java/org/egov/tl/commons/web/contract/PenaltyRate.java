package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@JsonProperty("applicationType")
	private ApplicationTypeEnum applicationType = null;

	@JsonProperty("fromRange")
	@NotNull(message = "{error.fromRange.null}")
	private Long fromRange = null;

	@JsonProperty("toRange")
	@NotNull(message = "{error.toRange.null}")
	private Long toRange = null;

	@JsonProperty("rate")
	@NotNull(message = "{error.rate.null}")
	private Double rate = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}