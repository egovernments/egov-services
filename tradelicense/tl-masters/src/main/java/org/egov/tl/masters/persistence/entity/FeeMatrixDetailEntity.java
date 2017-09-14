package org.egov.tl.masters.persistence.entity;

import org.egov.tl.commons.web.contract.AuditDetails;
import org.egov.tl.masters.domain.model.FeeMatrixDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeMatrixDetailEntity {

	public static final String TABLE_NAME = "egtl_fee_matrix_details";
	public static final String SEQUENCE_NAME = "seq_egtl_fee_matrix_details";

	private Long id;

	private Long feeMatrixId;

	private String tenantId;

	private Long uomFrom;

	private Long uomTo;

	private Double amount;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public FeeMatrixDetail toDomain() {
		FeeMatrixDetail feeMatrix = new FeeMatrixDetail();

		feeMatrix.setId(this.id);
		feeMatrix.setTenantId(this.tenantId);
		feeMatrix.setAmount(this.amount);
		feeMatrix.setFeeMatrixId(this.feeMatrixId);
		feeMatrix.setUomFrom(this.uomFrom);
		feeMatrix.setUomTo(this.uomTo);
		AuditDetails auditDetails = new AuditDetails();
		auditDetails.setCreatedBy(this.createdBy);
		auditDetails.setCreatedTime(this.createdTime);
		auditDetails.setLastModifiedBy(this.lastModifiedBy);
		auditDetails.setLastModifiedTime(this.lastModifiedTime);
		feeMatrix.setAuditDetails(auditDetails);

		return feeMatrix;
	}

	public FeeMatrixDetailEntity toEntity(FeeMatrixDetail feeMatrixDetail) {
		this.id = feeMatrixDetail.getId();
		this.tenantId = feeMatrixDetail.getTenantId();
		this.feeMatrixId = feeMatrixDetail.getFeeMatrixId();
		this.uomFrom = feeMatrixDetail.getUomFrom();
		this.uomTo = feeMatrixDetail.getUomTo();
		this.amount = feeMatrixDetail.getAmount();
		this.createdBy = feeMatrixDetail.getAuditDetails().getCreatedBy();
		this.lastModifiedBy = feeMatrixDetail.getAuditDetails().getLastModifiedBy();
		this.createdTime = feeMatrixDetail.getAuditDetails().getCreatedTime();
		this.lastModifiedTime = feeMatrixDetail.getAuditDetails().getLastModifiedTime();

		return this;
	}
}