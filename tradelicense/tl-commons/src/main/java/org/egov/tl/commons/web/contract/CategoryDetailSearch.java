package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;

import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * CategoryDetail
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDetailSearch {

	private Long id = null;

	@JsonProperty("categoryId")
	private Long categoryId;

	@JsonProperty("feeType")
	@NotNull(message = "{error.feeType.null}")
	private FeeTypeEnum feeType;

	@JsonProperty("rateType")
	@NotNull(message = "{error.rateType.null}")
	private RateTypeEnum rateType = null;

	@JsonProperty("uomId")
	@NotNull(message = "{error.uomid.null}")
	private Long uomId = null;

	@JsonProperty("uomName")
	private String uomName;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}