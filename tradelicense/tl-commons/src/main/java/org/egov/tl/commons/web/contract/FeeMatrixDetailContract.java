package org.egov.tl.commons.web.contract;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in FeeMatrixDetail
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeMatrixDetailContract {

	private Long id = null;

	@JsonProperty("feeMatrixId")

	private Long feeMatrixId = null;
	
	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@JsonProperty("uomFrom")
	@NotNull(message = "{error.uomFrom.null}")
	private Long uomFrom = null;

	@JsonProperty("uomTo")
	private Long uomTo = null;

	@NotNull(message = "{error.FeeMatrixDetail.amount}")
	@Min(1)
	@Digits(integer = 10, fraction = 2, message = "{error.valid.amount}")
	@JsonProperty("amount")
	private Double amount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}