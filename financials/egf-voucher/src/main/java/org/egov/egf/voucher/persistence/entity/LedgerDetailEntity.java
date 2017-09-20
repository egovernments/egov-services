package org.egov.egf.voucher.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.voucher.domain.model.LedgerDetail;

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
public class LedgerDetailEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_ledgerdetail";
    private String id;
    private String ledgerId;
    private String accountDetailTypeId;
    private String accountDetailKeyId;
    private BigDecimal amount;

    public LedgerDetail toDomain() {
        LedgerDetail ledgerDetail = new LedgerDetail();
        super.toDomain(ledgerDetail);
        ledgerDetail.setId(this.id);
        ledgerDetail.setAccountDetailType(AccountDetailTypeContract.builder().id(accountDetailTypeId).build());
        ledgerDetail.setAccountDetailKey(AccountDetailKeyContract.builder().id(accountDetailKeyId).build());
        ledgerDetail.setAmount(this.amount);
        return ledgerDetail;
    }

    public LedgerDetailEntity toEntity(LedgerDetail ledgerDetail) {
        super.toEntity((Auditable) ledgerDetail);
        this.id = ledgerDetail.getId();
        this.accountDetailTypeId = ledgerDetail.getAccountDetailType() != null
                ? ledgerDetail.getAccountDetailType().getId() : null;
        this.accountDetailKeyId = ledgerDetail.getAccountDetailKey() != null
                ? ledgerDetail.getAccountDetailKey().getId() : null;
        this.amount = ledgerDetail.getAmount();
        return this;
    }

}
