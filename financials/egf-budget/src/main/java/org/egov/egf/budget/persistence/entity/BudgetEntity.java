package org.egov.egf.budget.persistence.entity;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.EgfStatus;
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
	private String description;
	private Boolean isActiveBudget;
	private Boolean isPrimaryBudget;
	private String materializedPath;
	private String referenceBudgetId;
	private Long documentNumber;
	private String statusId;

	public Budget toDomain() {
		Budget budget = new Budget();
		super.toDomain(budget);
		budget.setId(this.id);
		budget.setName(this.name);
		budget.setFinancialYearId(FinancialYearContract.builder().id(financialYearId).build());
		budget.setEstimationType(this.estimationType);
		budget.setParentId(Budget.builder().id(parentId).build());
		budget.setDescription(this.description);
		budget.setIsActiveBudget(this.isActiveBudget);
		budget.setIsPrimaryBudget(this.isPrimaryBudget);
		budget.setMaterializedPath(this.materializedPath);
		budget.setReferenceBudgetId(Budget.builder().id(referenceBudgetId).build());
		budget.setDocumentNumber(this.documentNumber);
		budget.setStatusId(EgfStatus.builder().id(statusId).build());
		return budget;
	}

	public BudgetEntity toEntity(Budget budget) {
		super.toEntity(budget);
		this.id = budget.getId();
		this.name = budget.getName();
		this.financialYearId = budget.getFinancialYearId() != null ? budget.getFinancialYearId().getId() : null;
		this.estimationType = budget.getEstimationType();
		this.parentId = budget.getParentId() != null ? budget.getParentId().getId() : null;
		this.description = budget.getDescription();
		this.isActiveBudget = budget.getIsActiveBudget();
		this.isPrimaryBudget = budget.getIsPrimaryBudget();
		this.materializedPath = budget.getMaterializedPath();
		this.referenceBudgetId = budget.getReferenceBudgetId() != null ? budget.getReferenceBudgetId().getId() : null;
		this.documentNumber = budget.getDocumentNumber();
		this.statusId = budget.getStatusId() != null ? budget.getStatusId().getId() : null;
		return this;
	}

}
