package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.Bank;
import org.egov.egf.master.domain.model.BankSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BankSearchEntity extends BankEntity {
	private Integer pageSize;
	private Integer offset;

	@Override
	public Bank toDomain() {
		Bank bank = new Bank();
		super.toDomain(bank);
		return bank;
	}

	public BankSearchEntity toEntity(BankSearch bankSearch) {
		super.toEntity(bankSearch);
		this.pageSize = bankSearch.getPageSize();
		this.offset = bankSearch.getOffset();
		return this;
	}

}