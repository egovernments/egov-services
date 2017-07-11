package org.egov.egf.budget.persistence.entity;

import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDetailSearchEntity extends BudgetDetailEntity {
	private Integer pageSize;
	private Integer offset;

	@Override
	public BudgetDetail toDomain() {
		BudgetDetail budgetDetail = new BudgetDetail();
		super.toDomain(budgetDetail);
		return budgetDetail;
	}

	public BudgetDetailSearchEntity toEntity(BudgetDetailSearch budgetDetailSearch) {
		super.toEntity(budgetDetailSearch);
		this.pageSize = budgetDetailSearch.getPageSize();
		this.offset = budgetDetailSearch.getOffset();
		return this;
	}

}