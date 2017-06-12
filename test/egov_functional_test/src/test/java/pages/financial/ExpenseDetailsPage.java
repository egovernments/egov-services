package pages.financial;

import entities.ApprovalDetails;
import entities.financial.FinancialExpenseBillDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ExpenseDetailsPage extends FinancialPage {

    @FindBy(id = "fund")
    private WebElement expenseFund;

    @FindBy(id = "department")
    private WebElement expenseDepartment;

    @FindBy(id = "function")
    private WebElement expenseFunction;

    @FindBy(id = "billSubType")
    private WebElement expenseBillSubType;

    @FindBy(id = "payTo")
    private WebElement expensePayTo;

    @FindBy(id = "tempDebitDetails[0].debitGlcode")
    private WebElement expenseAccountCodeDebit;

    @FindBy(className = "tt-highlight")
    private WebElement expenseBillAccountCodeDropdown;

    @FindBy(id = "tempDebitDetails[0].debitamount")
    private WebElement expenseDebitAmount;

    @FindBy(id = "tempCreditDetails[0].creditGlcode")
    private WebElement expenseAccountCodeCredit;

    @FindBy(id = "tempCreditDetails[0].creditamount")
    private WebElement expenseCreditAmount;

    @FindBy(id = "netPayableAccountCode")
    private WebElement expenseNetPayable;

    @FindBy(css = "input[id='netPayableAmount'][type='text']")
    private WebElement expenseNetAmount;

    @FindBy(id = "populateAccountDetails")
    private WebElement expensePopulate;

    @FindBy(id = "approvalDepartment")
    private WebElement expenseApprovalDepartment;

    @FindBy(id = "approvalDesignation")
    private WebElement expenseApprovalDesignation;

    @FindBy(id = "approvalPosition")
    private WebElement expenseApprovalPosition;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(id = "expType")
    private WebElement billType;

    @FindBy(id = "billNumber")
    private WebElement billNumberTextBox;

    @FindBy(css = ".buttonsubmit")
    private WebElement submitButton;

    @FindBy(id = "button2")
    private WebElement closeButton;

    @FindBy(className = "actionMessage")
    private WebElement forwardMessage;

    @FindBy(id = "subLedgerType")
    private WebElement expenseSubLedgerType;

    public ExpenseDetailsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void createNewExpenseBill(FinancialExpenseBillDetails financialExpenseBillDetails) {

        selectFromDropDown(expenseFund, financialExpenseBillDetails.getExpenseFund(), webDriver);
        selectFromDropDown(expenseDepartment, financialExpenseBillDetails.getExpenseDeparment(), webDriver);

        waitForElementToBeClickable(expenseFunction, webDriver);
        expenseFunction.sendKeys(financialExpenseBillDetails.getExpenseFunction());
        waitForElementToBeVisible(webDriver.findElement(By.className("tt-dropdown-menu")), webDriver);
        clickOnButton(webDriver.findElement(By.className("tt-dataset-0")), webDriver);

        selectFromDropDown(expenseBillSubType, financialExpenseBillDetails.getExpenseBillSubType(), webDriver);
        selectFromDropDown(expenseSubLedgerType, "Contigent Bill", webDriver);
        enterText(webDriver.findElement(By.id("subLedgerCode")), "ADMN0009", webDriver);

        clickOnButton(webDriver.findElement(By.className("tt-dataset-3")), webDriver);
        enterText(expensePayTo, "tester", webDriver);
        enterText(expenseAccountCodeDebit, financialExpenseBillDetails.getExpenseAccountCodeDebit(), webDriver);

        clickOnButton(expenseBillAccountCodeDropdown, webDriver);
        enterText(expenseDebitAmount, financialExpenseBillDetails.getExpenseDebitAmount(), webDriver);
        enterText(expenseAccountCodeCredit, financialExpenseBillDetails.getExpenseAccountCodeCredit(), webDriver);
        clickOnButton(expenseBillAccountCodeDropdown, webDriver);

        enterText(expenseCreditAmount, financialExpenseBillDetails.getExpenseCreditAmount(), webDriver);

        List<WebElement> element1 = expenseNetPayable.findElements(By.tagName("option"));
        waitForElementToBeClickable(element1.get(1), webDriver);
        element1.get(1).click();

        enterText(expenseNetAmount, financialExpenseBillDetails.getExpenseNetAmount(), webDriver);

        clickOnButton(expensePopulate, webDriver);
    }

    public String enterExpenseApprovalDetails(ApprovalDetails approvalDetails) {

        selectFromDropDown(expenseApprovalDepartment, approvalDetails.getApproverDepartment(), webDriver);

        for (int i = 0; i <= 10; i++) {
            if (!webDriver.findElement(By.id("approvalDesignation")).getText().equalsIgnoreCase(approvalDetails.getApproverDesignation())) {
                try {
                    selectFromDropDown(expenseApprovalDesignation, approvalDetails.getApproverDesignation(), webDriver);
                } catch (StaleElementReferenceException e) {
                    WebElement element = webDriver.findElement(By.id("approvalDesignation"));
                    selectFromDropDown(element, approvalDetails.getApproverDesignation(), webDriver);
                }
            }
        }

        Select approverPosition = new Select(expenseApprovalPosition);
        await().atMost(10, SECONDS).until(() -> approverPosition.getOptions().size() > 1);
        String userName;
        if (approverPosition.getOptions().get(1).getText().split("\\ ")[0].length() == 1) {
            userName = approverPosition.getOptions().get(1).getText().split("\\ ")[0] + " " + approverPosition.getOptions().get(1).getText().split("\\ ")[1];
        } else {
            userName = approverPosition.getOptions().get(1).getText().split("\\ ")[0];
        }
        clickOnButton(approverPosition.getOptions().get(1), webDriver);

        clickOnButton(forwardButton, webDriver);

        switchToNewlyOpenedWindow(webDriver);
        return userName;
    }

    public void filterCreateVoucherBill(String applicationNumber) {

        selectFromDropDown(billType, "Expense", webDriver);
        enterText(billNumberTextBox, applicationNumber, webDriver);

        clickOnButton(submitButton, webDriver);

        getExpenseVoucherRow(applicationNumber).click();
        switchToNewlyOpenedWindow(webDriver);
    }

    private WebElement getExpenseVoucherRow(String applicationNumber) {
        WebElement element = webDriver.findElement(By.className("tablebottom"));
        List<WebElement> elements = element.findElements(By.className("setborder"));

        for (WebElement applicationRow : elements) {
            if (applicationRow.findElements(By.tagName("td")).get(1).getText().contains(applicationNumber))
                return applicationRow.findElements(By.tagName("td")).get(1);
        }
        throw new RuntimeException("No application row found for -- " + applicationNumber);
    }

    public String closesExpenseVoucherPage() {

        String message = getTextFromWeb(forwardMessage, webDriver);

        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }
}
