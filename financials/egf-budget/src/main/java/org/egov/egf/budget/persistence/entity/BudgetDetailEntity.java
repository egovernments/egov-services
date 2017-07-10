package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;

import org.egov.common.master.web.contract.BoundaryContract;
import org.egov.common.master.web.contract.DepartmentContract;
import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.Budget;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.EgfStatus;
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
		budgetDetail.setBudgetGroupId(BudgetGroupContract.builder().id(budgetGroupId).build());
		budgetDetail.setBudgetId(Budget.builder().id(budgetId).build());
		budgetDetail.setOriginalAmount(this.originalAmount);
		budgetDetail.setApprovedAmount(this.approvedAmount);
		budgetDetail.setBudgetAvailable(this.budgetAvailable);
		budgetDetail.setAnticipatoryAmount(this.anticipatoryAmount);
		budgetDetail.setUsingDepartmentId(DepartmentContract.builder().id(usingDepartmentId).build());
		budgetDetail.setExecutingDepartmentId(DepartmentContract.builder().id(executingDepartmentId).build());
		budgetDetail.setFunctionId(FunctionContract.builder().id(functionId).build());
		budgetDetail.setSchemeId(SchemeContract.builder().id(schemeId).build());
		budgetDetail.setFundId(FundContract.builder().id(fundId).build());
		budgetDetail.setSubSchemeId(SubSchemeContract.builder().id(subSchemeId).build());
		budgetDetail.setFunctionaryId(FunctionContract.builder().id(functionaryId).build());
		budgetDetail.setBoundaryId(BoundaryContract.builder().id(boundaryId).build());
		budgetDetail.setMaterializedPath(this.materializedPath);
		budgetDetail.setDocumentNumber(this.documentNumber);
		budgetDetail.setUniqueNo(this.uniqueNo);
		budgetDetail.setPlanningPercent(this.planningPercent);
		budgetDetail.setStatusId(EgfStatus.builder().id(statusId).build());
		return budgetDetail;
	}

	public BudgetDetailEntity toEntity(BudgetDetail budgetDetail) {
		super.toEntity(budgetDetail);
		this.id = budgetDetail.getId();
		this.budgetGroupId = budgetDetail.getBudgetGroupId() != null ? budgetDetail.getBudgetGroupId().getId() : null;
		this.budgetId = budgetDetail.getBudgetId() != null ? budgetDetail.getBudgetId().getId() : null;
		this.originalAmount = budgetDetail.getOriginalAmount();
		this.approvedAmount = budgetDetail.getApprovedAmount();
		this.budgetAvailable = budgetDetail.getBudgetAvailable();
		this.anticipatoryAmount = budgetDetail.getAnticipatoryAmount();
		this.usingDepartmentId = budgetDetail.getUsingDepartmentId() != null ? budgetDetail.getUsingDepartmentId().getId()
				: null;
		this.executingDepartmentId = budgetDetail.getExecutingDepartmentId() != null
				? budgetDetail.getExecutingDepartmentId().getId() : null;
		this.functionId = budgetDetail.getFunctionId() != null ? budgetDetail.getFunctionId().getId() : null;
		this.schemeId = budgetDetail.getSchemeId() != null ? budgetDetail.getSchemeId().getId() : null;
		this.fundId = budgetDetail.getFundId() != null ? budgetDetail.getFundId().getId() : null;
		this.subSchemeId = budgetDetail.getSubSchemeId() != null ? budgetDetail.getSubSchemeId().getId() : null;
		this.functionaryId = budgetDetail.getFunctionaryId() != null ? budgetDetail.getFunctionaryId().getId() : null;
		this.boundaryId = budgetDetail.getBoundaryId() != null ? budgetDetail.getBoundaryId().getId() : null;
		this.materializedPath = budgetDetail.getMaterializedPath();
		this.documentNumber = budgetDetail.getDocumentNumber();
		this.uniqueNo = budgetDetail.getUniqueNo();
		this.planningPercent = budgetDetail.getPlanningPercent();
		this.statusId = budgetDetail.getStatusId() != null ? budgetDetail.getStatusId().getId() : null;
		return this;
	}

}
