package org.egov.egf.voucher.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.AccountDetailKeyContract;
import org.egov.egf.master.web.contract.AccountDetailTypeContract;
import org.egov.egf.voucher.domain.model.SubLedger;

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
public class SubLedgerEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_subledger";
    private String id;
    private String ledgerId;
    private String accountDetailTypeId;
    private String accountDetailKeyId;
    private BigDecimal amount;

    public SubLedger toDomain() {
        SubLedger subLedger = new SubLedger();
        super.toDomain(subLedger);
        subLedger.setId(this.id);
        subLedger.setAccountDetailType(AccountDetailTypeContract.builder().id(accountDetailTypeId).build());
        subLedger.setAccountDetailKey(AccountDetailKeyContract.builder().id(accountDetailKeyId).build());
        subLedger.setAmount(this.amount);
        return subLedger;
    }

    public SubLedgerEntity toEntity(SubLedger subLedger) {
        super.toEntity((Auditable) subLedger);
        this.id = subLedger.getId();
        this.accountDetailTypeId = subLedger.getAccountDetailType() != null
                ? subLedger.getAccountDetailType().getId() : null;
        this.accountDetailKeyId = subLedger.getAccountDetailKey() != null
                ? subLedger.getAccountDetailKey().getId() : null;
        this.amount = subLedger.getAmount();
        return this;
    }

}
