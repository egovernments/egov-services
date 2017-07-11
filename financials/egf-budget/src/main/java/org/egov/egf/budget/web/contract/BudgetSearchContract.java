package org.egov.egf.budget.web.contract;

import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetSearchContract extends BudgetContract {
	private Integer pageSize;
	private Integer offSet;

	public BudgetSearch toDomain() {
		BudgetSearch budgetSearch = new BudgetSearch();
		budgetSearch.setId(this.getId());
		budgetSearch.setName(this.getName());
		budgetSearch.setFinancialYearId(this.getFinancialYear());
		budgetSearch.setEstimationType(this.getEstimationType());
		budgetSearch
				.setParentId(Budget.builder().id(this.getParent() != null ? this.getParent().getId() : null).build());
		budgetSearch.setDescription(this.getDescription());
		budgetSearch.setIsActiveBudget(this.getIsActiveBudget());
		budgetSearch.setIsPrimaryBudget(this.getIsPrimaryBudget());
		budgetSearch.setMaterializedPath(this.getMaterializedPath());
		budgetSearch.setReferenceBudgetId(Budget.builder()
				.id(this.getReferenceBudget() != null ? this.getReferenceBudget().getId() : null).build());
		budgetSearch.setDocumentNumber(this.getDocumentNumber());
		budgetSearch.setStatusId(
				EgfStatus.builder().id(this.getStatus() != null ? this.getStatus().getId() : null).build());
		budgetSearch.setCreatedBy(this.getCreatedBy());
		budgetSearch.setCreatedDate(this.getCreatedDate());
		budgetSearch.setLastModifiedBy(this.getLastModifiedBy());
		budgetSearch.setLastModifiedDate(this.getLastModifiedDate());
		budgetSearch.setTenantId(this.getTenantId());
		budgetSearch.setPageSize(this.getPageSize());
		budgetSearch.setOffSet(this.getOffSet());
		return budgetSearch;
	}

	public void toContract(BudgetSearch budgetSearch) {
		this.setId(budgetSearch.getId());
		this.setName(budgetSearch.getName());
		this.setFinancialYear(FinancialYearContract.builder()
				.id(budgetSearch.getFinancialYearId() != null ? budgetSearch.getFinancialYearId().getId() : null)
				.build());
		this.setEstimationType(budgetSearch.getEstimationType());
		this.setParent(BudgetContract.builder()
				.id(budgetSearch.getParentId() != null ? budgetSearch.getParentId().getId() : null).build());
		this.setDescription(budgetSearch.getDescription());
		this.setIsActiveBudget(budgetSearch.getIsActiveBudget());
		this.setIsPrimaryBudget(budgetSearch.getIsPrimaryBudget());
		this.setMaterializedPath(budgetSearch.getMaterializedPath());
		this.setReferenceBudget(BudgetContract.builder()
				.id(budgetSearch.getReferenceBudgetId() != null ? budgetSearch.getReferenceBudgetId().getId() : null)
				.build());
		this.setDocumentNumber(budgetSearch.getDocumentNumber());
		this.setStatus(EgfStatusContract.builder()
				.id(budgetSearch.getStatusId() != null ? budgetSearch.getStatusId().getId() : null).build());
		this.setCreatedBy(budgetSearch.getCreatedBy());
		this.setCreatedDate(budgetSearch.getCreatedDate());
		this.setLastModifiedBy(budgetSearch.getLastModifiedBy());
		this.setLastModifiedDate(budgetSearch.getLastModifiedDate());
		this.setTenantId(budgetSearch.getTenantId());
		this.setPageSize(budgetSearch.getPageSize());
		this.setOffSet(budgetSearch.getOffSet());
	}
}