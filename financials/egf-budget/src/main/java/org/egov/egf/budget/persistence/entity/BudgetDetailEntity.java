package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.master.web.contract.BoundaryContract;
import org.egov.common.master.web.contract.DepartmentContract;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.master.web.contract.BudgetGroupContract;
import org.egov.egf.master.web.contract.FunctionContract;
import org.egov.egf.master.web.contract.FundContract;
import org.egov.egf.master.web.contract.SchemeContract;
import org.egov.egf.master.web.contract.SubSchemeContract;

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
public class BudgetDetailEntity extends AuditableEntity {

	public static final String TABLE_NAME = "egf_budgetdetail";
	private String id;
	private String budgetGroupId;
	private String budgetId;
	private BigDecimal originalAmount;
	private BigDecimal approvedAmount;
	private BigDecimal budgetAvailable;
	private BigDecimal anticipatoryAmount;
	private String usingDepartmentId;
	private String executingDepartmentId;
	private String functionId;
	private String schemeId;
	private String fundId;
	private String subSchemeId;
	private String functionaryId;
	private String boundaryId;
	private String materializedPath;
	private Long documentNumber;
	private String uniqueNo;
	private BigDecimal planningPercent;
	private String statusId;

	public BudgetDetail toDomain() {
		BudgetDetail budgetDetail = new BudgetDetail();
		super.toDomain(budgetDetail);
		budgetDetail.setId(this.id);
		budgetDetail.setBudgetGroup(BudgetGroupContract.builder().id(budgetGroupId).build());
		budgetDetail.setBudget(Budget.builder().id(budgetId).build());
		budgetDetail.setOriginalAmount(this.originalAmount);
		budgetDetail.setApprovedAmount(this.approvedAmount);
		budgetDetail.setBudgetAvailable(this.budgetAvailable);
		budgetDetail.setAnticipatoryAmount(this.anticipatoryAmount);
		budgetDetail.setUsingDepartment(DepartmentContract.builder().id(usingDepartmentId).build());
		budgetDetail.setExecutingDepartment(DepartmentContract.builder().id(executingDepartmentId).build());
		budgetDetail.setFunction(FunctionContract.builder().id(functionId).build());
		budgetDetail.setScheme(SchemeContract.builder().id(schemeId).build());
		budgetDetail.setFund(FundContract.builder().id(fundId).build());
		budgetDetail.setSubScheme(SubSchemeContract.builder().id(subSchemeId).build());
		budgetDetail.setFunctionary(FunctionContract.builder().id(functionaryId).build());
		budgetDetail.setBoundary(BoundaryContract.builder().id(boundaryId).build());
		budgetDetail.setMaterializedPath(this.materializedPath);
		budgetDetail.setDocumentNumber(this.documentNumber);
		budgetDetail.setUniqueNo(this.uniqueNo);
		budgetDetail.setPlanningPercent(this.planningPercent);
		// budgetDetail.setStatus(EgfStatus.builder().id(statusId).build());
		return budgetDetail;
	}

	public BudgetDetailEntity toEntity(BudgetDetail budgetDetail) {
		super.toEntity(budgetDetail);
		this.id = budgetDetail.getId();
		this.budgetGroupId = budgetDetail.getBudgetGroup() != null ? budgetDetail.getBudgetGroup().getId() : null;
		this.budgetId = budgetDetail.getBudget() != null ? budgetDetail.getBudget().getId() : null;
		this.originalAmount = budgetDetail.getOriginalAmount();
		this.approvedAmount = budgetDetail.getApprovedAmount();
		this.budgetAvailable = budgetDetail.getBudgetAvailable();
		this.anticipatoryAmount = budgetDetail.getAnticipatoryAmount();
		this.usingDepartmentId = budgetDetail.getUsingDepartment() != null ? budgetDetail.getUsingDepartment().getId()
				: null;
		this.executingDepartmentId = budgetDetail.getExecutingDepartment() != null
				? budgetDetail.getExecutingDepartment().getId() : null;
		this.functionId = budgetDetail.getFunction() != null ? budgetDetail.getFunction().getId() : null;
		this.schemeId = budgetDetail.getScheme() != null ? budgetDetail.getScheme().getId() : null;
		this.fundId = budgetDetail.getFund() != null ? budgetDetail.getFund().getId() : null;
		this.subSchemeId = budgetDetail.getSubScheme() != null ? budgetDetail.getSubScheme().getId() : null;
		this.functionaryId = budgetDetail.getFunctionary() != null ? budgetDetail.getFunctionary().getId() : null;
		this.boundaryId = budgetDetail.getBoundary() != null ? budgetDetail.getBoundary().getId() : null;
		this.materializedPath = budgetDetail.getMaterializedPath();
		this.documentNumber = budgetDetail.getDocumentNumber();
		this.uniqueNo = budgetDetail.getUniqueNo();
		this.planningPercent = budgetDetail.getPlanningPercent();
		this.statusId = budgetDetail.getStatus() != null ? budgetDetail.getStatus().getId() : null;
		return this;
	}

}
