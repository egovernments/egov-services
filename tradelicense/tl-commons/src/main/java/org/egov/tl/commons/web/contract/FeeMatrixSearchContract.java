package org.egov.tl.commons.web.contract;

import java.util.List;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearchContract {

	private Long id;

	private String tenantId;

	@JsonProperty("applicationType")
	private ApplicationTypeEnum applicationType;

	@JsonProperty("category")
	private String category;

	@JsonProperty("businessNature")
	private BusinessNatureEnum businessNature;

	@JsonProperty("subCategory")
	private String subCategory;

	@JsonProperty("financialYear")
	private String financialYear;
	
	@JsonProperty("financialYearId")
	private String financialYearId;

	@JsonProperty("effectiveFrom")
	private Long effectiveFrom;

	@JsonProperty("effectiveTo")
	private Long effectiveTo;

	@JsonProperty("feeType")
	private String feeType;

	@JsonProperty("rateType")
	private String rateType;

	@JsonProperty("categoryName")
	private String categoryName;

	@JsonProperty("subCategoryName")
	private String subCategoryName;

	@JsonProperty("uomName")
	private String uomName;

	@JsonProperty("uom")
	private String uom;

	private List<FeeMatrixDetailContract> feeMatrixDetails;

	private AuditDetails auditDetails;

}
