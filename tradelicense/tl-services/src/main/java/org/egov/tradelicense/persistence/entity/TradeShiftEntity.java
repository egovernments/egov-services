package org.egov.tradelicense.persistence.entity;

import org.egov.tradelicense.domain.model.AuditDetails;
import org.egov.tradelicense.domain.model.TradeShift;
import org.egov.tradelicense.domain.model.TradeShiftSearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class TradeShiftEntity {

	public static final String TABLE_NAME = "egtl_trade_shift";
	public static final String SEQUENCE_NAME = "seq_egtl_trade_shift";

	private Long id;

	private String tenantId;

	private Long licenseId;

	private Integer shiftNo;

	private Long fromTime;

	private Long toTime;

	private String remarks;

	private String createdBy;

	private String lastModifiedBy;

	private Long createdTime;

	private Long lastModifiedTime;

	public TradeShift toDomain() {

		TradeShift tradeShift = new TradeShift();

		AuditDetails auditDetails = new AuditDetails();

		tradeShift.setId(this.id);

		tradeShift.setTenantId(this.tenantId);

		tradeShift.setLicenseId(this.licenseId);

		tradeShift.setShiftNo(this.shiftNo);

		tradeShift.setFromTime(this.fromTime);

		tradeShift.setToTime(this.toTime);

		tradeShift.setRemarks(this.remarks);

		auditDetails.setCreatedBy(this.createdBy);

		auditDetails.setCreatedTime(this.createdTime);

		auditDetails.setLastModifiedBy(this.lastModifiedBy);

		auditDetails.setLastModifiedTime(this.lastModifiedTime);

		tradeShift.setAuditDetails(auditDetails);

		return tradeShift;

	}

	public TradeShiftEntity toEntity(TradeShift tradeShift) {

		AuditDetails auditDetails = tradeShift.getAuditDetails();

		this.id = tradeShift.getId();

		this.tenantId = tradeShift.getTenantId();

		this.licenseId = tradeShift.getLicenseId();

		this.shiftNo = tradeShift.getShiftNo();

		this.fromTime = tradeShift.getFromTime();

		this.toTime = tradeShift.getToTime();

		this.remarks = tradeShift.getRemarks();

		this.createdBy = (auditDetails == null) ? null : auditDetails.getCreatedBy();

		this.lastModifiedBy = (auditDetails == null) ? null : auditDetails.getLastModifiedBy();

		this.createdTime = (auditDetails == null) ? null : auditDetails.getCreatedTime();

		this.lastModifiedTime = (auditDetails == null) ? null : auditDetails.getLastModifiedTime();

		return this;
	}
	
	public TradeShiftEntity toSearchEntity(TradeShiftSearch tradeShift) {

		this.id = tradeShift.getId();

		this.tenantId = tradeShift.getTenantId();

		this.licenseId = tradeShift.getLicenseId();

		return this;
	}
}