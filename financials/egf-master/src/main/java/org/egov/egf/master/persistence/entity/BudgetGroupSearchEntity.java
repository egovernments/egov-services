package org.egov.egf.master.persistence.entity;

import org.egov.egf.master.domain.model.BudgetGroup;
import org.egov.egf.master.domain.model.BudgetGroupSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetGroupSearchEntity extends BudgetGroupEntity {
	private Integer pageSize;
	private Integer offSet;

	@Override
	public BudgetGroup toDomain() {
		BudgetGroup budgetGroup = new BudgetGroup();
		super.toDomain(budgetGroup);
		return budgetGroup;
	}

	public BudgetGroupSearchEntity toEntity(BudgetGroupSearch budgetGroupSearch) {
		super.toEntity((BudgetGroup) budgetGroupSearch);
		this.pageSize = budgetGroupSearch.getPageSize();
		this.offSet = budgetGroupSearch.getOffSet();
		return this;
	}

}