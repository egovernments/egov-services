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
    private String sortBy;

    @Override
    public BudgetDetail toDomain() {
        final BudgetDetail budgetDetail = new BudgetDetail();
        super.toDomain(budgetDetail);
        return budgetDetail;
    }

    public BudgetDetailSearchEntity toEntity(final BudgetDetailSearch budgetDetailSearch) {
        super.toEntity(budgetDetailSearch);
        pageSize = budgetDetailSearch.getPageSize();
        offset = budgetDetailSearch.getOffset();
        sortBy = budgetDetailSearch.getSortBy();
        return this;
    }

}