package org.egov.tl.masters.domain.model;

import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearch {

	private Long id;

	private String tenantId;

	private String applicationType=null;

	private Long categoryId;

	private String businessNature=null;

	private Long subCategoryId;

	private String financialYear;

	private Long effectiveFrom;

	private Long effectiveTo;

	private String feeType=null;

	private String rateType;

	private String categoryName;

	private String subCategoryName;

	private String uomName;

	private Long uomId;

	private List<FeeMatrixDetail> feeMatixDetails;

	private AuditDetails auidtDetails;

}
