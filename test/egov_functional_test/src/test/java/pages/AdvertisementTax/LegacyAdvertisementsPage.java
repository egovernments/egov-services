package pages.AdvertisementTax;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class LegacyAdvertisementsPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "pendingTax")
    private WebElement pendingTaxTextBox;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(xpath = ".//*[@id='advertisementSuccessform']/div/div/div")
    private WebElement creationMsg;

    @FindBy(css = "input[id='permissionNumber'][type='text']")
    private WebElement permissionNumberTextBox;

    @FindBy(id = "search-update")
    private WebElement searchButton;

    @FindBy(xpath = ".//*[@id='search-update-result-table']/tbody/tr/td[7]/span[1]/i")
    private WebElement updateButton;

    @FindBy(css = "input[id='advertisement.taxPaidForCurrentYear1'][type='radio']")
    private WebElement taxForYearYesRadioButton;

    @FindBy(css = "input[id='advertisementNumber'][type='text']")
    private WebElement advertisementNumberBox;

    @FindBy(css = "input[id='hoardingnumber'][type='text']")
    private WebElement hoardingNumberTextBox;

    @FindBy(id = "renewalsearch")
    private WebElement renewalSearchButton;

    @FindBy(id = "renewdropdown")
    private WebElement renewalDropDownBox;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(css = "input[id='taxAmount'][type='text']")
    private WebElement taxAmountTextBox;

    public LegacyAdvertisementsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterArrearsTaxDetails() {
        enterText(pendingTaxTextBox, "1000", driver);
    }

    public String submit() {
        clickOnButton(submitButton, driver);

        boolean isPresent = driver.findElements(By.id("taxAmount-error")).size() > 0;

        if (isPresent) {
            enterText(taxAmountTextBox, "10", driver);
            clickOnButton(submitButton, driver);
        }
        String number = getTextFromWeb(creationMsg, driver);
        String num = number.split("\\ ")[6];
        String num1 = num.substring(0, num.length() - 1);
        return num1;
    }

    public String successMessage() {
        return getTextFromWeb(creationMsg, driver);
    }

    public void searchFile(String applicationNumber) {
        enterText(advertisementNumberBox, applicationNumber, driver);
        clickOnButton(searchButton, driver);
    }

    public void updateLegacyAd() {
        clickOnButton(updateButton, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void update() {
        clickOnButton(taxForYearYesRadioButton, driver);
        enterText(pendingTaxTextBox, "0", driver);
    }

    public void searchFileForRenewal(String applicationNumber) {
        enterText(hoardingNumberTextBox, applicationNumber, driver);
        clickOnButton(renewalSearchButton, driver);
    }

    public void requestForRenewal() {
        selectFromDropDown(renewalDropDownBox, "Adtax Renewal", driver);
        switchToNewlyOpenedWindow(driver);
    }
}
