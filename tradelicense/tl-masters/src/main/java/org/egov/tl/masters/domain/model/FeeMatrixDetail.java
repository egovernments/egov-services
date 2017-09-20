package org.egov.tl.masters.domain.model;

import org.egov.tl.commons.web.contract.AuditDetails;

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
public class FeeMatrixDetail {

	private Long id = null;

	@JsonProperty("feeMatrixId")

	private Long feeMatrixId = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	@JsonProperty("uomFrom")
	private Long uomFrom = null;

	@JsonProperty("uomTo")
	private Long uomTo = null;

	@JsonProperty("amount")
	private Double amount = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}