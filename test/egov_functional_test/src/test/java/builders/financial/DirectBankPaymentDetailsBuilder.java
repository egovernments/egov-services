package builders.financial;

import entities.financial.DirectBankPaymentDetails;

public class DirectBankPaymentDetailsBuilder {

    DirectBankPaymentDetails directBankPaymentDetails = new DirectBankPaymentDetails();

    public DirectBankPaymentDetailsBuilder withFundId(String fundId) {
        directBankPaymentDetails.setFundId(fundId);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withVoucherDepartment(String voucherDepartment) {
        directBankPaymentDetails.setVoucherDepartment(voucherDepartment);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withVoucherFunction(String voucherFunction) {
        directBankPaymentDetails.setVoucherFunction(voucherFunction);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withBankBranch(String bankBranch) {
        directBankPaymentDetails.setBankBranch(bankBranch);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withAmount(String amount) {
        directBankPaymentDetails.setAmount(amount);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withAccountNumber(String accountNumber) {
        directBankPaymentDetails.setAccountNumber(accountNumber);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withAccountCode1(String accountCode1) {
        directBankPaymentDetails.setAccountCode1(accountCode1);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withDebitAmount1(String debitAmount1) {
        directBankPaymentDetails.setDebitAmount1(debitAmount1);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withLedgerAccount1(String ledgerAccount1) {
        directBankPaymentDetails.setLedgerAccount1(ledgerAccount1);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withLedgerType1(String ledgerType1) {
        directBankPaymentDetails.setLedgerType1(ledgerType1);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withLedgerCode1(String ledgerCode1) {
        directBankPaymentDetails.setLedgerCode1(ledgerCode1);
        return this;
    }

    public DirectBankPaymentDetailsBuilder withLedgerAmount1(String ledgerAmount1) {
        directBankPaymentDetails.setLedgerAmount1(ledgerAmount1);
        return this;
    }

    public DirectBankPaymentDetails build() {
        return directBankPaymentDetails;
    }
}

