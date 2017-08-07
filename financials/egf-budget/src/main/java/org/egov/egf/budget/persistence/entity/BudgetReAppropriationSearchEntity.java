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
        final BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
        super.toDomain(budgetReAppropriation);
        return budgetReAppropriation;
    }

    public BudgetReAppropriationSearchEntity toEntity(final BudgetReAppropriationSearch budgetReAppropriationSearch) {
        super.toEntity(budgetReAppropriationSearch);
        pageSize = budgetReAppropriationSearch.getPageSize();
        offset = budgetReAppropriationSearch.getOffset();
        sortBy = budgetReAppropriationSearch.getSortBy();
        return this;
    }

}