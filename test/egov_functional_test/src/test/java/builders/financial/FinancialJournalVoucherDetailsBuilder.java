package builders.financial;

import entities.financial.FinancialJournalVoucherDetails;

public class FinancialJournalVoucherDetailsBuilder {

    FinancialJournalVoucherDetails financialJournalVoucherDetails = new FinancialJournalVoucherDetails();

    public FinancialJournalVoucherDetailsBuilder withFundId(String fund) {
        financialJournalVoucherDetails.setFundId(fund);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withVoucherType(String voucherType) {
        financialJournalVoucherDetails.setVoucherType(voucherType);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withAccountCode1(String accountCode1) {
        financialJournalVoucherDetails.setAccountCode1(accountCode1);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withAccountCode2(String accountCode2) {
        financialJournalVoucherDetails.setAccountCode2(accountCode2);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withDepartment(String department) {
        financialJournalVoucherDetails.setDepartment(department);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withFunction(String function) {
        financialJournalVoucherDetails.setFunction(function);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withAccountCode3(String accountCode3) {
        financialJournalVoucherDetails.setAccountCode3(accountCode3);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withDebitAmount1(String debitAmount1) {
        financialJournalVoucherDetails.setDebitAmount1(debitAmount1);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withCreditAmount2(String creditAmount2) {
        financialJournalVoucherDetails.setCreditAmount2(creditAmount2);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withCreditAmount3(String creditAmount3) {
        financialJournalVoucherDetails.setCreditAmount3(creditAmount3);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerAmount1(String ledgerAmount1) {
        financialJournalVoucherDetails.setLedgerAmount1(ledgerAmount1);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerAmount2(String ledgerAmount2) {
        financialJournalVoucherDetails.setLedgerAmount2(ledgerAmount2);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerType1(String ledgerType1) {
        financialJournalVoucherDetails.setLedgerType1(ledgerType1);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerType2(String ledgerType2) {
        financialJournalVoucherDetails.setLedgerType2(ledgerType2);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerCode1(String ledgerCode1) {
        financialJournalVoucherDetails.setLedgerCode1(ledgerCode1);
        return this;
    }

    public FinancialJournalVoucherDetailsBuilder withLedgerCode2(String ledgerCode2) {
        financialJournalVoucherDetails.setLedgerCode2(ledgerCode2);
        return this;
    }

    public FinancialJournalVoucherDetails build() {
        return financialJournalVoucherDetails;
    }
}
