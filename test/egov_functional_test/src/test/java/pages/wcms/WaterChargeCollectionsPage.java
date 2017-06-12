package pages.wcms;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class WaterChargeCollectionsPage extends WaterChargeManagementPage {

    @FindBy(id = "totalamounttobepaid")
    private WebElement totalAmount;

    @FindBy(id = "cashradiobutton")
    private WebElement cashRadio;

    @FindBy(id = "instrHeaderCash.instrumentAmount")
    private WebElement amountToBePaidTextBox;

    @FindBy(id = "chequeradiobutton")
    private WebElement chequeModeOfPaymentRadio;

    @FindBy(id = "instrumentChequeNumber")
    private WebElement chequeNumberTextBox;

    @FindBy(id = "instrumentDate")
    private WebElement chequeDateTextBox;

    @FindBy(id = "bankName")
    private WebElement bankNameInput;

    @FindBy(css = "input[id='instrumentChequeAmount'][type='text']")
    private WebElement payAmountBoxForCheque;

    @FindBy(id = "ddradiobutton")
    private WebElement ddModeOfPaymentRadio;

    @FindBy(css = "input[type='submit'][id='button2']")
    private WebElement button2;

    @FindBy(name = "consumerCode")
    private WebElement onlineConsumerCode;

    @FindBy(id = "searchapprvedapplication")
    private WebElement onlineSearchApplication;

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

    @FindBy(css = "div[id='paymentInfo']")
    private WebElement onlinePaymentSuccessMessage;

    @FindBy(id = "btnGenerateReceipt")
    private WebElement onlineGenerateReceipt;

    @FindBy(id = "buttonClose")
    private WebElement closeReceiptButton;

    private String message;

    public WaterChargeCollectionsPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void paymentWithMode(String mode) {

        waitForElementToBeVisible(totalAmount, webDriver);
        String amount = totalAmount.getAttribute("value");
        String actualAmount = amount.split("\\.")[0];

        switch (mode) {

            case "cash":

                jsClick(cashRadio, webDriver);
                waitForElementToBeClickable(amountToBePaidTextBox, webDriver);
                amountToBePaidTextBox.sendKeys(actualAmount);

                break;

            case "cheque":

                jsClick(chequeModeOfPaymentRadio, webDriver);

                enterText(chequeNumberTextBox, "123456", webDriver);
                enterDate(chequeDateTextBox, "07/04/2017", webDriver);
                enterText(bankNameInput, "102", webDriver);

                webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                await().atMost(10, SECONDS).until(() -> webDriver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());

                bankNameInput.sendKeys(Keys.TAB);

                enterText(payAmountBoxForCheque, actualAmount, webDriver);
                break;

            case "dd":

                jsClick(ddModeOfPaymentRadio, webDriver);

                enterText(chequeNumberTextBox, "123456", webDriver);
                enterDate(chequeDateTextBox, "02/04/2017", webDriver);
                enterText(bankNameInput, "102", webDriver);

                webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
                await().atMost(10, SECONDS).until(() -> webDriver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());

                enterText(payAmountBoxForCheque, actualAmount, webDriver);
                break;

        }
        jsClick(button2, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void onlinePaymentLink() {
        webDriver.navigate().to("http://kurnool-uat.egovernments.org/wtms/search/waterSearch/");
    }

    public void enterOnlineConsumerNumber(String consumerNumber) {

        enterText(onlineConsumerCode, consumerNumber, webDriver);
        clickOnButton(onlineSearchApplication, webDriver);
    }

    public void clickOnOnlinePayButton() {
        clickOnButton(onlinePayButton, webDriver);
    }

    public void selectBankDetails() {

        String amount = getTextFromWeb(totalOnlineAmount.get(1), webDriver);

        waitForElementToBeClickable(totalOnlineAmountToBePaid, webDriver);
        totalOnlineAmountToBePaid.sendKeys(amount.split("\\.")[0]);
        jsClick(axisBankRadio, webDriver);

        clickOnButton(termsAndConditionsCheckBox, webDriver);
        clickOnButton(button2, webDriver);

        clickOnButton(masterCardImage, webDriver);
    }

    public void enterCardDetails() {

        waitForElementToBeClickable(cardNumber, webDriver);
        cardNumber.sendKeys("5123456789012346");

        waitForElementToBeClickable(cardMonth, webDriver);
        cardMonth.sendKeys("05");

        waitForElementToBeClickable(cardYear, webDriver);
        cardYear.sendKeys("17");

        waitForElementToBeClickable(cvvNumber, webDriver);
        cvvNumber.sendKeys("123");

        clickOnButton(onlineCardPaymentButton, webDriver);
    }

    public String onlinePaymentSuccess() {
        await().atMost(30, SECONDS).until(() -> webDriver.findElements(By.cssSelector("div[id='paymentInfo']")).size() == 1);
        message = getTextFromWeb(onlinePaymentSuccessMessage, webDriver);
        return message;
    }

    public void onlineGenerateReceipt() {

        clickOnButton(onlineGenerateReceipt, webDriver);
        waitForElementToBeClickable(closeReceiptButton, webDriver);
        if (closeReceiptButton.isDisplayed()) {
            webDriver.close();
        }
    }
}
