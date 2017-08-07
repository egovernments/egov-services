package org.egov.egf.budget.persistence.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.common.persistence.entity.AuditableEntity;
import org.egov.egf.budget.domain.model.BudgetDetail;
import org.egov.egf.budget.domain.model.BudgetReAppropriation;
import org.egov.egf.master.web.contract.FinancialStatusContract;

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
        final BudgetReAppropriation budgetReAppropriation = new BudgetReAppropriation();
        super.toDomain(budgetReAppropriation);
        budgetReAppropriation.setId(id);
        budgetReAppropriation.setBudgetDetail(BudgetDetail.builder().id(budgetDetailId).build());
        budgetReAppropriation.setAdditionAmount(additionAmount);
        budgetReAppropriation.setDeductionAmount(deductionAmount);
        budgetReAppropriation.setOriginalAdditionAmount(originalAdditionAmount);
        budgetReAppropriation.setOriginalDeductionAmount(originalDeductionAmount);
        budgetReAppropriation.setAnticipatoryAmount(anticipatoryAmount);
        budgetReAppropriation.setStatus(FinancialStatusContract.builder().id(statusId).build());
        budgetReAppropriation.setAsOnDate(asOnDate);
        return budgetReAppropriation;
    }

    public BudgetReAppropriationEntity toEntity(final BudgetReAppropriation budgetReAppropriation) {
        super.toEntity(budgetReAppropriation);
        id = budgetReAppropriation.getId();
        budgetDetailId = budgetReAppropriation.getBudgetDetail() != null
                ? budgetReAppropriation.getBudgetDetail().getId() : null;
        additionAmount = budgetReAppropriation.getAdditionAmount();
        deductionAmount = budgetReAppropriation.getDeductionAmount();
        originalAdditionAmount = budgetReAppropriation.getOriginalAdditionAmount();
        originalDeductionAmount = budgetReAppropriation.getOriginalDeductionAmount();
        anticipatoryAmount = budgetReAppropriation.getAnticipatoryAmount();
        statusId = budgetReAppropriation.getStatus() != null ? budgetReAppropriation.getStatus().getId() : null;
        asOnDate = budgetReAppropriation.getAsOnDate();
        return this;
    }

}
