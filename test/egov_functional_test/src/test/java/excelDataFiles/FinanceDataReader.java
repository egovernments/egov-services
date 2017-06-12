package excelDataFiles;

import builders.financial.*;
import entities.financial.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class FinanceDataReader extends ExcelReader {

    Sheet financialJournalVoucherSheet;
    Sheet financialBankDetailsSheet;
    Sheet financialExpenseBillDetailsSheet;
    Sheet directBankPaymentDetailsSheet;
    Sheet bankToBankTransferDetailsSheet;

    public FinanceDataReader(String testData) {
        super(testData);
        financialJournalVoucherSheet = workbook.getSheet("journalVoucherDetails");
        financialBankDetailsSheet = workbook.getSheet("financialBankDetails");
        financialExpenseBillDetailsSheet = workbook.getSheet("financialExpenseBillDetails");
        directBankPaymentDetailsSheet = workbook.getSheet("directBankPaymentDetails");
        bankToBankTransferDetailsSheet = workbook.getSheet("financialBankToBankDetails");
    }

    public FinancialJournalVoucherDetails getJournalVoucherDetails(String voucher) {

        Row dataRow = readDataRow(financialJournalVoucherSheet, voucher);
        String voucherType = getCellData(financialJournalVoucherSheet, dataRow, "voucherType").getStringCellValue();
        String fundId = getCellData(financialJournalVoucherSheet, dataRow, "fund").getStringCellValue();
        String department = getCellData(financialJournalVoucherSheet, dataRow, "department").getStringCellValue();
        String function = getCellData(financialJournalVoucherSheet, dataRow, "function").getStringCellValue();

        String accountCode1 = convertNumericToString(financialJournalVoucherSheet, dataRow, "accountCode1");
        String accountCode2 = convertNumericToString(financialJournalVoucherSheet, dataRow, "accountCode2");
        String accountCode3 = convertNumericToString(financialJournalVoucherSheet, dataRow, "accountCode3");

        String debitAmount1 = convertNumericToString(financialJournalVoucherSheet, dataRow, "debitAmount1");
        String creditAmount2 = convertNumericToString(financialJournalVoucherSheet, dataRow, "creditAmount2");
        String creditAmount3 = convertNumericToString(financialJournalVoucherSheet, dataRow, "creditAmount3");

        String ledgerAmount1 = convertNumericToString(financialJournalVoucherSheet, dataRow, "ledgerAmount1");
        String ledgerAmount2 = convertNumericToString(financialJournalVoucherSheet, dataRow, "ledgerAmount2");

        String ledgerType1 = getCellData(financialJournalVoucherSheet, dataRow, "ledgerType1").getStringCellValue();
        String ledgerType2 = getCellData(financialJournalVoucherSheet, dataRow, "ledgerType2").getStringCellValue();
        String ledgerCode1 = getCellData(financialJournalVoucherSheet, dataRow, "ledgerCode1").getStringCellValue();

        String ledgerCode2 = convertNumericToString(financialJournalVoucherSheet, dataRow, "ledgerCode2");

        return new FinancialJournalVoucherDetailsBuilder()
                .withVoucherType(voucherType)
                .withFundId(fundId)
                .withAccountCode1(accountCode1)
                .withAccountCode2(accountCode2)
                .withAccountCode3(accountCode3)
                .withDepartment(department)
                .withFunction(function)
                .withDebitAmount1(debitAmount1)
                .withCreditAmount2(creditAmount2)
                .withCreditAmount3(creditAmount3)
                .withLedgerAmount1(ledgerAmount1)
                .withLedgerAmount2(ledgerAmount2)
                .withLedgerType1(ledgerType1)
                .withLedgerType2(ledgerType2)
                .withLedgerCode1(ledgerCode1)
                .withLedgerCode2(ledgerCode2)
                .build();
    }

    public FinancialBankDetails getFinancialBankDetails(String bankDetails) {
        Row dataRow = readDataRow(financialBankDetailsSheet, bankDetails);

        String bankName = getCellData(financialBankDetailsSheet, dataRow, "bankName").getStringCellValue();
        String accountNumber = getCellData(financialBankDetailsSheet, dataRow, "accountNumber").getStringCellValue();

        return new FinancialBankDetailsBuilder()
                .withBankName(bankName)
                .withAccountNumber(accountNumber)
                .build();
    }

    public FinancialExpenseBillDetails getFinancialExpenseBillDetails(String expenseBill) {
        Row dataRow = readDataRow(financialExpenseBillDetailsSheet, expenseBill);

        String fund = getCellData(financialExpenseBillDetailsSheet, dataRow, "fund").getStringCellValue();
        String department = getCellData(financialExpenseBillDetailsSheet, dataRow, "department").getStringCellValue();
        String function = getCellData(financialExpenseBillDetailsSheet, dataRow, "function").getStringCellValue();
        String billSubType = getCellData(financialExpenseBillDetailsSheet, dataRow, "billSubType").getStringCellValue();

        String accountCodeDebit = convertNumericToString(financialExpenseBillDetailsSheet, dataRow, "accountCodeDebit");
        String accountCodeCredit = convertNumericToString(financialExpenseBillDetailsSheet, dataRow, "accountCodeCredit");

        String expenseDebitAmount = convertNumericToString(financialExpenseBillDetailsSheet, dataRow, "expenseDebitAmount");
        String expenseCreditAmount = convertNumericToString(financialExpenseBillDetailsSheet, dataRow, "expenseCreditAmount");
        String expenseNetAmount = convertNumericToString(financialExpenseBillDetailsSheet, dataRow, "expenseNetAmount");

        return new FinancialExpenseBillDetailsBuilder()
                .withExpenseFund(fund)
                .withExpenseDepartment(department)
                .withExpenseFunction(function)
                .withBillSubType(billSubType)
                .withExpenseAccountDebit(accountCodeDebit)
                .withExpenseAccountCredit(accountCodeCredit)
                .withExpenseDebitAmount(expenseDebitAmount)
                .withExpenseCreditAmount(expenseCreditAmount)
                .withExpenseNetAmount(expenseNetAmount)
                .build();
    }

    public DirectBankPaymentDetails getDirectBankPaymentDetails(String directBankDetails) {

        Row dataRow = readDataRow(directBankPaymentDetailsSheet, directBankDetails);

        String fundId = getCellData(directBankPaymentDetailsSheet, dataRow, "fundId").getStringCellValue();
        String voucherDepartment = getCellData(directBankPaymentDetailsSheet, dataRow, "voucherDepartment").getStringCellValue();
        String voucherFunction = getCellData(directBankPaymentDetailsSheet, dataRow, "voucherFunction").getStringCellValue();
        String bankBranch = getCellData(directBankPaymentDetailsSheet, dataRow, "bankBranch").getStringCellValue();
        String accountNumber = getCellData(directBankPaymentDetailsSheet, dataRow, "accountNumber").getStringCellValue();
        String ledgerType1 = getCellData(directBankPaymentDetailsSheet, dataRow, "ledgerType1").getStringCellValue();
        String ledgerCode1 = getCellData(directBankPaymentDetailsSheet, dataRow, "ledgerCode1").getStringCellValue();

        String amount = convertNumericToString(directBankPaymentDetailsSheet, dataRow, "amount");
        String accountCode1 = convertNumericToString(directBankPaymentDetailsSheet, dataRow, "accountCode1");
        String debitAmount1 = convertNumericToString(directBankPaymentDetailsSheet, dataRow, "debitAmount1");

        String ledgerAccount1 = convertNumericToString(directBankPaymentDetailsSheet, dataRow, "ledgerAccount1");
        String ledgerAmount1 = convertNumericToString(directBankPaymentDetailsSheet, dataRow, "ledgerAmount1");

        return new DirectBankPaymentDetailsBuilder()
                .withFundId(fundId)
                .withVoucherDepartment(voucherDepartment)
                .withVoucherFunction(voucherFunction)
                .withBankBranch(bankBranch)
                .withAccountNumber(accountNumber)
                .withLedgerType1(ledgerType1)
                .withLedgerCode1(ledgerCode1)
                .withAmount(amount)
                .withAccountCode1(accountCode1)
                .withDebitAmount1(debitAmount1)
                .withLedgerAccount1(ledgerAccount1)
                .withLedgerAmount1(ledgerAmount1)
                .build();
    }

    public FinancialBankToBankDetails getBankToBankTransferDetails(String bankDetails) {

        Row dataRow = readDataRow(bankToBankTransferDetailsSheet, bankDetails);

        String fundId = getCellData(bankToBankTransferDetailsSheet, dataRow, "fundId").getStringCellValue();
        String voucherDepartment = getCellData(bankToBankTransferDetailsSheet, dataRow, "voucherDepartment").getStringCellValue();
        String voucherFunction = getCellData(bankToBankTransferDetailsSheet, dataRow, "voucherFunction").getStringCellValue();
        String fromBank = getCellData(bankToBankTransferDetailsSheet, dataRow, "fromBank").getStringCellValue();
        String fromAccountNumber = getCellData(bankToBankTransferDetailsSheet, dataRow, "fromAccountNumber").getStringCellValue();
        String toFundId = getCellData(bankToBankTransferDetailsSheet, dataRow, "toFundId").getStringCellValue();
        String toBank = getCellData(bankToBankTransferDetailsSheet, dataRow, "toBank").getStringCellValue();
        String toAccountNumber = getCellData(bankToBankTransferDetailsSheet, dataRow, "toAccountNumber").getStringCellValue();
        String amount = convertNumericToString(bankToBankTransferDetailsSheet, dataRow, "amount");

        return new FinancialBankToBankDetailsBuilder()
                .withFundId(fundId)
                .withVoucherDepartment(voucherDepartment)
                .withVoucherFunction(voucherFunction)
                .withFromBank(fromBank)
                .withFromAccountNumber(fromAccountNumber)
                .withToFundId(toFundId)
                .withToBank(toBank)
                .withToAccountNumber(toAccountNumber)
                .withAmount(amount)
                .build();
    }
}
