package pages.financial;

import entities.ApprovalDetails;
import entities.financial.FinancialBankDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

import java.text.ParseException;
import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class FinancialPage extends BasePage {

    protected WebDriver webDriver;

    @FindBy(id = "fundId")
    private WebElement fundId;

    @FindBy(id = "totalcramount")
    private WebElement totalCreditAmount;

    @FindBy(id = "approverDepartment")
    private WebElement approverDepartment;

    @FindBy(id = "approverDesignation")
    private WebElement approverDesignation;

    @FindBy(id = "approverPositionId")
    private WebElement approverPosition;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(css = "'div[class~='bootbox-alert'] button[class^='btn']'")
    private WebElement okButton;

    @FindBy(id = "button2")
    private WebElement closeButton;

    @FindBy(id = "Approve")
    private WebElement approveButton;

    @FindBy(id = "searchBtn")
    private WebElement billSearch;

    @FindBy(id = "search")
    private WebElement remittanceBillSearch;

    @FindBy(className = "actionMessage")
    private WebElement forwardMessage;

    @FindBy(id = "voucherTypeBean.partyName")
    private WebElement voucherPartyName;

    @FindBy(css = "input[type='text'][id='voucherDate']")
    private WebElement voucherDate;

    @FindBy(id = "Create And Approve")
    private WebElement createAndApprove;

    @FindBy(name = "contingentList[0].isSelected")
    private WebElement firstVoucher;

    @FindBy(name = "contingentList[1].isSelected")
    private WebElement secondVoucher;

    @FindBy(id = "generatePayment")
    private WebElement generatePayment;

    @FindBy(linkText = "Bill Details")
    private WebElement billDetails;

    @FindBy(linkText = "Payment Details")
    private WebElement paymentDetails;

    @FindBy(name = "billList[0].billVoucherId")
    private WebElement voucherNumberToVerify;

    @FindBy(id = "bankbranch")
    private WebElement bankBranch;

    @FindBy(id = "bankaccount")
    private WebElement bankAccount;

    @FindBy(id = "bank")
    private WebElement bankBranch1;

    @FindBy(id = "billSubType")
    private WebElement expenseBillSubType;

    @FindBy(id = "subLedgerType")
    private WebElement subLedgerType;

    @FindBy(css = ".panel-title.text-center")
    private WebElement expenseCreatedMessage;

    @FindBy(id = "glCode")
    private WebElement glCode;

    @FindBy(id = "Search")
    private WebElement modifyAndSearch;

    @FindBy(id = "chartOfAccounts_accountDetailTypeList")
    private WebElement accountDetailType;

    @FindBy(id = "recoveryId")
    private WebElement recoveryId;

    @FindBy(id = "genPayment")
    private WebElement remittancePayment;

    @FindBy(css = "input[type='text'][id='vouchernumber']")
    private WebElement BPVNumber;

    @FindBy(name = "voucherNumber")
    private WebElement voucherNumberSearch;

    @FindBy(id = "bank_branch")
    private WebElement bankBranch2;

    @FindBy(id = "isSelected0")
    private WebElement firstBill;

    @FindBy(id = "expSelectAll")
    private WebElement selectAllBillsFromExpense;

    @FindBy(name = "chequeAssignmentList[0].chequeNumber")
    private WebElement checkAssignmentNumberBox;

    @FindBy(id = "assignChequeBtn")
    private WebElement assignChequeButton;

    @FindBy(css = ".form-control.datepicker")
    private WebElement rtgsDate;

    @FindBy(id = "departmentid")
    private WebElement remittanceAssignmentDepartment;

    @FindBy(css = "input[type='text'][name='chequeNo']")
    private WebElement remittanceChequeAssignmentNumber;

    @FindBy(name = "chequeDt")
    private WebElement remittanceChequeDate;

    @FindBy(name = "inFavourOf")
    private WebElement remittanceFavour;

    @FindBy(id = "closeButton")
    private WebElement bankCloseButton;

    @FindBy(id = "cbilltab")
    private WebElement expenseBillTable;

    @FindBy(id = "voucherTypeBean.billNum")
    private WebElement billNumberFromPreviewScreen;

    @FindBy(css = "input[type='button'][value='Close']")
    private WebElement closeButtonWithCSS;

    @FindBy(css = ".buttonsubmit")
    private WebElement submitButton;

    @FindBy(id = "Create And Approve")
    private WebElement createAndApproveButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement okButton1;

    @FindBy(id = "selectAll")
    private WebElement selectAllBillsFromRemittanceRecovery;

    @FindBy(id = "selectall")
    private WebElement selectAllBillsfromAssignmentPage;

    private List<WebElement> voucherRows;

    private String userName = "";

    public FinancialPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String enterFinanceApprovalDetails(ApprovalDetails approvalDetails) throws ParseException {

        maximizeBrowserWindow(webDriver);

        selectFromDropDown(approverDepartment, approvalDetails.getApproverDepartment(), webDriver);
        selectFromDropDown(approverDesignation, approvalDetails.getApproverDesignation(), webDriver);

        Select approverPos = new Select(approverPosition);
        approverPosition.click();
        await().atMost(10, SECONDS).until(() -> approverPos.getOptions().size() > 1);
        checkApprovalPositionIsLoadedOrNot(approverPosition);

        if (approverPos.getOptions().get(1).getText().split("\\ ")[0].length() == 1) {
            userName = approverPos.getOptions().get(1).getText().split("\\ ")[0] + " " + approverPos.getOptions().get(1).getText().split("\\ ")[1];
        } else {
            userName = approverPos.getOptions().get(1).getText().split("\\ ")[0];
        }
        clickOnButton(approverPos.getOptions().get(1), webDriver);

        clickOnForwardButton();

        isAlertOpened();
        return userName;
    }

    private void checkApprovalPositionIsLoadedOrNot(WebElement element) {
        if (element.getText().equals(null)) {
            clickOnButton(element, webDriver);
            clickOnButton(element, webDriver);
        }
    }

    public String closePage() {
        switchToNewlyOpenedWindow(webDriver);

        String message = getForwardMessage();

        List<WebElement> closeElements = webDriver.findElements(By.className("button"));
        waitForElementToBeClickable(closeElements.get(1), webDriver);
        closeElements.get(1).click();

        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }

    public String verifyVoucher() {

        clickOnButton(billDetails, webDriver);
        String voucherNumber = getTextFromWeb(voucherNumberToVerify, webDriver);
        clickOnButton(paymentDetails, webDriver);

        return voucherNumber;
    }

    public void billPayment(FinancialBankDetails financialBankDetails) {
        maximizeBrowserWindow(webDriver);
        enterBankBranchDetails(financialBankDetails);
        enterBankAccountDetails(financialBankDetails);
    }

    private void enterBankBranchDetails(FinancialBankDetails financialBankDetails) {
        selectFromDropDown(bankBranch, financialBankDetails.getBankName(), webDriver);
    }

    private void enterBankBranchDetails1(FinancialBankDetails financialBankDetails) {
        selectFromDropDown(bankBranch1, financialBankDetails.getBankName(), webDriver);
    }

    private void enterBankAccountDetails(FinancialBankDetails financialBankDetails) {
        selectFromDropDown(bankAccount, financialBankDetails.getAccountNumber(), webDriver);
    }

    public void billRemittancePayment(FinancialBankDetails financialBankDetails) {
        maximizeBrowserWindow(webDriver);
        enterBankBranchDetails1(financialBankDetails);
        enterBankAccountDetails(financialBankDetails);
    }

    public String closesTheExpensePage() {
        String message = getTextFromWeb(expenseCreatedMessage, webDriver);
        clickOnCloseButton();
        switchToPreviouslyOpenedWindow(webDriver);

        return message;
    }

    public String closesSuccessfulPaymentPage() {
        switchToNewlyOpenedWindow(webDriver);

        WebElement element = webDriver.findElement(By.xpath(".//*[@id='payment']/div[1]/table/tbody/tr/td/div/table/tbody/tr/td/div/div[1]/span/table/tbody/tr[5]/td[3]"));
        waitForElementToBeVisible(element, webDriver);
        String billNumber = element.getText();

        clickOnCloseButton();

        switchToPreviouslyOpenedWindow(webDriver);
        return billNumber;
    }

    public void enterAccountCodeToModify(String code) {
        enterText(glCode, code, webDriver);
        clickOnButton(modifyAndSearch, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void toModifyTheGLCodeAccount() {

        waitForElementToBeClickable(accountDetailType, webDriver);
        List<WebElement> element = accountDetailType.findElements(By.tagName("option"));
        if (!element.get(5).isSelected()) {
            element.get(5).click();
        }

        clickOnSubmitButton();
        switchToNewlyOpenedWindow(webDriver);

        clickOnButton(closeButtonWithCSS, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void searchRemittanceBill() {
        selectFromDropDown(recoveryId, "3502002-GPF –Employees on Deputation", webDriver);
        selectFromDropDown(fundId, "Municipal Fund", webDriver);
        clickOnButton(remittanceBillSearch, webDriver);
    }

    public void chequeAssignmentBillSearch(String number) {
        if (!number.contains("-CASH")) {
            enterText(voucherNumberSearch, number, webDriver);
        } else {
            enterText(voucherNumberSearch, number.split("\\-")[0], webDriver);
        }
        enterBankDetailsWithFund();
        clickOnBillSearch();
        switchToNewlyOpenedWindow(webDriver);
    }

    private void enterBankDetailsWithFund() {
        maximizeBrowserWindow(webDriver);
        selectFromDropDown(fundId, "Municipal Fund", webDriver);
        selectFromDropDown(bankBranch2, "ANDHRA BANK Andhra Bank RTC Busstand", webDriver);
        selectFromDropDown(bankAccount, "4502110--110710100009664--ANDHRA BANK", webDriver);
    }

    public void chequeAssignmentBillSearch() {
        enterBankDetailsWithFund();
        clickOnBillSearch();
        switchToNewlyOpenedWindow(webDriver);
    }

    public void toFillChequeAssignmentDetails(String assignment) {

        switch (assignment) {

            case "cheque":

                clickOnButton(firstBill, webDriver);
                enterText(checkAssignmentNumberBox, get6DigitRandomInt(), webDriver);
                break;

            case "RTGS":

                clickOnButton(firstBill, webDriver);
                enterDate(rtgsDate, getCurrentDate(), webDriver);
                break;

            case "remittance":

                clickOnButton(firstBill, webDriver);
                selectFromDropDown(remittanceAssignmentDepartment, "ACCOUNTS", webDriver);
                enterText(remittanceChequeAssignmentNumber, get6DigitRandomInt(), webDriver);
                enterText(remittanceChequeDate, getCurrentDate(), webDriver);
                enterText(remittanceFavour, "Testing", webDriver);
                break;
        }
        clickOnButton(assignChequeButton, webDriver);
    }

    public String closeAssignmentSuccessPage() {
        String message = getForwardMessage();
        clickOnSubmitButton();
        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }

    public String closesSuccessfulTransferCreationPage() {

        String message = getForwardMessage();

        clickOnBankCloseButton();
        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }

    public void clickOnCreateAndApprove() {

        clickOnButton(createAndApproveButton, webDriver);
        // This method is required on bank details page if there is any less balance
        // in bank it will open the alert and accepts it and it is not required for other screens
        isAlertOpened();

        switchToNewlyOpenedWindow(webDriver);
    }

    private void isAlertOpened() {
        try {
            webDriver.switchTo().alert().accept();
        } catch (NoAlertPresentException Ex) {
            System.out.println(" ");
        }
    }

    public String createAndApproveSuccessPage() {
        switchToNewlyOpenedWindow(webDriver);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);

        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div[class~='bootbox-alert'] div[class^='bootbox-body']")));
        WebElement voucherNumber = webDriver.findElement(By.cssSelector("div[class~='bootbox-alert'] div[class^='bootbox-body']"));
        String message = voucherNumber.getText();

        clickOnButton(okButton1, webDriver);
        clickOnCloseButton();

        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }

    public void selectMultipleRemittanceBill() {

        if (webDriver.findElement(By.id("listRemitBean[0].chkremit")).isDisplayed()) {
            clickOnButton(selectAllBillsFromRemittanceRecovery, webDriver);
        } else {
            throw new RuntimeException("No Remittance Recovery Bills found");
        }

        clickOnRemittancePaymentButton();
        switchToNewlyOpenedWindow(webDriver);
    }

    public void toFillMultipleChequeAssignmentDetails(String assignmentMode) {

        switch (assignmentMode) {

            case "cheque":

                if (firstBill.isDisplayed()) {
                    clickOnButton(selectAllBillsfromAssignmentPage, webDriver);
                } else {
                    throw new RuntimeException("No Bills are found");
                }
                WebElement paymentTable = webDriver.findElement(By.id("paymentTable"));
                List<WebElement> chequeRows = paymentTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

                for (int i = 0; i < (chequeRows.size() - 1); i++) {
                    WebElement element = webDriver.findElement(By.name("chequeAssignmentList[" + i + "].chequeNumber"));
                    waitForElementToBeClickable(element, webDriver);
                    element.sendKeys(get6DigitRandomInt());
                }
                break;

            case "RTGS":

                if (firstBill.isDisplayed()) {
                    clickOnButton(selectAllBillsfromAssignmentPage, webDriver);
                } else {
                    throw new RuntimeException("No Bills are found");
                }
                enterDate(rtgsDate, getCurrentDate(), webDriver);
                break;

            case "remittance":

                if (firstBill.isDisplayed()) {
                    clickOnButton(selectAllBillsfromAssignmentPage, webDriver);
                } else {
                    throw new RuntimeException("No Bills are found");
                }
                selectFromDropDown(remittanceAssignmentDepartment, "ACCOUNTS", webDriver);
                enterText(remittanceChequeAssignmentNumber, get6DigitRandomInt(), webDriver);
                enterDate(remittanceChequeDate, getCurrentDate(), webDriver);
                enterText(remittanceFavour, "Testing", webDriver);
                break;
        }
        clickOnButton(assignChequeButton, webDriver);
    }

    private void clickOnCloseButton() {
        clickOnButton(closeButton, webDriver);
    }

    public void approvalPage() {
        clickOnButton(approveButton, webDriver);
    }

    private void clickOnForwardButton() {
        clickOnButton(forwardButton, webDriver);
    }

    private void clickOnSubmitButton() {
        clickOnButton(submitButton, webDriver);
    }

    private void clickOnBillSearch() {
        clickOnButton(billSearch, webDriver);
    }

    private void clickOnBankCloseButton() {
        clickOnButton(bankCloseButton, webDriver);
    }

    private void clickOnRemittancePaymentButton() {
        clickOnButton(remittancePayment, webDriver);
    }

    private String getForwardMessage() {
        return getTextFromWeb(forwardMessage, webDriver);
    }
}
