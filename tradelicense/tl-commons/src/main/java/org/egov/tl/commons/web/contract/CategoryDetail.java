package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

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
public class CategoryDetail {

	private Long id = null;

	@JsonProperty("category")
	private String category;
	
	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@JsonProperty("feeType")
	@NotNull(message = "{error.feeType.null}")
	private FeeTypeEnum feeType;

	@JsonProperty("rateType")
	@NotNull(message = "{error.rateType.null}")
	private RateTypeEnum rateType = null;

	@JsonProperty("uom")
	@NotEmpty(message = "{error.uom.null}")
	private String uom = null;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}