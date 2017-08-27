package org.egov.tl.commons.web.contract;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
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
public class FeeMatrixDetail {

	private Long id = null;

	@JsonProperty("feeMatrixId")

	private Long feeMatrixId = null;

	@JsonProperty("uomFrom")
	@NotNull(message = "{error.uomFrom.null}")
	private Long uomFrom = null;

	@JsonProperty("uomTo")
	@NotNull(message = "{error.uomTo.null}")
	private Long uomTo = null;

	@NotNull(message = "{error.FeeMatrixDetail.amount}")
	@Min(1)
	@Digits(integer = 10, fraction = 2, message = "{error.valid.amount}")
	@JsonProperty("amount")
	private Double amount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}