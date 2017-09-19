package org.egov.egf.bill.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.egov.egf.bill.domain.model.BillRegister;
import org.egov.egf.bill.domain.model.BillRegisterSearch;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillRegisterSearchEntity extends BillRegisterEntity {
    private String ids;
    private String sortBy;
    private Integer pageSize;
    private Integer offset;
	private String glcode;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
	private String accountDetailTypeId;
	private String accountDetailKeyId;
	private BigDecimal subLedgerAmount;
	private String types;
	private String names;
	private String billNumbers;
	private String statuses;
	private Date billFromDate;
	private Date billToDate;

    public BillRegister toDomain() {
	BillRegister billRegister = new BillRegister();
	super.toDomain(billRegister);
	return billRegister;
    }

    public BillRegisterSearchEntity toEntity(BillRegisterSearch billRegisterSearch) {
	super.toEntity((BillRegister) billRegisterSearch);
	this.pageSize = billRegisterSearch.getPageSize();
	this.offset = billRegisterSearch.getOffset();
	this.sortBy = billRegisterSearch.getSortBy();
	this.ids = billRegisterSearch.getIds();
	this.glcode = billRegisterSearch.getGlcode();
	this.debitAmount = billRegisterSearch.getDebitAmount();
	this.creditAmount = billRegisterSearch.getCreditAmount();
	this.accountDetailTypeId = billRegisterSearch.getAccountDetailTypeId();
	this.accountDetailKeyId = billRegisterSearch.getAccountDetailKeyId();
	this.subLedgerAmount = billRegisterSearch.getSubLedgerAmount();
	this.types = billRegisterSearch.getTypes();
	this.names = billRegisterSearch.getNames();
	this.billNumbers = billRegisterSearch.getBillNumbers();
	this.statuses = billRegisterSearch.getStatuses();
	this.billFromDate = billRegisterSearch.getBillFromDate();
	this.billToDate = billRegisterSearch.getBillToDate();
	return this;
    }
}