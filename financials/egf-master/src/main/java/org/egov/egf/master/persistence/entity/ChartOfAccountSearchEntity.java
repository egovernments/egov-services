package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.ChartOfAccount;
import org.egov.egf.master.domain.model.ChartOfAccountSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ChartOfAccountSearchEntity extends ChartOfAccountEntity {
	private Integer pageSize;
	private Integer offSet;

	@Override
	public ChartOfAccount toDomain() {
		ChartOfAccount chartOfAccount = new ChartOfAccount();
		super.toDomain(chartOfAccount);
		return chartOfAccount;
	}

	public ChartOfAccountSearchEntity toEntity(ChartOfAccountSearch chartOfAccountSearch) {
		super.toEntity((ChartOfAccount) chartOfAccountSearch);
		this.pageSize = chartOfAccountSearch.getPageSize();
		this.offSet = chartOfAccountSearch.getOffSet();
		return this;
	}

}