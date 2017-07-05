package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;

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
public class BudgetReAppropriationEntity extends AuditableEntity {
	public static final String TABLE_NAME = "egf_budgetreappropriation";
	private String id;
	private String budgetDetailId;
	private BigDecimal additionAmount;
	private BigDecimal deductionAmount;
	private BigDecimal originalAdditionAmount;
	private BigDecimal originalDeductionAmount;
	private BigDecimal anticipatoryAmount;
	private String statusId;
	private Date asOnDate;

	public BudgetReAppropriation toDomain() {
		BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
		super.toDomain(budgetReAppropriation);
		budgetReAppropriation.setId(this.id);
		budgetReAppropriation.setBudgetDetail(BudgetDetail.builder().id(budgetDetailId).build());
		budgetReAppropriation.setAdditionAmount(this.additionAmount);
		budgetReAppropriation.setDeductionAmount(this.deductionAmount);
		budgetReAppropriation.setOriginalAdditionAmount(this.originalAdditionAmount);
		budgetReAppropriation.setOriginalDeductionAmount(this.originalDeductionAmount);
		budgetReAppropriation.setAnticipatoryAmount(this.anticipatoryAmount);
		//budgetReAppropriation.setStatus(EgfStatus.builder().id(statusId).build());
		budgetReAppropriation.setAsOnDate(this.asOnDate);
		return budgetReAppropriation;
	}

	public BudgetReAppropriationEntity toEntity(BudgetReAppropriation budgetReAppropriation) {
		super.toEntity(budgetReAppropriation);
		this.id = budgetReAppropriation.getId();
		this.budgetDetailId = budgetReAppropriation.getBudgetDetail() != null
				? budgetReAppropriation.getBudgetDetail().getId() : null;
		this.additionAmount = budgetReAppropriation.getAdditionAmount();
		this.deductionAmount = budgetReAppropriation.getDeductionAmount();
		this.originalAdditionAmount = budgetReAppropriation.getOriginalAdditionAmount();
		this.originalDeductionAmount = budgetReAppropriation.getOriginalDeductionAmount();
		this.anticipatoryAmount = budgetReAppropriation.getAnticipatoryAmount();
		this.statusId = budgetReAppropriation.getStatus() != null ? budgetReAppropriation.getStatus().getId() : null;
		this.asOnDate = budgetReAppropriation.getAsOnDate();
		return this;
	}

}
