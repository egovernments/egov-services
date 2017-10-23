package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.LicenseFeeDetailSearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LicenseFeeDetailSearchEntity {

	public static final String TABLE_NAME = "egtl_fee_details";
	public static final String SEQUENCE_NAME = "seq_egtl_fee_details";

	private Long id;

	private Long applicationId;

	private String tenantId;

	private String financialYear;

	private Double amount;

	private Boolean paid;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public LicenseFeeDetailSearch toDomain() {

		LicenseFeeDetailSearch licenseFeeDetail = new LicenseFeeDetailSearch();

		AuditDetails auditDetails = new AuditDetails();

		licenseFeeDetail.setId(this.id);

		licenseFeeDetail.setTenantId(this.tenantId);

		licenseFeeDetail.setApplicationId(this.applicationId);

		licenseFeeDetail.setFinancialYear(this.financialYear);

		licenseFeeDetail.setPaid(this.paid);

		licenseFeeDetail.setAmount(this.amount);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		licenseFeeDetail.setAuditDetails(auditDetails);

		return licenseFeeDetail;
	}

	public LicenseFeeDetailSearchEntity toEntity(LicenseFeeDetailSearch licenseFeeDetail) {

		AuditDetails auditDetails = licenseFeeDetail.getAuditDetails();

		this.amount = licenseFeeDetail.getAmount();

		this.id = licenseFeeDetail.getId();

		this.tenantId = licenseFeeDetail.getTenantId();

		this.applicationId = licenseFeeDetail.getApplicationId();

		this.financialYear = licenseFeeDetail.getFinancialYear();

		this.paid = licenseFeeDetail.getPaid();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;

	}
}