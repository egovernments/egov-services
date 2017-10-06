package org.egov.tl.commons.web.contract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.egov.tl.commons.web.contract.enums.ApplicationTypeEnum;
import org.egov.tl.commons.web.contract.enums.BusinessNatureEnum;
import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearchCriteriaContract {

	private Integer[] ids;

	@NotNull
	@Size(min = 4, max = 28)
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

	@JsonProperty("feeType")
	private FeeTypeEnum feeType;

	private boolean fallBack;

	private Integer pageSize;

	private Integer offSet;

}
