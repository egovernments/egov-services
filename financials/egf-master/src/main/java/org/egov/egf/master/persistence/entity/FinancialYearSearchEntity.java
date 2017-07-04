package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.FinancialYear;
import org.egov.egf.master.domain.model.FinancialYearSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class FinancialYearSearchEntity extends FinancialYearEntity {
	private Integer pageSize;
	private Integer offset;

	@Override
	public FinancialYear toDomain() {
		FinancialYear financialYear = new FinancialYear();
		super.toDomain(financialYear);
		return financialYear;
	}

	public FinancialYearSearchEntity toEntity(FinancialYearSearch financialYearSearch) {
		super.toEntity((FinancialYear) financialYearSearch);
		this.pageSize = financialYearSearch.getPageSize();
		this.offset = financialYearSearch.getOffset();
		return this;
	}

}