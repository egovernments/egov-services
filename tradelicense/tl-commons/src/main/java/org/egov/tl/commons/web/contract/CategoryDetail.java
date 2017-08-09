package org.egov.tl.commons.web.contract;

import org.egov.tl.commons.web.contract.enums.FeeTypeEnum;
import org.egov.tl.commons.web.contract.enums.RateTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * CategoryDetail
 * 
 * @author Pavan Kumar Kamma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetail {

	private Long id = null;

	private Long categoryId = null;

	private FeeTypeEnum feeType = null;

	private RateTypeEnum rateType = null;

	private Long uomId = null;
	
	@JsonProperty("auditDetails")
	private AuditDetails auditDetails = null;
}