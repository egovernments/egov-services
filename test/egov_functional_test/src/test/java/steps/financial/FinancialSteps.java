package steps.financial;

import cucumber.api.java8.En;
import entities.ApprovalDetails;
import entities.financial.*;
import excelDataFiles.ExcelReader;
import excelDataFiles.FinanceDataReader;
import org.junit.Assert;
import pages.financial.*;
import steps.BaseSteps;

import java.text.ParseException;

public class FinancialSteps extends BaseSteps implements En {

    public FinancialSteps() {

        And("^officer will enter the journal voucher details as (\\w+) with subledger (\\w+)$", (String voucher, String withOrWithoutSubledger) -> {
            FinancialJournalVoucherDetails financialJournalVoucherDetails = new FinanceDataReader(financialTestDataFileName).getJournalVoucherDetails(voucher);
            pageStore.get(JournalVoucherDetailsPage.class).enterJournalVoucherDetails(financialJournalVoucherDetails, withOrWithoutSubledger);
        });

        And("^officer will enter the approval details as (\\w+)$", (String approveOfficer) -> {
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getFinanceApprovalDetails(approveOfficer);
            try {
                String userName = pageStore.get(FinancialPage.class).enterFinanceApprovalDetails(approvalDetails);
                scenarioContext.setUser(userName);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        And("^officer will get successful voucher created and closes it$", () -> {
            String voucherNumber = pageStore.get(JournalVoucherDetailsPage.class).getVoucherNumber();
            scenarioContext.setApplicationNumber(voucherNumber.split("\\.")[0].split("\\ ")[1]);
            scenarioContext.setActualMessage(voucherNumber.split("\\.")[0]);
        });

        And("^officer will closes the acknowledgement page$", () -> {
            String actualMessage = pageStore.get(FinancialPage.class).closePage();
            scenarioContext.setActualMessage(actualMessage);
            if (scenarioContext.getIsRemittance() == 1) {
                scenarioContext.setApplicationNumber(actualMessage.split("\\n")[0].split("\\ ")[7] + "-CASH");
                scenarioContext.setIsRemittance(0);
            }
        });

        And("^officer click on approval of the voucher$", () -> {
            pageStore.get(FinancialPage.class).approvalPage();
        });

        Then("^officer will modify the results depending upon the fund and date$", () -> {
            pageStore.get(SelectSingleOrMultipleBillsPage.class).singleBillSearch();
        });

        Then("^officer will search the bill based on department and fund with type as (\\w+) with payment mode as (\\w+)$", (String type, String paymentMode) -> {
            pageStore.get(SelectSingleOrMultipleBillsPage.class).multipleBillSearch(type, paymentMode);
        });

        And("^officer will act upon the above voucher with payment mode as (\\w+)$", (String paymentMode) -> {
            pageStore.get(SelectSingleOrMultipleBillsPage.class).actOnAboveVoucher(paymentMode, scenarioContext.getApplicationNumber());
        });

        And("^officer will verify the voucher number$", () -> {
            String voucher = pageStore.get(FinancialPage.class).verifyVoucher();
            Assert.assertEquals(voucher, scenarioContext.getApplicationNumber());
        });

        And("^officer will enter the bank details$", () -> {
            String bankDetails = "SBI";
            FinancialBankDetails financialBankDetails = new FinanceDataReader(financialTestDataFileName).getFinancialBankDetails(bankDetails);
            pageStore.get(FinancialPage.class).billPayment(financialBankDetails);
        });

        And("^officer will enter the remittance bank details$", () -> {
            String bankDetails = "SBI1";
            FinancialBankDetails financialBankDetails = new FinanceDataReader(financialTestDataFileName).getFinancialBankDetails(bankDetails);
            pageStore.get(FinancialPage.class).billRemittancePayment(financialBankDetails);
            scenarioContext.setIsRemittance(1);
        });

        And("^officer will the expense bill details as (\\w+)$", (String expenseBill) -> {
            FinancialExpenseBillDetails financialBill = new FinanceDataReader(financialTestDataFileName).getFinancialExpenseBillDetails(expenseBill);
            pageStore.get(ExpenseDetailsPage.class).createNewExpenseBill(financialBill);
        });

        And("^officer will enter the expense approval details as (\\w+)$", (String approveOfficer) -> {
            ApprovalDetails approvalDetails = new ExcelReader(approvalDetailsTestDataFileName).getFinanceApprovalDetails(approveOfficer);
            String userName = pageStore.get(ExpenseDetailsPage.class).enterExpenseApprovalDetails(approvalDetails);
            scenarioContext.setUser(userName);
        });

        And("^officer will closes the expense acknowledgement page$", () -> {
            String expenseBillNumber = pageStore.get(FinancialPage.class).closesTheExpensePage();
            scenarioContext.setApplicationNumber(expenseBillNumber.split("\\ ")[2]);
            scenarioContext.setActualMessage(expenseBillNumber.split("\\ ")[3]);
        });

        And("^officer will closes the successfull payment page$", () -> {
            String billNUmber = pageStore.get(FinancialPage.class).closesSuccessfulPaymentPage();
            scenarioContext.setApplicationNumber(billNUmber);
        });

        And("^officer will get successful BAN NUMBER created and closes it$", () -> {
            String voucherNumber = pageStore.get(JournalVoucherDetailsPage.class).getVoucherNumber();
            scenarioContext.setApplicationNumber(voucherNumber.split("\\ ")[1]);
            scenarioContext.setActualMessage(voucherNumber);
        });

        And("^user will enter the account code to modify as (\\w+)$", (String glCode) -> {
            pageStore.get(FinancialPage.class).enterAccountCodeToModify(glCode);
        });

        And("^user will map the account code to particular$", () -> {
            pageStore.get(FinancialPage.class).toModifyTheGLCodeAccount();
        });

        And("^officer will search for (\\w+) remittance bill$", (String singleOrMultiple) -> {
            pageStore.get(FinancialPage.class).searchRemittanceBill();
            if (singleOrMultiple.equalsIgnoreCase("single")) {
                pageStore.get(SelectSingleOrMultipleBillsPage.class).selectSingleRemittanceBill(scenarioContext.getApplicationNumber());
            } else {
                pageStore.get(FinancialPage.class).selectMultipleRemittanceBill();
            }
        });

        And("^officer will filter the bill according to the type$", () -> {
            pageStore.get(ExpenseDetailsPage.class).filterCreateVoucherBill(scenarioContext.getApplicationNumber());
        });

        And("^officer will set the new expense voucher number and closes it$", () -> {
            String expenseVoucherMessage = pageStore.get(ExpenseDetailsPage.class).closesExpenseVoucherPage();
            scenarioContext.setApplicationNumber(expenseVoucherMessage.split("\\ ")[4].split("\\.")[0]);
            scenarioContext.setActualMessage(expenseVoucherMessage);
        });

        And("^officer will filter the payment cheque assignment bill as (\\w+)$", (String singleOrMultiple) -> {
            if (singleOrMultiple.equalsIgnoreCase("single")) {
                pageStore.get(FinancialPage.class).chequeAssignmentBillSearch(scenarioContext.getApplicationNumber());
            } else {
                pageStore.get(FinancialPage.class).chequeAssignmentBillSearch();
            }
        });

        And("^officer will select the (\\w+) bill and enter the details (\\w+)$", (String singleOrMultiple, String assignmentMode) -> {
            if (singleOrMultiple.equalsIgnoreCase("single")) {
                pageStore.get(FinancialPage.class).toFillChequeAssignmentDetails(assignmentMode);
            } else {
                pageStore.get(FinancialPage.class).toFillMultipleChequeAssignmentDetails(assignmentMode);
            }
        });

        And("^officer will close the successfull assignment page$", () -> {
            String msg = pageStore.get(FinancialPage.class).closeAssignmentSuccessPage();
            scenarioContext.setActualMessage(msg);
        });

        And("^officer will enter the direct bank payment details as (\\w+) with mode as (\\w+)$", (String directBankDetails, String mode) -> {
            DirectBankPaymentDetails directBankPaymentDetails = new FinanceDataReader(financialTestDataFileName).getDirectBankPaymentDetails(directBankDetails);
            pageStore.get(DirectBankPaymentDetailsPage.class).enterDirectBankPaymentDetails(directBankPaymentDetails, mode);
        });

        And("^officer will see the successful voucher creation page and closes it$", () -> {
            String msg = pageStore.get(DirectBankPaymentDetailsPage.class).directBankSuccessPage();
            if (msg.contains("Successful")) {
                scenarioContext.setApplicationNumber(msg.split("\\ ")[8]);
            }
            scenarioContext.setActualMessage(msg);
        });

        And("^officer will enter the bank to bank transfer details as (\\w+)$", (String bankDetails) -> {
            FinancialBankToBankDetails financialBankToBankDetails = new FinanceDataReader(financialTestDataFileName).getBankToBankTransferDetails(bankDetails);
            pageStore.get(BankToBankDetailsPage.class).enterBankToBankDetails(financialBankToBankDetails);
        });

        And("^officer will close the successful creation page$", () -> {
            String message = pageStore.get(FinancialPage.class).closesSuccessfulTransferCreationPage();
            scenarioContext.setActualMessage(message);
        });

        And("^officer will click on the direct approve button$", () -> {
            pageStore.get(FinancialPage.class).clickOnCreateAndApprove();
        });

        And("^officer will get the successful create and approve page and closes it$", () -> {
            String message = pageStore.get(FinancialPage.class).createAndApproveSuccessPage();
            scenarioContext.setActualMessage(message);
        });
    }
}
