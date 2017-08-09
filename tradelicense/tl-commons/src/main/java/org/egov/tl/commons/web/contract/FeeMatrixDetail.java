package org.egov.tl.commons.web.contract;

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

	@NotNull
	private Long feeMatrixId = null;

	@NotNull
	private Long uomFrom = null;

	@NotNull
	private Long uomTo = null;

	@NotNull
	private Double amount = null;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}