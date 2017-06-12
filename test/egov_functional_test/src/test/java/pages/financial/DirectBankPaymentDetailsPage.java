package pages.financial;

import entities.financial.DirectBankPaymentDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DirectBankPaymentDetailsPage extends FinancialPage {

    @FindBy(id = "fundId")
    private WebElement fundId;

    @FindBy(id = "vouchermis.departmentid")
    private WebElement voucherDepartment;

    @FindBy(id = "vouchermis.function")
    private WebElement voucherFunction;

    @FindBy(id = "bankId")
    private WebElement bankPaymentId;

    @FindBy(id = "amount")
    private WebElement amountTextBox;

    @FindBy(id = "accountNumber")
    private WebElement accountNumber;

    @FindBy(id = "modeOfPaymentcash")
    private WebElement bankPaymentCash;

    @FindBy(id = "modeOfPaymentrtgs")
    private WebElement bankPaymentRTGS;

    @FindBy(id = "paidTo")
    private WebElement paidToCustomer;

    @FindBy(id = "commonBean.documentNumber")
    private WebElement documentNumber;

    @FindBy(id = "documentDate")
    private WebElement documentDate;

    @FindBy(id = "billDetailslist[0].glcodeDetail")
    private WebElement accountCode1;

    @FindBy(className = "yui-ac-highlight")
    private WebElement accountCodeDropdown;

    @FindBy(id = "billDetailslist[0].debitAmountDetail")
    private WebElement debitAmount1;

    @FindBy(id = "subLedgerlist[0].glcode.id")
    private WebElement ledgerAccount1;

    @FindBy(id = "subLedgerlist[0].detailType.id")
    private WebElement ledgerType1;

    @FindBy(id = "subLedgerlist[0].detailCode")
    private WebElement ledgerCode1;

    @FindBy(id = "subLedgerlist[0].amount")
    private WebElement ledgerAmount1;

    @FindBy(className = "actionMessage")
    private WebElement forwardMessage;

    @FindBy(id = "closeButton")
    private WebElement bankCloseButton;

    @FindBy(id = "button2")
    private WebElement closeButton;

    public DirectBankPaymentDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void enterDirectBankPaymentDetails(DirectBankPaymentDetails directBankPaymentDetails, String mode) {

        selectFromDropDown(fundId, directBankPaymentDetails.getFundId(), webDriver);
        selectFromDropDown(voucherDepartment, directBankPaymentDetails.getVoucherDepartment(), webDriver);
        selectFromDropDown(voucherFunction, directBankPaymentDetails.getVoucherFunction(), webDriver);
        selectFromDropDown(bankPaymentId, directBankPaymentDetails.getBankBranch(), webDriver);

        enterText(amountTextBox, directBankPaymentDetails.getAmount(), webDriver);

        selectFromDropDown(accountNumber, directBankPaymentDetails.getAccountNumber(), webDriver);

        switch (mode) {
            case "cheque":
                break;

            case "cash":
                clickOnButton(bankPaymentCash, webDriver);
                break;

            case "RTGS":
                clickOnButton(bankPaymentRTGS, webDriver);
                break;
        }

        enterText(paidToCustomer, "Tester", webDriver);
        enterText(documentNumber, get6DigitRandomInt(), webDriver);
        enterDate(documentDate, getCurrentDate(), webDriver);
        enterText(accountCode1, directBankPaymentDetails.getAccountCode1(), webDriver);

        clickOnButton(accountCodeDropdown, webDriver);

        enterText(debitAmount1, directBankPaymentDetails.getDebitAmount1(), webDriver);

        selectFromDropDown(ledgerAccount1, directBankPaymentDetails.getLedgerAccount1(), webDriver);
        selectFromDropDown(ledgerType1, directBankPaymentDetails.getLedgerType1(), webDriver);

        enterText(ledgerCode1, directBankPaymentDetails.getLedgerCode1(), webDriver);

        clickOnButton(accountCodeDropdown, webDriver);

        enterText(ledgerAmount1, directBankPaymentDetails.getLedgerAmount1(), webDriver);
    }

    public String directBankSuccessPage() {
        switchToNewlyOpenedWindow(webDriver);
        String message = getTextFromWeb(forwardMessage.findElements(By.tagName("li")).get(0), webDriver);

        if (message.contains("Successful")) {
            clickOnButton(bankCloseButton, webDriver);
        } else {
            clickOnButton(closeButton, webDriver);
        }
        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }
}
