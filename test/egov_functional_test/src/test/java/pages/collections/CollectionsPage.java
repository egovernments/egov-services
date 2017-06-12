package pages.collections;

import entities.ApprovalDetails;
import entities.collections.ChallanHeaderDetails;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import pages.BasePage;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class CollectionsPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "chequeradiobutton")
    private WebElement chequeModeofPaymentRadio;

    @FindBy(id = "ddradiobutton")
    private WebElement ddModeofPaymentRadio;

    @FindBy(id = "instrumentChequeNumber")
    private WebElement chequeNumberTextBox;

    @FindBy(id = "instrumentDate")
    private WebElement chequeDateTextBox;

    @FindBy(id = "bankName")
    private WebElement bankNameInput;

    @FindBy(id = "billDetailslist[0].creditAmountDetail")
    private WebElement challanAmountTextBox;

    @FindBy(id = "paidBy")
    private WebElement paidByTextBox;

    @FindBy(id = "totalamounttobepaid")
    private WebElement amountToBePaidLabel;

    @FindBy(id = "challanDate")
    private WebElement challanDateTextBox;

    @FindBy(id = "payeeName")
    private WebElement payeeNameTextBox;

    @FindBy(id = "payeeAddress")
    private WebElement payeeAddressTextBox;

    @FindBy(id = "referenceDesc")
    private WebElement narrationTextBox;

    @FindBy(id = "serviceCategoryId")
    private WebElement serviceCategoryBox;

    @FindBy(id = "serviceId")
    private WebElement serviceTypeBox;

    @FindBy(id = "functionId")
    private WebElement functionTab;

    @FindBy(id = "instrumentChequeAmount")
    private WebElement amountTextBox;

    @FindBy(id = "approverDeptId")
    private WebElement approverDeptBox;

    @FindBy(id = "designationId")
    private WebElement approverDesignationBox;

    @FindBy(id = "positionUser")
    private WebElement approverBox;

    @FindBy(id = "CHALLAN_NEW")
    private WebElement createChallanButton;

    @FindBy(id = "totalcramount")
    private WebElement totalAmount;

    @FindBy(id = "CHALLAN_VALIDATE")
    private WebElement validateChallan;

    @FindBy(id = "challanNumber")
    private WebElement challanNumberTextBox;

    @FindBy(id = "totalamounttobepaid")
    private WebElement amountToBePaid;

    @FindBy(id = "instrHeaderCash.instrumentAmount")
    private WebElement amountToBePaidBox;

    @FindBy(id = "button2")
    private WebElement payButton;

    @FindBy(id = "input[value='Approve All Collections'][type='submit']")
    private WebElement approveCollectionButton;

    @FindBy(id = "app-appcodo")
    private WebElement consumerNumberTextBox;

    @FindBy(id = "submitButtonId")
    private WebElement submitButton;

    @FindBy(id = "payBtn")
    private WebElement collectWaterCharge;

    @FindBy(css = "input[id='instrumentChequeAmount'][type='text']")
    private WebElement payAmountBoxForCheque;

    @FindBy(css = "input[id='instrHeaderCash.instrumentAmount'][type='text']")
    private WebElement payAmountBoxForCash;

    @FindBy(id = "challan.challanNumber")
    private WebElement challanNumber;

    @FindBy(id = "actionMessages")
    private WebElement creationMsg;

    @FindBy(xpath = ".//*[@id='buttonclose2']")
    private WebElement closeButton;

    @FindBy(id = "assessmentNum")
    private WebElement assessmentNumberField;

    @FindBy(id = "assessmentform_search")
    private WebElement assessmentFormSearchButton;

    @FindBy(id = "taxEnsureCheckbox")
    private WebElement onlinePageCheckBox;

    @FindBy(css = "input[id='PayTax'][type='button']")
    private WebElement payTaxButton;

    @FindBy(id = "updatePaytax")
    private WebElement updatePayTaxButton;

    @FindBy(css = ".btn.btn-xs.btn-secondary.collect-hoardingWiseFee")
    private WebElement onlinePayButton;

    @FindBy(className = "justbold")
    private List<WebElement> totalOnlineAmount;

    @FindBy(id = "paymentAmount")
    private WebElement totalOnlineAmountToBePaid;

    @FindBy(name = "radioButton1")
    private WebElement axisBankRadio;

    @FindBy(id = "checkbox")
    private WebElement termsAndConditionsCheckBox;

    @FindBy(xpath = "html/body/center/table[6]/tbody/tr[3]/td/table/tbody/tr/td[3]/a/img")
    private WebElement masterCardImage;

    @FindBy(id = "CardNumber")
    private WebElement cardNumber;

    @FindBy(id = "CardMonth")
    private WebElement cardMonth;

    @FindBy(id = "CardYear")
    private WebElement cardYear;

    @FindBy(id = "Securecode")
    private WebElement cvvNumber;

    @FindBy(id = "Paybutton")
    private WebElement onlineCardPaymentButton;

    public CollectionsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterChallanHeader(ChallanHeaderDetails challanHeaderDetails) {
        enterDate(challanDateTextBox, getCurrentDate(), driver);
        enterText(payeeNameTextBox, challanHeaderDetails.getPayeeName(), driver);
        enterText(payeeAddressTextBox, challanHeaderDetails.getPayeeAddress(), driver);
        enterText(narrationTextBox, challanHeaderDetails.getNarration(), driver);
        selectFromDropDown(serviceCategoryBox, challanHeaderDetails.getServiceCategory(), driver);
        await().atMost(10, SECONDS).until(() -> new Select(serviceTypeBox).getOptions().size() > 1);
        selectFromDropDown(serviceTypeBox, challanHeaderDetails.getServiceType(), driver);
        try {
            challanAmountTextBox.clear();
            enterText(challanAmountTextBox, "500", driver);
        } catch (StaleElementReferenceException e) {
            challanAmountTextBox.clear();
            enterText(challanAmountTextBox, "500", driver);
        }
    }

    public void enterApprovalDetails(ApprovalDetails approverDetails) {

        selectFromDropDown(approverDeptBox, approverDetails.getApproverDepartment(), driver);
        await().atMost(10, SECONDS).until(() -> new Select(approverDesignationBox).getOptions().size() > 1);

        selectFromDropDown(approverDesignationBox, approverDetails.getApproverDesignation(), driver);
        await().atMost(10, SECONDS).until(() -> new Select(approverBox).getOptions().size() > 1);

        selectFromDropDown(approverBox, approverDetails.getApprover(), driver);
    }

    public void validateChallan() {
        clickOnButton(validateChallan, driver);
    }

    public void enterChallanNumber(String number) {
        enterText(challanNumberTextBox, number, driver);
        challanNumberTextBox.sendKeys(Keys.TAB);
    }

    public String generateChallan() {
        clickOnButton(createChallanButton, driver);
        WebElement isPresent = driver.findElement(By.xpath(".//*[@id='challan_error_area']"));
        if (isPresent.getText().equals("Please enter credit account details")) {
            enterText(challanAmountTextBox, "500", driver);
            clickOnButton(createChallanButton, driver);
        }
        waitForElementToBeVisible(challanNumber, driver);
        String number = challanNumber.getAttribute("value");
        return number;
    }

    public String successMessage() {
        String msg = getTextFromWeb(creationMsg, driver);
        System.out.println("\n" + msg);
        return msg;
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void propertyTaxOnlinePaymentLink() {
        driver.navigate().to(getEnvironmentURL() + "/ptis/citizen/search/search-searchByAssessmentForm.action");
    }

    public void enterAssessmentNumber(String assessmentNumber) {
        enterText(assessmentNumberField, assessmentNumber, driver);
        clickOnButton(assessmentFormSearchButton, driver);
        onlinePageCheckBox.click();
        payTaxButton.click();
        clickOnButton(updatePayTaxButton, driver);
    }

    public void enterAmountAndPayOnline() {
        waitForElementToBeVisible(totalOnlineAmount.get(1), driver);
        String amount = totalOnlineAmount.get(1).getText();
        waitForElementToBeClickable(totalOnlineAmountToBePaid, driver);
        totalOnlineAmountToBePaid.sendKeys(amount.split("\\.")[0]);
        jsClick(axisBankRadio, driver);
        jsClickCheckbox(termsAndConditionsCheckBox, driver);
        clickOnButton(payButton, driver);
        clickOnButton(masterCardImage, driver);
    }

    public void enterCarddetailsAndPay() {
        enterText(cardNumber, "5123456789012346", driver);
        enterText(cardMonth, "05", driver);
        enterText(cardYear, "17", driver);
        enterText(cvvNumber, "123", driver);
        clickOnButton(onlineCardPaymentButton, driver);
    }

    public String getSuccessMsg() {

        await().atMost(30, SECONDS).until(() -> driver.findElements(By.cssSelector("div[id='paymentInfo']")).size() == 1);
        String successMsg = getTextFromWeb(driver.findElement(By.id("paymentInfo")), driver);
        return successMsg;
    }
}
