package org.egov.tl.masters.domain.model;

import java.util.List;

import javax.validation.Valid;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class FeeMatrix {

	private Long id = null;

	@JsonProperty("tenantId")
	private String tenantId = null;

	private ApplicationTypeEnum applicationType = null;

	private String category;

	@JsonProperty("businessNature")
	private BusinessNatureEnum businessNature;

	@JsonProperty("feeType")
	private FeeTypeEnum feeType;

	@JsonProperty("subCategory")
	private String subCategory = null;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		FeeMatrix feeMatrix = (FeeMatrix) obj;
		if (applicationType != feeMatrix.applicationType)
			return false;
		if (businessNature != feeMatrix.businessNature)
			return false;
		if (category == null) {
			if (feeMatrix.category != null)
				return false;
		} else if (!category.equals(feeMatrix.category))
			return false;
//		if (effectiveFrom == null) {
//			if (feeMatrix.effectiveFrom != null)
//				return false;
//		} else if (!effectiveFrom.equals(feeMatrix.effectiveFrom))
//			return false;
//		if (effectiveTo == null) {
//			if (feeMatrix.effectiveTo != null)
//				return false;
//		} else if (!effectiveTo.equals(feeMatrix.effectiveTo))
//			return false;
		if (feeType != feeMatrix.feeType)
			return false;
		if (financialYear == null) {
			if (feeMatrix.financialYear != null)
				return false;
		} else if (!financialYear.equals(feeMatrix.financialYear))
			return false;
		if (id == null) {
			if (feeMatrix.id != null)
				return false;
		} else if (!id.equals(feeMatrix.id))
			return false;
		if (subCategory == null) {
			if (feeMatrix.subCategory != null)
				return false;
		} else if (!subCategory.equals(feeMatrix.subCategory))
			return false;
		if (tenantId == null) {
			if (feeMatrix.tenantId != null)
				return false;
		} else if (!tenantId.equals(feeMatrix.tenantId))
			return false;

		return true;
	}

}