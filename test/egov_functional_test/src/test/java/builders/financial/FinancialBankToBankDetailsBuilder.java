package builders.financial;

import entities.financial.FinancialBankToBankDetails;

public class FinancialBankToBankDetailsBuilder {

    FinancialBankToBankDetails financialBankToBankDetails = new FinancialBankToBankDetails();

    public FinancialBankToBankDetailsBuilder withFundId(String fundId) {
        financialBankToBankDetails.setFundId(fundId);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withVoucherDepartment(String voucherDepartment) {
        financialBankToBankDetails.setVoucherDepartment(voucherDepartment);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withVoucherFunction(String voucherFunction) {
        financialBankToBankDetails.setVoucherFunction(voucherFunction);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withFromBank(String fromBank) {
        financialBankToBankDetails.setFromBank(fromBank);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withFromAccountNumber(String fromAccountNumber) {
        financialBankToBankDetails.setFromAccountNumber(fromAccountNumber);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withToFundId(String toFundId) {
        financialBankToBankDetails.setToFundId(toFundId);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withToBank(String toBank) {
        financialBankToBankDetails.setToBank(toBank);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withToAccountNumber(String withToAccountNumber) {
        financialBankToBankDetails.setToAccountNumber(withToAccountNumber);
        return this;
    }

    public FinancialBankToBankDetailsBuilder withAmount(String withAmount) {
        financialBankToBankDetails.setAmount(withAmount);
        return this;
    }

    public FinancialBankToBankDetails build() {
        return financialBankToBankDetails;
    }
}

