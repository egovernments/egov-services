package org.egov.egf.budget.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.EstimationType;
import org.egov.egf.master.web.contract.FinancialStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class BudgetEntity extends AuditableEntity {
    public static final String TABLE_NAME = "egf_budget";
    private String id;
    private String name;
    private String financialYearId;
    private String estimationType;
    private String parentId;
    private Boolean active;
    private Boolean primaryBudget;
    private String referenceBudgetId;
    private String statusId;
    private String documentNumber;
    private String description;
    private String materializedPath;

    public Budget toDomain() {
        final Budget budget = new Budget();
        super.toDomain(budget);
        budget.setId(id);
        budget.setName(name);
        budget.setFinancialYear(FinancialYearContract.builder().id(financialYearId).build());
        budget.setEstimationType(EstimationType.valueOf(estimationType));
        budget.setParent(Budget.builder().id(parentId).build());
        budget.setActive(active);
        budget.setPrimaryBudget(primaryBudget);
        budget.setReferenceBudget(Budget.builder().id(referenceBudgetId).build());
        budget.setStatus(FinancialStatusContract.builder().id(statusId).build());
        budget.setDocumentNumber(documentNumber);
        budget.setDescription(description);
        budget.setMaterializedPath(materializedPath);
        return budget;
    }

    public BudgetEntity toEntity(final Budget budget) {
        super.toEntity(budget);
        id = budget.getId();
        name = budget.getName();
        financialYearId = budget.getFinancialYear() != null ? budget.getFinancialYear().getId() : null;
        estimationType = budget.getEstimationType() != null ? budget.getEstimationType().name() : null;
        parentId = budget.getParent() != null ? budget.getParent().getId() : null;
        active = budget.getActive();
        primaryBudget = budget.getPrimaryBudget();
        referenceBudgetId = budget.getReferenceBudget() != null ? budget.getReferenceBudget().getId() : null;
        statusId = budget.getStatus() != null ? budget.getStatus().getId() : null;
        documentNumber = budget.getDocumentNumber();
        description = budget.getDescription();
        materializedPath = budget.getMaterializedPath();
        return this;
    }

}
