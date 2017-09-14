package org.egov.tl.masters.domain.model;

import java.util.List;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.commons.web.contract.FeeMatrixDetailContract;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.enums.RateTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeMatrixSearch {

	private Long id;

	private String tenantId;

	private ApplicationTypeEnum applicationType=null;

	private Long categoryId;

	private BusinessNatureEnum businessNature=null;

	private Long subCategoryId;

	private String financialYear;

	private Long effectiveFrom;

	private Long effectiveTo;

	private FeeTypeEnum feeType=null;

	private RateTypeEnum rateType;

	private String categoryName;

	private String subCategoryName;

	private String uomName;

	private Long uomId;

	private List<FeeMatrixDetail> feeMatixDetails;

	private AuditDetails auidtDetails;

}
