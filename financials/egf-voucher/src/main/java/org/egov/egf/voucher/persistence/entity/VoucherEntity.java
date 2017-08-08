package org.egov.egf.voucher.persistence.entity;

import java.util.Date;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.voucher.domain.model.Voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class VoucherEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_voucher";
	public static final String SEQUENCE_NAME = "seq_egf_voucher";
	private String id;
	private String type;
	private String name;
	private String description;
	private String voucherNumber;
	private Date voucherDate;
	private String fundId;
	private String statusId;
	private String originalVoucherNumber;
	private String refVoucherNumber;
	private String moduleName;
	private String vouchermisId;

	public Voucher toDomain() {
		Voucher voucher = new Voucher();
		super.toDomain(voucher);
		voucher.setId(this.id);
		voucher.setType(this.type);
		voucher.setName(this.name);
		voucher.setDescription(this.description);
		voucher.setVoucherNumber(this.voucherNumber);
		voucher.setVoucherDate(this.voucherDate);
		voucher.setFund(FundContract.builder().id(fundId).build());
		voucher.setStatus(FinancialStatusContract.builder().id(statusId).build());
		voucher.setOriginalVoucherNumber(this.originalVoucherNumber);
		voucher.setRefVoucherNumber(this.refVoucherNumber);
		voucher.setModuleName(this.moduleName);
		//voucher.setVouchermis(Vouchermis.builder().id(vouchermisId).build());
		return voucher;
	}

	public VoucherEntity toEntity(Voucher voucher) {
		super.toEntity((Auditable) voucher);
		this.id = voucher.getId();
		this.type = voucher.getType();
		this.name = voucher.getName();
		this.description = voucher.getDescription();
		this.voucherNumber = voucher.getVoucherNumber();
		this.voucherDate = voucher.getVoucherDate();
		this.fundId = voucher.getFund() != null ? voucher.getFund().getId() : null;
		this.statusId = voucher.getStatus() != null ? voucher.getStatus().getId() : null;
		this.originalVoucherNumber = voucher.getOriginalVoucherNumber();
		this.refVoucherNumber = voucher.getRefVoucherNumber();
		this.moduleName = voucher.getModuleName();
		this.vouchermisId = voucher.getVouchermis() != null ? voucher.getVouchermis().getId() : null;
		return this;
	}

}
