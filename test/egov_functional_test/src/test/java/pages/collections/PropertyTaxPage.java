package pages.collections;

import entities.collections.PaymentMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PropertyTaxPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "assessmentNum")
    private WebElement assessmentNumberTextBox;

    @FindBy(id = "CollectTax")
    private WebElement collectTexButton;

    @FindBy(id = "payTax")
    private WebElement payTaxButton;

    @FindBy(css = "input[id='totalamounttobepaid'][type='text']")
    private WebElement totalAmountToBePaidText;

    @FindBy(css = "input[id='totalamounttobepaid'][type='label']")
    private WebElement totalAmountToBePaidTextForChallan;

    @FindBy(css = "input[id='instrHeaderCash.instrumentAmount'][type='text']")
    private WebElement amountPaidByCashTextBox;

    @FindBy(css = "input[value='Pay'][type='submit']")
    private WebElement payButtonForBill;

    @FindBy(css = "input[value='Pay'][type='button']")
    private WebElement payButtonForChallan;

    @FindBy(css = "input[type='radio'][id='chequeradiobutton']")
    private WebElement chequeModeRadioButton;

    @FindBy(css = "input[type='text'][id='instrumentDate']")
    private WebElement chequeDateTextBox;

    @FindBy(css = "input[type='text'][name='instrumentProxyList[0].bankId.name']")
    private WebElement bankNameTextBox;

    @FindBy(css = "input[type='text'][name='instrumentProxyList[0].instrumentNumber']")
    private WebElement chequeNumberTextBox;

    @FindBy(id = "instrumentChequeAmount")
    private WebElement amountPaidByChequeTextBox;

    @FindBy(css = "input[id='ddradiobutton'][type='radio']")
    private WebElement ddRadioButton;

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

    public PropertyTaxPage(WebDriver driver) {
        this.driver = driver;
    }

    public void collectTaxFor(String assessmentNumber) {
        enterText(assessmentNumberTextBox, assessmentNumber, driver);
        clickOnButton(collectTexButton, driver);
    }

    public void payTax() {
        clickOnButton(payTaxButton, driver);
    }

    public void collectTax(PaymentMethod paymentmethod, String paymentMode, String method) {

        String amount, actualAmount;

        if (method.equals("Bill")) {
            amount = totalAmountToBePaidText.getAttribute("value");
            actualAmount = amount.split("\\.")[0];
        } else {
            amount = totalAmountToBePaidTextForChallan.getAttribute("value");
            actualAmount = amount.split("\\.")[0];
        }

        switch (paymentMode) {

            case "cash":

                enterText(amountPaidByCashTextBox, actualAmount, driver);

                break;

            case "cheque":

                jsClick(chequeModeRadioButton, driver);
                enterText(chequeNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterText(chequeDateTextBox, getCurrentDate(), driver);
                enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
                await().atMost(10, SECONDS).until(() -> driver.findElement(By.id("bankcodescontainer")).findElements(By.cssSelector("ul li")).size() > 1);
//                while (driver.findElement(By.id("bankcodescontainer")).findElements(By.cssSelector("ul li")).size() == 0) {
//                    enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
//                }
                await().atMost(30, SECONDS).until(() -> driver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());

                enterText(amountPaidByChequeTextBox, actualAmount, driver);

                break;

            case "dd":
                jsClick(ddRadioButton, driver);
                enterText(chequeNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterText(chequeDateTextBox, getCurrentDate(), driver);
                enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
                boolean isPresentForDD = driver.findElement(By.id("bankcodescontainer")).findElements(By.cssSelector("ul li")).size() > 0;
                while (!isPresentForDD) {
                    bankNameTextBox.clear();
                    enterText(bankNameTextBox, paymentmethod.getBankName(), driver);
                }
                await().atMost(30, SECONDS).until(() -> driver.findElement(By.id("bankcodescontainer"))
                        .findElements(By.cssSelector("ul li"))
                        .get(0).click());
                enterText(amountPaidByChequeTextBox, actualAmount, driver);

                break;

            case "directBank1":

                jsClick(directBankRadioButton, driver);
                enterText(referenceNumberTextBox, paymentmethod.getChequeNumber(), driver);
                enterDate(challanDateTextBox, getCurrentDate(), driver);
                selectFromDropDown(bankNameDropBox, paymentmethod.getBankName(), driver);
                selectFromDropDown(accountNumberDropBox, paymentmethod.getAccountNumber(), driver);
                enterText(directBankAmountTextBox, actualAmount, driver);

                break;
        }

        if (method.equals("Bill")) {
            jsClick(payButtonForBill, driver);
        } else {
            jsClick(payButtonForChallan, driver);
        }
    }
}
