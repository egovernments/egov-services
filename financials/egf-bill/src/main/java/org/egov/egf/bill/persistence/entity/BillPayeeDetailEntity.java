package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.bill.domain.model.BillPayeeDetail;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillPayeeDetailEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_billpayeedetail";
	public static final String SEQUENCE_NAME = "seq_egf_billpayeedetail";
    private String id;
    private String billDetailId;
    private String accountDetailTypeId;
    private String accountDetailKeyId;
    private BigDecimal amount;

    public BillPayeeDetail toDomain() {
	BillPayeeDetail billPayeeDetail = new BillPayeeDetail();
	super.toDomain(billPayeeDetail);
	billPayeeDetail.setId(this.id);
	billPayeeDetail.setAccountDetailType(AccountDetailTypeContract.builder().id(accountDetailTypeId).build());
	billPayeeDetail.setAccountDetailKey(AccountDetailKeyContract.builder().id(accountDetailKeyId).build());
	billPayeeDetail.setAmount(this.amount);
	return billPayeeDetail;
    }

    public BillPayeeDetailEntity toEntity(BillPayeeDetail billPayeeDetail) {
	super.toEntity((Auditable) billPayeeDetail);
	this.id = billPayeeDetail.getId();
	this.accountDetailTypeId = billPayeeDetail.getAccountDetailType() != null
		? billPayeeDetail.getAccountDetailType().getId() : null;
	this.accountDetailKeyId = billPayeeDetail.getAccountDetailKey() != null
		? billPayeeDetail.getAccountDetailKey().getId() : null;
	this.amount = billPayeeDetail.getAmount();
	return this;
    }
}
