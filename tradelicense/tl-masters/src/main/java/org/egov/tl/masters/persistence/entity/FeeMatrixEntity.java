package org.egov.tl.masters.persistence.entity;

import java.sql.Timestamp;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.masters.domain.enums.ApplicationTypeEnum;
import org.egov.tl.masters.domain.enums.BusinessNatureEnum;
import org.egov.tl.masters.domain.enums.FeeTypeEnum;
import org.egov.tl.masters.domain.model.FeeMatrix;
import org.egov.tl.masters.domain.model.FeeMatrixSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class describe the set of fields contained in a Trade license
 * DocumentType
 * 
 * @author Shubham pratap Singh
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeeMatrixEntity {

	public static final String TABLE_NAME = "egtl_mstr_fee_matrix";
	public static final String SEQUENCE_NAME = "seq_egtl_mstr_fee_matrix";

	private Long id;

	private String tenantId;

	private String applicationType;

	private Long categoryId;

	private String businessNature;

	private Long subCategoryId;

	private String financialYear;

	private Timestamp effectiveFrom;

	private Timestamp effectiveTo;

	private String feeType;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public FeeMatrix toDomain() {
		FeeMatrix feeMatrix = new FeeMatrix();

		feeMatrix.setId(this.id);
		feeMatrix.setTenantId(this.tenantId);
		feeMatrix.setApplicationType(
				(this.applicationType == null ? null : ApplicationTypeEnum.fromValue(this.applicationType)));
		feeMatrix.setCategoryId(this.categoryId);
		feeMatrix.setBusinessNature(
				this.businessNature == null ? null : BusinessNatureEnum.fromValue(this.businessNature));
		feeMatrix.setCategoryId(this.categoryId);
		feeMatrix.setSubCategoryId(this.subCategoryId);
		feeMatrix.setFinancialYear(this.financialYear);
		feeMatrix.setEffectiveFrom(this.effectiveFrom.getTime());
		feeMatrix.setEffectiveTo(this.effectiveTo == null ? null : this.effectiveTo.getTime());
		feeMatrix.setFeeType(this.feeType == null ? null : FeeTypeEnum.fromValue(this.feeType));
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(createdBy);
		auditDetails.setCreatedTime(createdTime);
		auditDetails.setLastModifiedBy(lastModifiedBy);
		auditDetails.setLastModifiedTime(lastModifiedTime);
		feeMatrix.setAuditDetails(auditDetails);
		return feeMatrix;
	}

	public FeeMatrixEntity toEntity(FeeMatrix feeMatrix) {
		this.id = feeMatrix.getId();
		this.tenantId = feeMatrix.getTenantId();
		this.applicationType = (feeMatrix.getApplicationType() == null ? null
				: feeMatrix.getApplicationType().toString());
		this.categoryId = feeMatrix.getCategoryId();
		this.businessNature = (feeMatrix.getBusinessNature() == null ? null : feeMatrix.getBusinessNature().toString());
		this.subCategoryId = feeMatrix.getSubCategoryId();
		this.financialYear = feeMatrix.getFinancialYear();
		this.effectiveFrom = new Timestamp(feeMatrix.getEffectiveFrom());
		this.effectiveTo = feeMatrix.getEffectiveTo() == null ? null : new Timestamp(feeMatrix.getEffectiveTo());
		this.feeType = (feeMatrix.getFeeType() == null ? null : feeMatrix.getFeeType().toString());
		if (feeMatrix.getAuditDetails() != null) {
			this.createdBy = feeMatrix.getAuditDetails().getCreatedBy();
			this.lastModifiedBy = feeMatrix.getAuditDetails().getLastModifiedBy();
			this.lastModifiedTime = feeMatrix.getAuditDetails().getLastModifiedTime();
			this.createdTime = feeMatrix.getAuditDetails().getCreatedTime();
		}

		return this;
	}

	public FeeMatrixSearch toSearchDomain(FeeMatrixEntity feeMatrixEntity) {
		FeeMatrixSearch feeMatrix = new FeeMatrixSearch();

		feeMatrix.setId(feeMatrixEntity.getId());
		feeMatrix.setTenantId(feeMatrixEntity.getTenantId());
		feeMatrix.setApplicationType(feeMatrixEntity.getApplicationType());

		feeMatrix.setCategoryId(feeMatrixEntity.getCategoryId());
		feeMatrix.setBusinessNature(feeMatrixEntity.getBusinessNature());
		feeMatrix.setSubCategoryId(feeMatrixEntity.getSubCategoryId());
		feeMatrix.setFinancialYear(feeMatrixEntity.getFinancialYear());
		feeMatrix.setEffectiveFrom(feeMatrixEntity.getEffectiveFrom().getTime());
		feeMatrix.setEffectiveTo(
				feeMatrixEntity.getEffectiveTo() == null ? null : feeMatrixEntity.getEffectiveTo().getTime());
		feeMatrix.setFeeType(feeMatrixEntity.getFeeType());
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(feeMatrixEntity.getCreatedBy());
		auditDetails.setCreatedTime(feeMatrixEntity.getCreatedTime());
		auditDetails.setLastModifiedBy(feeMatrixEntity.getLastModifiedBy());
		auditDetails.setLastModifiedTime(feeMatrixEntity.getLastModifiedTime());
		feeMatrix.setAuidtDetails(auditDetails);
		return feeMatrix;
	}
}