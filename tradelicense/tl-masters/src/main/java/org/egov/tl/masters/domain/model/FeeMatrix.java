package org.egov.tl.masters.domain.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;

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
	private String tenantId = null;

	private ApplicationTypeEnum applicationType = null;

	private Long categoryId;

	@JsonProperty("businessNature")
	private BusinessNatureEnum businessNature;

	@JsonProperty("feeType")
	private FeeTypeEnum feeType;

	@JsonProperty("subCategoryId")
	private Long subCategoryId = null;

	@JsonProperty("financialYear")
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