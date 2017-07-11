package org.egov.egf.budget.web.contract;

import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriationSearch;
import org.egov.egf.budget.domain.model.EgfStatus;
import org.egov.egf.master.web.contract.EgfStatusContract;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BudgetReAppropriationSearchContract extends BudgetReAppropriationContract {
	private Integer pageSize;
	private Integer offset;

	public BudgetReAppropriationSearch toDomain() {
		BudgetReAppropriationSearch budgetReAppropriationSearch = new BudgetReAppropriationSearch();
		budgetReAppropriationSearch.setId(this.getId());
		budgetReAppropriationSearch.setBudgetDetailId(BudgetDetail.builder()
				.id(this.getBudgetDetail() != null ? this.getBudgetDetail().getId() : null).build());
		budgetReAppropriationSearch.setAdditionAmount(this.getAdditionAmount());
		budgetReAppropriationSearch.setDeductionAmount(this.getDeductionAmount());
		budgetReAppropriationSearch.setOriginalAdditionAmount(this.getOriginalAdditionAmount());
		budgetReAppropriationSearch.setOriginalDeductionAmount(this.getOriginalDeductionAmount());
		budgetReAppropriationSearch.setAnticipatoryAmount(this.getAnticipatoryAmount());
		budgetReAppropriationSearch.setStatusId(
				EgfStatus.builder().id(this.getStatus() != null ? this.getStatus().getId() : null).build());
		budgetReAppropriationSearch.setAsOnDate(this.getAsOnDate());
		budgetReAppropriationSearch.setCreatedBy(this.getCreatedBy());
		budgetReAppropriationSearch.setCreatedDate(this.getCreatedDate());
		budgetReAppropriationSearch.setLastModifiedBy(this.getLastModifiedBy());
		budgetReAppropriationSearch.setLastModifiedDate(this.getLastModifiedDate());
		budgetReAppropriationSearch.setTenantId(this.getTenantId());
		budgetReAppropriationSearch.setPageSize(this.pageSize);
		budgetReAppropriationSearch.setOffset(this.offset);
		return budgetReAppropriationSearch;
	}

	public void toContract(BudgetReAppropriationSearch budgetReAppropriationSearch) {
		this.setId(budgetReAppropriationSearch.getId());
		this.setBudgetDetail(BudgetDetailContract.builder().id(budgetReAppropriationSearch.getBudgetDetailId() != null
				? budgetReAppropriationSearch.getBudgetDetailId().getId() : null).build());
		this.setAdditionAmount(budgetReAppropriationSearch.getAdditionAmount());
		this.setDeductionAmount(budgetReAppropriationSearch.getDeductionAmount());
		this.setOriginalAdditionAmount(budgetReAppropriationSearch.getOriginalAdditionAmount());
		this.setOriginalDeductionAmount(budgetReAppropriationSearch.getOriginalDeductionAmount());
		this.setAnticipatoryAmount(budgetReAppropriationSearch.getAnticipatoryAmount());
		this.setStatus(EgfStatusContract.builder().id(budgetReAppropriationSearch.getStatusId() != null
				? budgetReAppropriationSearch.getStatusId().getId() : null).build());
		this.setAsOnDate(budgetReAppropriationSearch.getAsOnDate());
		this.setCreatedBy(budgetReAppropriationSearch.getCreatedBy());
		this.setCreatedDate(budgetReAppropriationSearch.getCreatedDate());
		this.setLastModifiedBy(budgetReAppropriationSearch.getLastModifiedBy());
		this.setLastModifiedDate(budgetReAppropriationSearch.getLastModifiedDate());
		this.setTenantId(budgetReAppropriationSearch.getTenantId());
		this.setPageSize(budgetReAppropriationSearch.getPageSize());
		this.setOffset(budgetReAppropriationSearch.getOffset());
	}
}