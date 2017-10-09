package org.egov.tl.masters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearchEntity {

	private Integer[] ids;

	private String tenantId;

	private String applicationType;

	private String category;

	private Long effectiveFrom;

	private String businessNature;

	private String subCategory;

	private String financialYear;

	private String feeType;

	private Integer pageSize;

	private Integer offSet;

	private Boolean fallBack;
}
