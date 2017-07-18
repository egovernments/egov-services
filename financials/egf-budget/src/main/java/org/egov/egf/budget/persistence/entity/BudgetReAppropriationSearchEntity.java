package org.egov.egf.budget.persistence.entity;

import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetReAppropriationSearchEntity extends BudgetReAppropriationEntity {
	private Integer pageSize;
	private Integer offset;
	private String sortBy;

	@Override
	public BudgetReAppropriation toDomain() {
		BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
		super.toDomain(budgetReAppropriation);
		return budgetReAppropriation;
	}

	public BudgetReAppropriationSearchEntity toEntity(BudgetReAppropriationSearch budgetReAppropriationSearch) {
		super.toEntity((BudgetReAppropriation) budgetReAppropriationSearch);
		this.pageSize = budgetReAppropriationSearch.getPageSize();
		this.offset = budgetReAppropriationSearch.getOffset();
		this.sortBy = budgetReAppropriationSearch.getSortBy();
		return this;
	}

}