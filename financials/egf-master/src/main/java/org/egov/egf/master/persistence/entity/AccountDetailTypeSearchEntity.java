package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.AccountDetailType;
import org.egov.egf.master.domain.model.AccountDetailTypeSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountDetailTypeSearchEntity extends AccountDetailTypeEntity {
	private Integer pageSize;
	private Integer offSet;

	@Override
	public AccountDetailType toDomain() {
		AccountDetailType accountDetailType = new AccountDetailType();
		super.toDomain(accountDetailType);
		return accountDetailType;
	}

	public AccountDetailTypeSearchEntity toEntity(AccountDetailTypeSearch accountDetailTypeSearch) {
		super.toEntity((AccountDetailType) accountDetailTypeSearch);
		this.pageSize = accountDetailTypeSearch.getPageSize();
		this.offSet = accountDetailTypeSearch.getOffSet();
		return this;
	}

}