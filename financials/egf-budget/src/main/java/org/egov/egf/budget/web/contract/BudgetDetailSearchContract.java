package org.egov.egf.budget.web.contract;

import org.egov.common.master.web.contract.BoundaryContract;
import org.egov.common.master.web.contract.DepartmentContract;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetailSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.EgfStatusContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetDetailSearchContract extends BudgetDetailContract {
	private Integer pageSize;
	private Integer offSet;

	public BudgetDetailSearch toDomain() {
		BudgetDetailSearch budgetDetailSearch = new BudgetDetailSearch();
		budgetDetailSearch.setId(this.getId());
		budgetDetailSearch.setBudgetGroupId(BudgetGroupContract.builder()
				.id(this.getBudgetGroup() != null ? this.getBudgetGroup().getId() : null).build());
		budgetDetailSearch.setBudgetId(Budget.builder().id(this.getBudget()!=null?this.getBudget().getId():null).build());
		budgetDetailSearch.setOriginalAmount(this.getOriginalAmount());
		budgetDetailSearch.setApprovedAmount(this.getApprovedAmount());
		budgetDetailSearch.setBudgetAvailable(this.getBudgetAvailable());
		budgetDetailSearch.setAnticipatoryAmount(this.getAnticipatoryAmount());
		budgetDetailSearch.setUsingDepartmentId(DepartmentContract.builder()
				.id(this.getUsingDepartment() != null ? this.getUsingDepartment().getId() : null).build());
		budgetDetailSearch.setExecutingDepartmentId(DepartmentContract.builder()
				.id(this.getExecutingDepartment() != null ? this.getExecutingDepartment().getId() : null).build());
		budgetDetailSearch.setFunctionId(
				FunctionContract.builder().id(this.getFunction() != null ? this.getFunction().getId() : null).build());
		budgetDetailSearch.setSchemeId(
				SchemeContract.builder().id(this.getScheme() != null ? this.getScheme().getId() : null).build());
		budgetDetailSearch
				.setFundId(FundContract.builder().id(this.getFund() != null ? this.getFund().getId() : null).build());
		budgetDetailSearch.setSubSchemeId(SubSchemeContract.builder()
				.id(this.getSubScheme() != null ? this.getSubScheme().getId() : null).build());
		budgetDetailSearch.setFunctionaryId(FunctionContract.builder()
				.id(this.getFunctionary() != null ? this.getFunctionary().getId() : null).build());
		budgetDetailSearch.setBoundaryId(
				BoundaryContract.builder().id(this.getBoundary() != null ? this.getBoundary().getId() : null).build());
		budgetDetailSearch.setMaterializedPath(this.getMaterializedPath());
		budgetDetailSearch.setDocumentNumber(this.getDocumentNumber());
		budgetDetailSearch.setUniqueNo(this.getUniqueNo());
		budgetDetailSearch.setPlanningPercent(this.getPlanningPercent());
		budgetDetailSearch.setStatusId(
				EgfStatus.builder().id(this.getStatus() != null ? this.getStatus().getId() : null).build());
		budgetDetailSearch.setCreatedBy(this.getCreatedBy());
		budgetDetailSearch.setCreatedDate(this.getCreatedDate());
		budgetDetailSearch.setLastModifiedBy(this.getLastModifiedBy());
		budgetDetailSearch.setLastModifiedDate(this.getLastModifiedDate());
		budgetDetailSearch.setTenantId(this.getTenantId());
		budgetDetailSearch.setPageSize(this.pageSize);
		budgetDetailSearch.setOffSet(this.offSet);
		return budgetDetailSearch;
	}

	public void toContract(BudgetDetailSearch budgetDetailSearch) {
		this.setId(budgetDetailSearch.getId());
		this.setBudgetGroup(
				budgetDetailSearch.getBudgetGroupId() != null ? budgetDetailSearch.getBudgetGroupId() : null);
		this.setBudget(BudgetContract.builder()
				.id(budgetDetailSearch.getBudgetId() != null ? budgetDetailSearch.getBudgetId().getId() : null)
				.build());
		this.setOriginalAmount(budgetDetailSearch.getOriginalAmount());
		this.setApprovedAmount(budgetDetailSearch.getApprovedAmount());
		this.setBudgetAvailable(budgetDetailSearch.getBudgetAvailable());
		this.setAnticipatoryAmount(budgetDetailSearch.getAnticipatoryAmount());
		this.setUsingDepartment(
				budgetDetailSearch.getUsingDepartmentId() != null ? budgetDetailSearch.getUsingDepartmentId() : null);
		this.setExecutingDepartment(budgetDetailSearch.getExecutingDepartmentId() != null
				? budgetDetailSearch.getExecutingDepartmentId() : null);
		this.setFunction(budgetDetailSearch.getFunctionId() != null ? budgetDetailSearch.getFunctionId() : null);
		this.setScheme(budgetDetailSearch.getSchemeId() != null ? budgetDetailSearch.getSchemeId() : null);
		this.setFund(budgetDetailSearch.getFundId() != null ? budgetDetailSearch.getFundId() : null);
		this.setSubScheme(budgetDetailSearch.getSubSchemeId() != null ? budgetDetailSearch.getSubSchemeId() : null);
		this.setFunctionary(
				budgetDetailSearch.getFunctionaryId() != null ? budgetDetailSearch.getFunctionaryId() : null);
		this.setBoundary(budgetDetailSearch.getBoundaryId() != null ? budgetDetailSearch.getBoundaryId() : null);
		this.setMaterializedPath(budgetDetailSearch.getMaterializedPath());
		this.setDocumentNumber(budgetDetailSearch.getDocumentNumber());
		this.setUniqueNo(budgetDetailSearch.getUniqueNo());
		this.setPlanningPercent(budgetDetailSearch.getPlanningPercent());
		this.setStatus(EgfStatusContract.builder()
				.id(budgetDetailSearch.getStatusId() != null ? budgetDetailSearch.getStatusId().getId() : null)
				.build());
		this.setCreatedBy(budgetDetailSearch.getCreatedBy());
		this.setCreatedDate(budgetDetailSearch.getCreatedDate());
		this.setLastModifiedBy(budgetDetailSearch.getLastModifiedBy());
		this.setLastModifiedDate(budgetDetailSearch.getLastModifiedDate());
		this.setTenantId(budgetDetailSearch.getTenantId());
		this.setPageSize(budgetDetailSearch.getPageSize());
		this.setOffSet(budgetDetailSearch.getOffSet());
	}
}