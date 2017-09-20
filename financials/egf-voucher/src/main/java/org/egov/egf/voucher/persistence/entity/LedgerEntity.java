package org.egov.egf.voucher.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.master.web.contract.ChartOfAccountContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.voucher.domain.model.Ledger;

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
public class LedgerEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_ledger";
    private String id;
    private String voucherId;
    private Integer orderId;
    private String chartOfAccountId;
    private String glcode;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String functionId;

    public Ledger toDomain() {
        Ledger ledger = new Ledger();
        super.toDomain(ledger);
        ledger.setId(this.id);
        ledger.setOrderId(this.orderId);
        ledger.setChartOfAccount(ChartOfAccountContract.builder().id(chartOfAccountId).build());
        ledger.setGlcode(this.glcode);
        ledger.setDebitAmount(this.debitAmount);
        ledger.setCreditAmount(this.creditAmount);
        ledger.setFunction(FunctionContract.builder().id(functionId).build());
        return ledger;
    }

    public LedgerEntity toEntity(Ledger ledger) {
        super.toEntity((Auditable) ledger);
        this.id = ledger.getId();
        this.orderId = ledger.getOrderId();
        this.chartOfAccountId = ledger.getChartOfAccount() != null ? ledger.getChartOfAccount().getId() : null;
        this.glcode = ledger.getGlcode();
        this.debitAmount = ledger.getDebitAmount();
        this.creditAmount = ledger.getCreditAmount();
        this.functionId = ledger.getFunction() != null ? ledger.getFunction().getId() : null;
        return this;
    }

}
