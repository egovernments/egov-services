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

	private Long categoryId;

	private String businessNature;

	private Long subCategoryId;

	private String financialYear;

	private String feeType;

	private String categoryName;

	private Long effectiveFrom;

	private String subCategoryName;

	private Integer pageSize;

	private Integer offSet;

	private Boolean fallBack;

}
