package builders.financial;

import entities.financial.FinancialExpenseBillDetails;

public class FinancialExpenseBillDetailsBuilder {

    FinancialExpenseBillDetails financialExpenseBillDetails = new FinancialExpenseBillDetails();

    public FinancialExpenseBillDetailsBuilder withExpenseFund(String expenseFund) {
        financialExpenseBillDetails.setExpenseFund(expenseFund);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseDepartment(String expenseDepartment) {
        financialExpenseBillDetails.setExpenseDeparment(expenseDepartment);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseFunction(String expenseFunction) {
        financialExpenseBillDetails.setExpenseFunction(expenseFunction);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder with(String expenseFund) {
        financialExpenseBillDetails.setExpenseFund(expenseFund);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withBillSubType(String billSubType) {
        financialExpenseBillDetails.setExpenseBillSubType(billSubType);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseAccountDebit(String expenseAccountDebit) {
        financialExpenseBillDetails.setExpenseAccountCodeDebit(expenseAccountDebit);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseAccountCredit(String expenseAccountCredit) {
        financialExpenseBillDetails.setExpenseAccountCodeCredit(expenseAccountCredit);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseDebitAmount(String expenseDebitAmount) {
        financialExpenseBillDetails.setExpenseDebitAmount(expenseDebitAmount);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseCreditAmount(String expenseCreditAmount) {
        financialExpenseBillDetails.setExpenseCreditAmount(expenseCreditAmount);
        return this;
    }

    public FinancialExpenseBillDetailsBuilder withExpenseNetAmount(String expenseNetAmount) {
        financialExpenseBillDetails.setExpenseNetAmount(expenseNetAmount);
        return this;
    }

    public FinancialExpenseBillDetails build() {
        return financialExpenseBillDetails;
    }
}
