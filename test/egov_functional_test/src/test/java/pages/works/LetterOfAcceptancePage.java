package pages.works;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class LetterOfAcceptancePage extends BasePage {
    String RandomString = RandomStringUtils.randomAlphanumeric(3).toUpperCase();
    private WebDriver driver;
    @FindBy(id = "fileNumber")
    private WebElement fileNumber;

    @FindBy(id = "fileDate")
    private WebElement fileDate;

    @FindBy(className = "caret")
    private WebElement tenderPer;

    @FindBy(id = "tenderFinalizedPercentage")
    private WebElement tenderFinalizedPercentage;

    @FindBy(id = "contractorSearch")
    private WebElement firmName;

    @FindBy(css = "div[class='alert text-center'][style='color:green;']")
    private WebElement loaNumber;

    @FindBy(id = "contractPeriod")
    private WebElement contractPeriod;

    @FindBy(id = "defectLiabilityPeriod")
    private WebElement defectLiabilityPeriod;

    @FindBy(id = "engineerIncharge")
    private WebElement engineerIncharge;

    @FindBy(id = "save")
    private WebElement saveButton;

    @FindBy(linkText = "View LOA PDF")
    private WebElement viewLOAPDF;

    @FindBy(id = "closeButton")
    private WebElement closeButton;

    @FindBy(linkText = "Close")
    private WebElement closeLink;

    @FindBy(css = "button[id='btnsearch'][type='button']")
    private WebElement searchLOAButton;

    @FindBy(xpath = ".//*[@id='workOrderNumber']")
    private WebElement workOrderNumber;

    @FindBy(xpath = "(//*[@id='resultTable']/tbody/tr/td/input)[1]")
    private WebElement selectLOA;

    @FindBy(id = "btnmodifyloa")
    private WebElement modifyLOAbutton;

    @FindBy(id = "revisedValue")
    private WebElement revisedValue;

    @FindBy(id = "modify")
    private WebElement modifyButton;

    @FindBy(id = "actionDropdown")
    private WebElement actionDropdown;

    @FindBy(xpath = "//*[@id='workOrder']/div[2]/div/a[1]")
    private WebElement closeViewLOA;

    @FindBy(xpath = ".//*[@id='btnsearch']")
    private WebElement searchButton;

    @FindBy(xpath = ".//*[@id='resultTable']/tbody/tr[1]/td[1]/input")
    private WebElement reqFileLink;

    @FindBy(id = "btncreateloa")
    private WebElement createLOAButton;

    @FindBy(id = "spillOverFlag")
    private WebElement spillOverCheck;

    @FindBy(id = "workOrderNumber")
    private WebElement LOANumber;

    @FindBy(id = "workOrderDate")
    private WebElement agreementDate;

    public LetterOfAcceptancePage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterLOAdetails() {
        enterText(fileNumber, "F/" + RandomString, driver);
        enterDate(fileDate, getCurrentDate(), driver);
        enterText(tenderFinalizedPercentage, "12", driver);
        enterText(firmName, "KMC055", driver);
        WebElement dropdown = driver.findElement(By.className("tt-dropdown-menu"));
        clickOnButton(dropdown, driver);
        enterText(contractPeriod, "500", driver);
        enterText(defectLiabilityPeriod, "0.6", driver);
        selectFromDropDown(engineerIncharge, "A.P.Sreenivasulu - Assistant Engineer", driver);
    }

    public String saveAndClose() {

        waitForElementToBeVisible(saveButton, driver);
        jsClick(saveButton, driver);
        waitForElementToBeVisible(loaNumber, driver);
        String loaText = getTextFromWeb(loaNumber, driver);
        String workNumber = (loaText.split("\\ ")[5]);
        return workNumber;
    }

    public void searchForLOA(String number) {

        enterText(workOrderNumber, number, driver);
        clickOnButton(searchLOAButton, driver);
        selectFromDropDown(actionDropdown, "View LOA", driver);
        switchToNewlyOpenedWindow(driver);
        clickOnButton(closeViewLOA, driver);

        for (String winHandle : driver.getWindowHandles()) {
            String title = driver.switchTo().window(winHandle).getCurrentUrl();
            if (title.equals("http://kurnool-uat.egovernments.org/egworks/searchletterofacceptance/searchform")) {
                break;
            }
        }
        clickOnButton(closeLink, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void searchForLOAModify(String number) {
        enterText(workOrderNumber, number, driver);
        clickOnButton(searchLOAButton, driver);
        clickOnButton(selectLOA, driver);
        clickOnButton(modifyLOAbutton, driver);
        enterText(revisedValue, "22", driver);
        clickOnButton(modifyButton, driver);
    }

    public void searchForApplication() {
        clickOnButton(searchButton, driver);
        jsClick(reqFileLink, driver);
        clickOnButton(createLOAButton, driver);
    }

    public String successMessage() {
        String msg = getTextFromWeb(loaNumber, driver);
        return msg;
    }

    public void searchForSpilloverEstimate() {
        clickOnButton(spillOverCheck, driver);
        clickOnButton(searchButton, driver);
        jsClick(reqFileLink, driver);
        clickOnButton(createLOAButton, driver);
    }

    public void entersSpilloverLOADetails() {
        enterText(LOANumber, "LOA/" + get6DigitRandomInt(), driver);
        enterText(fileNumber, "FN" + RandomString, driver);
        enterDate(fileDate, getCurrentDate(), driver);
        enterText(tenderFinalizedPercentage, "12", driver);
        enterDate(agreementDate, getCurrentDate(), driver);
        enterText(firmName, "KMC055", driver);
        WebElement dropdown = driver.findElement(By.className("tt-dropdown-menu"));
        clickOnButton(dropdown, driver);
        enterText(contractPeriod, "300", driver);
        enterText(defectLiabilityPeriod, "0.6", driver);
        selectFromDropDown(engineerIncharge, "A.P.Sreenivasulu - Assistant Engineer", driver);
    }
}