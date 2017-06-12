package builders.works;

import entities.works.FinancialDetails;

public class FinancialDetailsBuilder {

    FinancialDetails financialDetails = new FinancialDetails();

    public FinancialDetailsBuilder() {

    }

    public FinancialDetailsBuilder withFund(String fund) {
        financialDetails.setFund(fund);
        return this;
    }

    public FinancialDetailsBuilder withFunction(String function) {
        financialDetails.setFunction(function);
        return this;
    }

    public FinancialDetailsBuilder withBudgetHead(String budgetHead) {
        financialDetails.setBudgetHead(budgetHead);
        return this;
    }

    public FinancialDetails build() {
        return financialDetails;
    }
}
