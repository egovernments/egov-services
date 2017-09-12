package org.egov.tl.commons.web.contract;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
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
public class FeeMatrix {

	private Long id = null;

	@JsonProperty("tenantId")
	@Pattern(regexp = ".*[^ ].*", message = "{error.tenantId.emptyspaces}")
	@NotEmpty(message = "{error.tenantId.empty}")
	@Length(min = 4, max = 128, message = "{error.tenantId.empty}")
	private String tenantId = null;

	@NotNull(message = "{error.applicationType.null}")
	private ApplicationTypeEnum applicationType = null;

	@NotNull(message = "{error.categoryId.null}")
	private Long categoryId;

	@JsonProperty("businessNature")
	@NotNull(message = "{error.businessNature.null}")
	private BusinessNatureEnum businessNature;

	@JsonProperty("subCategoryId")
	@NotNull(message = "{error.subcategoryId.null}")
	private Long subCategoryId = null;

	@Pattern(regexp = ".*[^ ].*", message = "{error.financialYear.emptyspaces}")
	@JsonProperty("financialYear")
	@NotEmpty(message = "{error.financialYear.empty}")
	private String financialYear = null;

	@JsonProperty("effectiveFrom")
	private Long effectiveFrom = null;

	@JsonProperty("effectiveTo")
	private Long effectiveTo = null;

	@Valid
	private List<FeeMatrixDetail> feeMatrixDetails;

	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}