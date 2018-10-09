package org.egov.egf.instrument.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankContract;
import org.egov.egf.master.web.contract.FinancialStatusContract;

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
public class InstrumentEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_instrument";
    private String id;
    private String transactionNumber;
    private Date transactionDate;
    private BigDecimal amount;
    private String instrumentTypeId;
    private String bankId;
    private String branchName;
    private String bankAccountId;
    private String financialStatusId;
    private String remittanceVoucherId;
    private String transactionType;
    private String payee;
    private String drawer;
    private String surrenderReasonId;
    private String serialNo;
    private String payinSlipId;
    private BigDecimal reconciledAmount;
    private Date reconciledOn;

    public Instrument toDomain() {
        Instrument instrument = new Instrument();
        super.toDomain(instrument);
        instrument.setId(this.id);
        instrument.setTransactionNumber(this.transactionNumber);
        instrument.setTransactionDate(this.transactionDate);
        instrument.setAmount(this.amount);
        instrument.setInstrumentType(InstrumentType.builder().name(this.instrumentTypeId).build());
        instrument.setBank(BankContract.builder().id(bankId).build());
        instrument.setBranchName(this.branchName);
        instrument.setBankAccount(BankAccountContract.builder().accountNumber(bankAccountId).build());
        instrument.setFinancialStatus(FinancialStatusContract.builder().id(financialStatusId).build());
        instrument.setRemittanceVoucherId(this.remittanceVoucherId);
        if (this.transactionType != null)
            instrument.setTransactionType(TransactionType.valueOf(this.transactionType));
        instrument.setPayee(this.payee);
        instrument.setDrawer(this.drawer);
        instrument.setSurrenderReason(SurrenderReason.builder().id(surrenderReasonId).build());
        instrument.setSerialNo(this.serialNo);
        instrument.setPayinSlipId(this.payinSlipId);
        instrument.setReconciledAmount(this.reconciledAmount);
        instrument.setReconciledOn(this.reconciledOn);
        return instrument;
    }

    public InstrumentEntity toEntity(Instrument instrument) {
        super.toEntity(instrument);
        this.id = instrument.getId();
        this.transactionNumber = instrument.getTransactionNumber();
        this.transactionDate = instrument.getTransactionDate();
        this.amount = instrument.getAmount();
        this.instrumentTypeId = instrument.getInstrumentType() != null ? instrument.getInstrumentType().getName()
                : null;
        this.bankId = instrument.getBank() != null ? instrument.getBank().getId() : null;
        this.branchName = instrument.getBranchName();
        this.bankAccountId = instrument.getBankAccount() != null ? instrument.getBankAccount().getAccountNumber()
                : null;
        this.financialStatusId = instrument.getFinancialStatus() != null ? instrument.getFinancialStatus().getId()
                : null;
        this.remittanceVoucherId = instrument.getRemittanceVoucherId();
        this.transactionType = instrument.getTransactionType() != null ? instrument.getTransactionType().toString()
                : null;
        this.payee = instrument.getPayee();
        this.drawer = instrument.getDrawer();
        this.surrenderReasonId = instrument.getSurrenderReason() != null ? instrument.getSurrenderReason().getId()
                : null;
        this.serialNo = instrument.getSerialNo();
        this.payinSlipId = instrument.getPayinSlipId();
        this.reconciledAmount = instrument.getReconciledAmount();
        this.reconciledOn = instrument.getReconciledOn();
        return this;
    }

}
