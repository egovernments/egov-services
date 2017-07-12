package org.egov.egf.budget.persistence.entity;

import org.egov.common.domain.model.Auditable;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.budget.domain.model.EstimationType;
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
		Budget budget = new Budget();
		super.toDomain(budget);
		budget.setId(this.id);
		budget.setName(this.name);
		budget.setFinancialYear(FinancialYearContract.builder().id(financialYearId).build());
		budget.setEstimationType(EstimationType.valueOf(this.estimationType));
		budget.setParent(Budget.builder().id(parentId).build());
		budget.setActive(this.active);
		budget.setPrimaryBudget(this.primaryBudget);
		budget.setReferenceBudget(Budget.builder().id(referenceBudgetId).build());
		budget.setStatus(EgfStatus.builder().id(statusId).build());
		budget.setDocumentNumber(this.documentNumber);
		budget.setDescription(this.description);
		budget.setMaterializedPath(this.materializedPath);
		return budget;
	}

	public BudgetEntity toEntity(Budget budget) {
		super.toEntity((Auditable) budget);
		this.id = budget.getId();
		this.name = budget.getName();
		this.financialYearId = budget.getFinancialYear() != null ? budget.getFinancialYear().getId() : null;
		this.estimationType = budget.getEstimationType() != null ? budget.getEstimationType().name() : null;
		this.parentId = budget.getParent() != null ? budget.getParent().getId() : null;
		this.active = budget.getActive();
		this.primaryBudget = budget.getPrimaryBudget();
		this.referenceBudgetId = budget.getReferenceBudget() != null ? budget.getReferenceBudget().getId() : null;
		this.statusId = budget.getStatus() != null ? budget.getStatus().getId() : null;
		this.documentNumber = budget.getDocumentNumber();
		this.description = budget.getDescription();
		this.materializedPath = budget.getMaterializedPath();
		return this;
	}

}
