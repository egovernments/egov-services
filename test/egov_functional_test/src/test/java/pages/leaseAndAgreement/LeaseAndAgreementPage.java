package pages.leaseAndAgreement;

import entities.leaseAndAgreement.LandAgreementDetails;
import entities.leaseAndAgreement.LandAllotteeDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class LeaseAndAgreementPage extends BasePage {

    @FindBy(id = "asset_category")
    private WebElement assetCategorySearchDropdown;

    @FindBy(css = "button[class='btn btn-submit']")
    private WebElement searchAsseetButton;

    @FindBy(css = "input[type='search']")
    private WebElement searchAssetCodeTextBox;

    @FindBy(id = "myOptions")
    private WebElement actionDropdown;

    // Land Allottee Details
    @FindBy(css = "input[id='allottee.aadhaarNumber']")
    private WebElement aadharNumberTextBox;

    @FindBy(css = "input[id='allottee.mobileNumber']")
    private WebElement mobileNumberTextBox;

    @FindBy(css = "input[id='allottee.name']")
    private WebElement nameTextBox;

    @FindBy(css = "input[id='allottee.emailId']")
    private WebElement emailIdTextBox;

    @FindBy(css = "input[id='allottee.pan']")
    private WebElement panTextBox;

    // Land Agreement Details
    @FindBy(css = "input[id='tenderNumber']")
    private WebElement tenderNumberTextBox;

    @FindBy(css = "input[id='tenderDate']")
    private WebElement tenderDate;

    @FindBy(css = "[id='natureOfAllotment']")
    private WebElement natureOfAllotmentDropdown;

    @FindBy(css = "[id='councilNumber']")
    private WebElement councilNumberTextBox;

    @FindBy(css = "[id='councilDate']")
    private WebElement councilDate;

    @FindBy(css = "[id='rent']")
    private WebElement landRentTextBox;

    @FindBy(css = "[id='paymentCycle']")
    private WebElement paymentCycleDropdown;

    @FindBy(css = "[id='bankGuaranteeAmount']")
    private WebElement bankGuaranteeAmountTextBox;

    @FindBy(css = "[id='bankGuaranteeDate']")
    private WebElement bankGuaranteeDate;

    @FindBy(css = "[id='solvencyCertificateNo']")
    private WebElement solvencyCertificateNumberTextBox;

    @FindBy(css = "[id='solvencyCertificateDate']")
    private WebElement solvencyCertificateDate;

    @FindBy(css = "[id='securityDepositDate']")
    private WebElement securityDepositDate;

    @FindBy(css = "[id='commencementDate']")
    private WebElement commencementDate;

    @FindBy(css = "[id='rentIncrementMethod']")
    private WebElement rentIncrementMethodDropdown;

    @FindBy(css = "[id='timePeriod']")
    private WebElement timePeriodDropdown;

    @FindAll({@FindBy(id = "createAgreement"), @FindBy(id = "Forward")})
    private WebElement forwardButton;

    @FindBy(id = "registration_free")
    private WebElement registrationFeeTextBox;

    @FindBy(id = "goodWillAmount")
    private WebElement goodWillAmountTextBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement closeButton;

    @FindBy(css = "[name='instrHeaderCash.instrumentAmount']")
    private WebElement amountTextBox;

    @FindBy(id = "button2")
    private WebElement payButton;

    private WebDriver webDriver;

    public LeaseAndAgreementPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void searchAssetApplication(String applicationNumber, String categoryType, String action) {
        selectFromDropDown(assetCategorySearchDropdown, categoryType, webDriver);
        clickOnButton(searchAsseetButton, webDriver);
        enterText(searchAssetCodeTextBox, applicationNumber, webDriver);
        selectFromDropDown(actionDropdown, action, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void enterAgreementDetails(LandAllotteeDetails landAllotteeDetails, LandAgreementDetails landAgreementDetails) {
        enterLandAllotteeDetails(landAllotteeDetails);
        enterLandAgreementDetails(landAgreementDetails);
    }

    private void enterLandAllotteeDetails(LandAllotteeDetails landAllotteeDetails) {
        enterText(aadharNumberTextBox, get6DigitRandomInt() + get6DigitRandomInt(), webDriver);
        enterText(mobileNumberTextBox, "9" + get6DigitRandomInt() + get6DigitRandomInt().substring(0, 3), webDriver);
        enterText(nameTextBox, landAllotteeDetails.getName(), webDriver);
        enterText(emailIdTextBox, landAllotteeDetails.getEmail(), webDriver);
        enterText(panTextBox, "ABCDE" + get6DigitRandomInt().substring(0, 4) + "F", webDriver);

    }

    private void enterLandAgreementDetails(LandAgreementDetails landAgreementDetails) {
        enterText(tenderNumberTextBox, "T" + get6DigitRandomInt().substring(0, 3), webDriver);
        enterDate(tenderDate, getCurrentDate(), webDriver);
        selectFromDropDown(natureOfAllotmentDropdown, landAgreementDetails.getNatureOfAllotment(), webDriver);
        enterText(councilNumberTextBox, "C" + get6DigitRandomInt().substring(0, 3), webDriver);
        enterTextWithoutClearing(registrationFeeTextBox, "1000", webDriver);
        enterDate(councilDate, getCurrentDate(), webDriver);
        enterText(landRentTextBox, landAgreementDetails.getLandRent(), webDriver);
        selectFromDropDown(paymentCycleDropdown, landAgreementDetails.getPaymentCycle(), webDriver);
        enterText(bankGuaranteeAmountTextBox, landAgreementDetails.getBankGuaranteeAmount(), webDriver);
        enterDate(bankGuaranteeDate, getCurrentDate(), webDriver);
        enterText(solvencyCertificateNumberTextBox, "S" + get6DigitRandomInt().substring(0, 3), webDriver);
        enterDate(solvencyCertificateDate, getCurrentDate(), webDriver);
        enterDate(securityDepositDate, getCurrentDate(), webDriver);
        enterDate(commencementDate, getCurrentDate(), webDriver);
//        selectFromDropDown(rentIncrementMethodDropdown, landAgreementDetails.getRentIncrementMethod(), webDriver);
        enterTextWithoutClearing(goodWillAmountTextBox, "100", webDriver);
        selectFromDropDown(timePeriodDropdown, landAgreementDetails.getTimePeriod(), webDriver);
    }

    public void clickOnForwardAndCloseSuccessPage() {
        clickOnButton(forwardButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
        // Close Acknowledge Page
        clickOnButton(closeButton, webDriver);
        if (webDriver.getWindowHandles().size() > 1) {
            ArrayList<String> tabs2 = new ArrayList<>(webDriver.getWindowHandles());
            webDriver.switchTo().window(tabs2.get(1));
            clickOnButton(webDriver.findElement(By.cssSelector(".btn.btn-close")), webDriver);
        }
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public String collectFeeFromApplicant() {
        await().atMost(10, TimeUnit.SECONDS).until(() -> webDriver.findElements(By.cssSelector("[class='greyboxwithlink'] table tbody tr")).size() > 0);
        waitForElementToBeVisible(webDriver.findElements(By.cssSelector("[class='greyboxwithlink'] table tbody tr")).get(1).findElements(By.tagName("td")).get(2), webDriver);
        String applicationNumber = webDriver.findElements(By.cssSelector("[class='greyboxwithlink'] table tbody tr")).get(1).findElements(By.tagName("td")).get(2).getText();
        waitForElementToBeVisible(webDriver.findElement(By.id("totalamounttobepaid")), webDriver);
        String amountTobePaid = webDriver.findElement(By.id("totalamounttobepaid")).getAttribute("value");

        enterText(amountTextBox, amountTobePaid.split("\\.")[0], webDriver);
        clickOnButton(payButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
        return applicationNumber;
    }

    public void closeAcknowledgementForm() {
        webDriver.close();
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void acceptApplication() {
        jsClick(webDriver.findElement(By.cssSelector("[class='btn btn-submit'][id='Approve']")), webDriver);
        switchToNewlyOpenedWindow(webDriver);
        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
