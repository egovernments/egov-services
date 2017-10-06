package org.egov.tl.masters.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearchCriteria {

	private Integer[] ids;

	private String tenantId;

	private String applicationType;

	private String category;

	private String businessNature;

	private String subCategory;

	private String financialYear;

	private String feeType;

	private String categoryName;

	private Long effectiveFrom;

	private String subCategoryName;

	private Integer pageSize;

	private Integer offSet;

	private Boolean fallBack;

}
