package pages.collections;

import entities.collections.PaymentMethod;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import pages.BasePage;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MiscellaneousPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "paidBy")
    private WebElement paidByTextBox;

    @FindBy(id = "referenceDesc")
    private WebElement narrationTextBox;

    @FindBy(id = "payeeAddress")
    private WebElement payeeAddressTextBox;

    @FindBy(id = "serviceCategoryid")
    private WebElement serviceCategoryDropDown;

    @FindBy(id = "serviceId")
    private WebElement serviceTypeIDropDown;

    @FindBy(css = "input[type='text'][name='instrumentProxyList[0].instrumentNumber']")
    private WebElement chequeNumberTextBox;

    @FindBy(css = "input[type='radio'][id='chequeradiobutton']")
    private WebElement chequeModeRadioButton;

    @FindBy(css = "input[type='text'][id='instrumentDate']")
    private WebElement chequeDateTextBox;

    @FindBy(css = "input[type='text'][name='instrumentProxyList[0].bankId.name']")
    private WebElement bankNameTextBox;

    @FindBy(xpath = ".//*[@id='instrumentChequeAmount']")
    private WebElement amountTextBox;

    @FindBy(css = "input[type='submit'][value='Pay']")
    private WebElement payButton;

    @FindBy(css = "input[type='radio'][id='ddradiobutton']")
    private WebElement ddModeRadioButton;

    @FindBy(css = "input[value = 'Submit All Collections'][type='submit']")
    private WebElement submitAllCollectionsButton;

    @FindBy(css = "input[type = 'button'][value = 'Close']")
    private WebElement closeButton;

    @FindBy(css = "input[value = 'Approve All Collections'][type = 'submit']")
    private WebElement approveAllCollectionsButton;

    @FindBy(id = "serviceClass")
    private WebElement classificationBox;

    @FindBy(id = "serviceType")
    private WebElement serviceTypeTextBox;

    @FindBy(css = "input[id='fromDate'][type='text']")
    private WebElement fromDateTextBox;

    @FindBy(css = "input[id='toDate'][type='text']")
    private WebElement toDateTextBox;

    @FindBy(css = "input[value = 'Search'][type='submit']")
    private WebElement searchButton;

    @FindBy(css = "input[value = 'Cancel Receipt'][type = 'button']")
    private WebElement cancelReceiptButton;

    @FindBy(id = "reasonForCancellation")
    private WebElement reasonForCancellationTextBox;

    @FindBy(id = "bankBranchMaster")
    private WebElement bankNameBox;

    @FindBy(id = "accountNumberId")
    private WebElement bankAccountNumberBox;

    @FindBy(id = "paymentMode")
    private WebElement paymentModeBox;

    @FindBy(xpath = ".//*[@id='receiptIds']")
    private WebElement receiptCheckBox;

    @FindBy(css = "input[id='remittanceDate'][type='text']")
    private WebElement remitDateTextBox;

    @FindBy(css = "input[value='Remit to Bank'][type='submit']")
    private WebElement remitToBankButton;

    @FindBy(className = "mainheading")
    private WebElement successMessageTextOfRemittance;

    @FindBy(css = "input[id='bankradiobutton'][type='radio']")
    private WebElement directBankRadioButton;

    @FindBy(id = "bankBranchMaster")
    private WebElement bankNameDropBox;

    @FindBy(id = "accountNumberMaster")
    private WebElement accountNumberDropBox;

    @FindBy(css = "input[id='bankChallanDate'][type='text']")
    private WebElement challanDateTextBox;

    @FindBy(css = "input[id='instrHeaderBank.transactionNumber'][type='text']")
    private WebElement referenceNumberTextBox;

    @FindBy(css = "input[id='instrHeaderBank.instrumentAmount'][type='text']")
    private WebElement directBankAmountTextBox;

    @FindBy(className = "subheadnew")
    private WebElement submissionMessage;

    public MiscellaneousPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterMiscellaneousDetails() {
        enterText(paidByTextBox, "Bimal kumar", driver);
        enterText(narrationTextBox, "Narration", driver);
        enterText(payeeAddressTextBox, "Banglore", driver);
        selectFromDropDown(serviceCategoryDropDown, "Entry Fees", driver);
        await().atMost(10, SECONDS).until(() -> new Select(serviceTypeIDropDown).getOptions().size() > 1);
        selectFromDropDown(serviceTypeIDropDown, "Monuments Entry Fees-MNMENTFEE", driver);
        for (int i = 0; i < 4; i++) {
            try {
                WebElement amountBox = driver.findElement(By.cssSelector("input[type='text'][id='billCreditDetailslist[0].creditAmountDetail']"));
                enterText(amountBox, "655", driver);
            } catch (StaleElementReferenceException e) {
                WebElement amountBox = driver.findElement(By.cssSelector("input[type='text'][id='billCreditDetailslist[0].creditAmountDetail']"));
                enterText(amountBox, "655", driver);
            }
        }
    }

    public void enterPaymentDetails(PaymentMethod paymentmethod, String mode) {

        switch (mode) {

            case "cash":

                break;

            case "cheque":
                jsClick(chequeModeRadioButton, driver);
                enterText(chequeNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterDate(chequeDateTextBox, getCurrentDate(), driver);
                waitForElementToBeClickable(bankNameTextBox, driver);
                enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
                await().atMost(10, SECONDS).until(() -> driver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());
                enterText(amountTextBox, "655", driver);
                break;

            case "dd":
                jsClick(ddModeRadioButton, driver);
                enterText(chequeNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterDate(chequeDateTextBox, getCurrentDate(), driver);
                waitForElementToBeClickable(bankNameTextBox, driver);
                enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
                await().atMost(10, SECONDS).until(() -> driver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());
                enterText(amountTextBox, "655", driver);
                break;

            case "directBank1":
                jsClick(directBankRadioButton, driver);
                enterText(referenceNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterDate(challanDateTextBox, getCurrentDate(), driver);
                for (int i = 0; i < 10; i++) {
                    selectFromDropDown(bankNameDropBox, paymentmethod.getBankName(), driver);
                }
                for (int i = 0; i < 10; i++) {
                    selectFromDropDown(accountNumberDropBox, paymentmethod.getAccountNumber(), driver);
                }
                enterText(directBankAmountTextBox, "655", driver);
                break;
        }
        clickOnButton(payButton, driver);
    }

    public String submitAllCollections() {
        jsClick(submitAllCollectionsButton, driver);
        return submissionMessage.getText();
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public String approveAllCollections() {
        jsClick(approveAllCollectionsButton, driver);
        return submissionMessage.getText();
    }

    public void selectRequiredReceipt() {
        List<WebElement> totalPages = driver.findElements(By.xpath(".//*[@id='searchReceipt-search']/div[4]/span/a"));

        if (totalPages.size() > 0) {
            WebElement lastPageLink = driver.findElement(By.xpath(".//*[@id='searchReceipt-search']/div[4]/span/a[" + (totalPages.size() - 1) + "]"));
            jsClick(lastPageLink, driver);
        }

        List<WebElement> totalRows = driver.findElements(By.xpath(".//*[@id='selectedReceipts']"));
        WebElement requiredRow = totalRows.get(totalRows.size() - 1);
        jsClick(requiredRow, driver);
        clickOnButton(cancelReceiptButton, driver);
    }

    public void searchRequiredReceipt() {
        selectFromDropDown(classificationBox, "Miscelleneous Collection", driver);
        selectFromDropDown(serviceTypeTextBox, "Monuments Entry Fees-MNMENTFEE", driver);
        enterDate(fromDateTextBox, getCurrentDate(), driver);
        clickOnButton(searchButton, driver);
    }

    public String cancelReceipt() {
        enterText(reasonForCancellationTextBox, "Testing", driver);
        jsClick(cancelReceiptButton, driver);
        WebElement cancelReceiptSuccessMessage = driver.findElement(By.xpath(".//*[@id='searchReceipt']/table/tbody/tr[2]/td/font/b/div"));
        waitForElementToBeVisible(cancelReceiptSuccessMessage, driver);
        String message = cancelReceiptSuccessMessage.getText();

        return message;
    }

    public void enterBankDetails() {
        selectFromDropDown(bankNameBox, "ANDHRA BANK-Andhra Bank RTC Busstand", driver);
        selectFromDropDown(bankAccountNumberBox, "110710011005899", driver);
        selectFromDropDown(paymentModeBox, "cheque/dd", driver);
        enterDate(fromDateTextBox, getCurrentDate(), driver);
        enterDate(toDateTextBox, getCurrentDate(), driver);
        clickOnButton(searchButton, driver);
        jsClick(receiptCheckBox, driver);
        receiptCheckBox.sendKeys(Keys.TAB);

        List<WebElement> allDates = driver.findElements(By.xpath(".//div[@class='datepicker-days']//td"));

        for (WebElement ele : allDates) {
            String date = ele.getAttribute("class");

            if (date.equalsIgnoreCase("active day")) {
                jsClick(ele, driver);
                break;
            }
        }
        jsClick(remitToBankButton, driver);

        driver.switchTo().alert().accept();
    }

    public String successMessageOfRemittance() {
        return successMessageTextOfRemittance.getText();
    }

    public void checkPaymentPage(String applicationNumber) {

        boolean isOk = driver.getCurrentUrl().split("/")[4].equals(applicationNumber);
        if (isOk) {
            System.out.println("Fine");
        } else {
            System.out.println("Not Done");
        }
    }
}
