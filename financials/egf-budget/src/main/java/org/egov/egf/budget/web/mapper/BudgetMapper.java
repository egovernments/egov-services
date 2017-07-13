package org.egov.egf.budget.web.mapper;

import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.budget.web.contract.BudgetContract;
import org.egov.egf.budget.web.contract.BudgetSearchContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FinancialYearContract;

public class BudgetMapper {

	public Budget toDomain(BudgetContract contract) {

		Budget budget = new Budget();

		budget.setId(contract.getId());
		budget.setName(contract.getName());
		budget.setFinancialYear(contract.getFinancialYear());
		budget.setEstimationType(contract.getEstimationType());
		budget.setParent(
				Budget.builder().id(contract.getParent() != null ? contract.getParent().getId() : null).build());
		budget.setDescription(contract.getDescription());
		budget.setActive(contract.getActive());
		budget.setPrimaryBudget(contract.getPrimaryBudget());
		budget.setMaterializedPath(contract.getMaterializedPath());
		budget.setReferenceBudget(Budget.builder()
				.id(contract.getReferenceBudget() != null ? contract.getReferenceBudget().getId() : null).build());
		budget.setDocumentNumber(contract.getDocumentNumber());
		budget.setStatus(
				EgfStatus.builder().id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
		budget.setCreatedBy(contract.getCreatedBy());
		budget.setCreatedDate(contract.getCreatedDate());
		budget.setLastModifiedBy(contract.getLastModifiedBy());
		budget.setLastModifiedDate(contract.getLastModifiedDate());
		budget.setTenantId(contract.getTenantId());

		return budget;
	}

	public BudgetContract toContract(Budget budget) {

		BudgetContract contract = new BudgetContract();
		contract.setId(budget.getId());
		contract.setName(budget.getName());
		if (budget.getFinancialYear() != null)
			contract.setFinancialYear(FinancialYearContract.builder().id(budget.getFinancialYear().getId())
					.active(budget.getFinancialYear().getActive()).endingDate(budget.getFinancialYear().getEndingDate())
					.finYearRange(budget.getFinancialYear().getFinYearRange())
					.isActiveForPosting(budget.getFinancialYear().getIsActiveForPosting())
					.isClosed(budget.getFinancialYear().getIsClosed())
					.startingDate(budget.getFinancialYear().getStartingDate())
					.transferClosingBalance(budget.getFinancialYear().getTransferClosingBalance()).build());
		contract.setEstimationType(budget.getEstimationType());
		if (budget.getParent() != null) {
			contract.setParent(toContract(budget.getParent()));
		}
		contract.setDescription(budget.getDescription());
		contract.setActive(budget.getActive());
		contract.setPrimaryBudget(budget.getPrimaryBudget());
		contract.setMaterializedPath(budget.getMaterializedPath());
		if (budget.getReferenceBudget() != null) {
			contract.setReferenceBudget(toContract(budget.getReferenceBudget()));
		}
		contract.setDocumentNumber(budget.getDocumentNumber());
		if (budget.getStatus() != null)
			contract.setStatus(EgfStatusContract.builder().id(budget.getStatus().getId())
					.code(budget.getStatus().getCode()).description(budget.getStatus().getDescription())
					.moduleType(budget.getStatus().getModuleType()).build());
		contract.setCreatedBy(budget.getCreatedBy());
		contract.setCreatedDate(budget.getCreatedDate());
		contract.setLastModifiedBy(budget.getLastModifiedBy());
		contract.setLastModifiedDate(budget.getLastModifiedDate());
		contract.setTenantId(budget.getTenantId());

		return contract;
	}

	public BudgetSearch toSearchDomain(BudgetSearchContract contract) {

		BudgetSearch budgetSearch = new BudgetSearch();

		budgetSearch.setId(contract.getId());
		budgetSearch.setName(contract.getName());
		budgetSearch.setFinancialYear(contract.getFinancialYear());
		budgetSearch.setEstimationType(contract.getEstimationType());
		budgetSearch.setParent(
				Budget.builder().id(contract.getParent() != null ? contract.getParent().getId() : null).build());
		budgetSearch.setDescription(contract.getDescription());
		budgetSearch.setActive(contract.getActive());
		budgetSearch.setPrimaryBudget(contract.getPrimaryBudget());
		budgetSearch.setMaterializedPath(contract.getMaterializedPath());
		budgetSearch.setReferenceBudget(Budget.builder()
				.id(contract.getReferenceBudget() != null ? contract.getReferenceBudget().getId() : null).build());
		budgetSearch.setDocumentNumber(contract.getDocumentNumber());
		budgetSearch.setStatus(
				EgfStatus.builder().id(contract.getStatus() != null ? contract.getStatus().getId() : null).build());
		budgetSearch.setCreatedBy(contract.getCreatedBy());
		budgetSearch.setCreatedDate(contract.getCreatedDate());
		budgetSearch.setLastModifiedBy(contract.getLastModifiedBy());
		budgetSearch.setLastModifiedDate(contract.getLastModifiedDate());
		budgetSearch.setTenantId(contract.getTenantId());
		budgetSearch.setPageSize(contract.getPageSize());
		budgetSearch.setOffset(contract.getOffset());

		return budgetSearch;
	}

	public BudgetSearchContract toSearchContract(BudgetSearch budgetSearch) {

		BudgetSearchContract contract = new BudgetSearchContract();

		contract.setId(budgetSearch.getId());
		contract.setName(budgetSearch.getName());
		contract.setFinancialYear(FinancialYearContract.builder()
				.id(budgetSearch.getFinancialYear() != null ? budgetSearch.getFinancialYear().getId() : null).build());
		contract.setEstimationType(budgetSearch.getEstimationType());
		contract.setParent(BudgetContract.builder()
				.id(budgetSearch.getParent() != null ? budgetSearch.getParent().getId() : null).build());
		contract.setDescription(budgetSearch.getDescription());
		contract.setActive(budgetSearch.getActive());
		contract.setPrimaryBudget(budgetSearch.getPrimaryBudget());
		contract.setMaterializedPath(budgetSearch.getMaterializedPath());
		contract.setReferenceBudget(BudgetContract.builder()
				.id(budgetSearch.getReferenceBudget() != null ? budgetSearch.getReferenceBudget().getId() : null)
				.build());
		contract.setDocumentNumber(budgetSearch.getDocumentNumber());
		contract.setStatus(EgfStatusContract.builder()
				.id(budgetSearch.getStatus() != null ? budgetSearch.getStatus().getId() : null).build());
		contract.setCreatedBy(budgetSearch.getCreatedBy());
		contract.setCreatedDate(budgetSearch.getCreatedDate());
		contract.setLastModifiedBy(budgetSearch.getLastModifiedBy());
		contract.setLastModifiedDate(budgetSearch.getLastModifiedDate());
		contract.setTenantId(budgetSearch.getTenantId());
		contract.setPageSize(budgetSearch.getPageSize());
		contract.setOffset(budgetSearch.getOffset());

		return contract;
	}

}