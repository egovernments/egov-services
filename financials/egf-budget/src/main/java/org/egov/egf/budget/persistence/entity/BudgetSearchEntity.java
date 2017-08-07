package org.egov.egf.budget.persistence.entity;

import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetSearchEntity extends BudgetEntity {
    private Integer pageSize;
    private Integer offset;
    private String sortBy;

    @Override
    public Budget toDomain() {
        final Budget budget = new Budget();
        super.toDomain(budget);
        return budget;
    }

    public BudgetSearchEntity toEntity(final BudgetSearch budgetSearch) {
        super.toEntity(budgetSearch);
        pageSize = budgetSearch.getPageSize();
        offset = budgetSearch.getOffset();
        sortBy = budgetSearch.getSortBy();
        return this;
    }

}