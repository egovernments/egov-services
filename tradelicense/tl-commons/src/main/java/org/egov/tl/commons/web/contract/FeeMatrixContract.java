package org.egov.tl.commons.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in FeeMatrix
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeMatrixContract {

	private Long id;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	private ApplicationTypeEnum applicationType ;

	@NotNull(message = "{error.category.null}")
	private String category;

	@JsonProperty("feeType")
	@NotNull(message = "{error.feeType.null}")
	private FeeTypeEnum feeType;
	
	private BusinessNatureEnum businessNature;

	@JsonProperty("subCategory")
	@NotNull(message = "{error.subcategory.null}")
	private String subCategory = null;

	@Pattern(regexp = ".*[^ ].*", message = "{error.financialYear.emptyspaces}")
	@JsonProperty("financialYear")
	@NotEmpty(message = "{error.financialYear.empty}")
	private String financialYear = null;

	@JsonProperty("effectiveFrom")
	private Long effectiveFrom = null;

	@JsonProperty("effectiveTo")
	private Long effectiveTo = null;

	@Valid
	private List<FeeMatrixDetailContract> feeMatrixDetails;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}