package org.egov.egf.instrument.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.instrument.domain.model.Instrument;
import org.egov.egf.instrument.domain.model.InstrumentStatus;
import org.egov.egf.instrument.domain.model.InstrumentType;
import org.egov.egf.instrument.domain.model.SurrenderReason;
import org.egov.egf.instrument.domain.model.TransactionType;
import org.egov.egf.master.web.contract.BankAccountContract;
import org.egov.egf.master.web.contract.BankContract;

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
	private String instrumentStatusId;
	private String transactionTypeId;
	private String payee;
	private String drawer;
	private String surrendarReasonId;
	private String serialNo;

	public Instrument toDomain() {
		Instrument instrument = new Instrument();
		super.toDomain(instrument);
		instrument.setId(this.id);
		instrument.setTransactionNumber(this.transactionNumber);
		instrument.setTransactionDate(this.transactionDate);
		instrument.setAmount(this.amount);
		instrument.setInstrumentType(InstrumentType.builder().id(instrumentTypeId).build());
		instrument.setBank(BankContract.builder().id(bankId).build());
		instrument.setBranchName(this.branchName);
		instrument.setBankAccount(BankAccountContract.builder().id(bankAccountId).build());
		instrument.setInstrumentStatus(InstrumentStatus.builder().id(instrumentStatusId).build());
	//	instrument.setTransactionType(transactionTypeId.toString());
		instrument.setPayee(this.payee);
		instrument.setDrawer(this.drawer);
		instrument.setSurrendarReason(SurrenderReason.builder().id(surrendarReasonId).build());
		instrument.setSerialNo(this.serialNo);
		return instrument;
	}

	public InstrumentEntity toEntity(Instrument instrument) {
		super.toEntity((Auditable) instrument);
		this.id = instrument.getId();
		this.transactionNumber = instrument.getTransactionNumber();
		this.transactionDate = instrument.getTransactionDate();
		this.amount = instrument.getAmount();
		this.instrumentTypeId = instrument.getInstrumentType() != null ? instrument.getInstrumentType().getId() : null;
		this.bankId = instrument.getBank() != null ? instrument.getBank().getId() : null;
		this.branchName = instrument.getBranchName();
		this.bankAccountId = instrument.getBankAccount() != null ? instrument.getBankAccount().getId() : null;
		this.instrumentStatusId = instrument.getInstrumentStatus() != null ? instrument.getInstrumentStatus().getId()
				: null;
		/*this.transactionTypeId = instrument.getTransactionType() != null ? instrument.getTransactionType().getId()
				: null;*/
		this.payee = instrument.getPayee();
		this.drawer = instrument.getDrawer();
		this.surrendarReasonId = instrument.getSurrendarReason() != null ? instrument.getSurrendarReason().getId()
				: null;
		this.serialNo = instrument.getSerialNo();
		return this;
	}

}
