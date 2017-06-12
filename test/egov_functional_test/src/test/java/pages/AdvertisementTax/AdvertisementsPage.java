package pages.AdvertisementTax;

import entities.advertisementTax.AdvertisementDetails;
import entities.advertisementTax.LocalityDetails;
import entities.advertisementTax.PermissionDetails;
import entities.advertisementTax.StructureDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AdvertisementsPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "category")
    private WebElement categoryBox;

    @FindBy(id = "subCategory")
    private WebElement subCategoryBox;

    @FindBy(id = "rateClass")
    private WebElement classBox;

    @FindBy(id = "revenueInspector")
    private WebElement revenueInspectorBox;

    @FindBy(css = "input[id='advertisement.type2'][type='radio']")
    private WebElement structureTypeRadioButton;

    @FindBy(id = "propertyType")
    private WebElement propertyTypeBox;

    @FindBy(css = "input[id='applicationDate'][type='text']")
    private WebElement applicationDateBox;

    @FindBy(css = "input[id='agencyTypeAhead'][type='text']")
    private WebElement agencyTextBox;

    @FindBy(css = "input[id='permissionstartdate'][type='text']")
    private WebElement permissionStartDateBox;

    @FindBy(css = "input[id='permissionenddate'][type='text']")
    private WebElement permissionEndDateBox;

    @FindBy(id = "advertisementParticular")
    private WebElement adParticularTextBox;

    @FindBy(id = "advertisementDuration")
    private WebElement advertismentDurationBox;

    @FindBy(id = "locality")
    private WebElement localityBox;

    @FindBy(id = "address")
    private WebElement addressTextBox;

    @FindBy(css = "input[id='measurement'][type='text']")
    private WebElement measurementTextBox;

    @FindBy(id = "unitOfMeasure")
    private WebElement measurementTypeBox;

    @FindBy(css = "input[id='taxAmount'][type='text']")
    private WebElement taxAmountTextBox;

    @FindBy(id = "ownerDetail")
    private WebElement ownerDetailsTextBox;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(css = "div[class='panel-title text-center']")
    private WebElement creationMsg;

    @FindBy(xpath = ".//*[@id='statusinactivesuccess']/div/div/div")
    private WebElement creationMsgForDeactivation;

    @FindBy(linkText = "Close")
    private WebElement closeLink;

    @FindBy(id = "approvalComent")
    private WebElement commentBox;

    @FindBy(id = "Approve")
    private WebElement approveButton;

    @FindBy(css = "input[id='hoardingnumber'][type='text']")
    private WebElement hoardingNumberTextBox;

    @FindBy(id = "search")
    private WebElement submitButton;

    @FindBy(css = "input[value= 'Advertisement'][type= 'radio']")
    private WebElement searchType;

    @FindBy(id = "hoardingnumber")
    private WebElement advertisementNumber;

    @FindBy(id = "search")
    private WebElement searchAdvertisementButton;

    @FindBy(css = "button[class*='collect']")
    private WebElement collectButton;

    @FindBy(css = "input[id='totalamounttobepaid'][type='text']")
    private WebElement totalamounttobepaid;

    @FindBy(id = "instrHeaderCash.instrumentAmount")
    private WebElement amount;

    @FindBy(css = "input[type='submit'][value ='Pay']")
    private WebElement payButton;

    @FindBy(id = "agencyWiseCollectionListSelected[0]")
    private WebElement selectAdvertisement;

    @FindBy(css = "input[type ='button'][value='Close']")
    private WebElement closeButton;

    @FindBy(css = "input[type='text'][id='agencycode']")
    private WebElement agencyCodeTextBox;

    @FindBy(css = "input[type='text'][id='agencyname']")
    private WebElement agencyNameTextBox;

    @FindBy(css = "input[type='text'][id='depositAmount']")
    private WebElement depositAmountTextBox;

    @FindBy(css = "input[type='text'][id='mobilenumber']")
    private WebElement mobileNumberTextBox;

    @FindBy(id = "status_dropdown")
    private WebElement statusDropDownBox;

    @FindBy(css = "button[type='submit']")
    private WebElement submitAgencyDetailsButton;

    @FindBy(xpath = ".//*[@id='agencysuccess']/div[1]")
    private WebElement agencyCreationMessage;

    @FindBy(xpath = ".//*[@id='agencysuccess']/div[3]/div/button[2]")
    private WebElement closeAgencyCreation;

    @FindBy(xpath = ".//*[@id='agencysuccess']/div[2]/div/button")
    private WebElement closeAgencySearch;

    @FindBy(id = "agencies")
    private WebElement searchAgencyBox;

    @FindBy(id = "buttonView")
    private WebElement viewButton;

    @FindBy(css = "input[id='selectAll'][type='checkbox']")
    private WebElement selectAllCheckBox;

    @FindBy(id = "agencysearch")
    private WebElement collectFeeButton;

    @FindBy(id = "searchrecord")
    private WebElement submitButtonForDeactive;

    @FindBy(xpath = "//*[@id='adtax_searchrecord']/tbody/tr[1]/td[10]/button")
    private WebElement deactivateButton;

    @FindBy(id = "deactivation_remarks")
    private WebElement deactivationRemarks;

    @FindBy(id = "deactiveDate")
    private WebElement deactiveDate;

    @FindBy(id = "deactivation")
    private WebElement deactivateSubmitButton;

    @FindBy(xpath = ".//*[@id='statusinactivesuccess']/div/div[2]/div/button")
    private WebElement closeButtonOfDeactivateSuccessPage;

    @FindBy(css = "input[id='cashradiobutton'][type='radio']")
    private WebElement cashRadioButton;

    @FindBy(css = "input[id = 'chequeradiobutton'][type='radio']")
    private WebElement chequeRadioButton;

    @FindBy(id = "electionWard")
    private WebElement electionWardBox;

    public AdvertisementsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterAdvertisementDetails1(AdvertisementDetails advertisementDetails) {
        selectFromDropDown(categoryBox, advertisementDetails.getCategory(), driver);
        selectFromDropDown(subCategoryBox, advertisementDetails.getSubCategory(), driver);
        selectFromDropDown(classBox, advertisementDetails.getClassType(), driver);
        selectFromDropDown(revenueInspectorBox, advertisementDetails.getRevenueInspector(), driver);
        jsClick(structureTypeRadioButton, driver);
        selectFromDropDown(propertyTypeBox, advertisementDetails.getPropertyType(), driver);
    }

    public void enterPermissionDetails1(PermissionDetails permissionDetails) {
        enterDate(applicationDateBox, getCurrentDate(), driver);
        enterText(adParticularTextBox, permissionDetails.getAdParticular(), driver);
        enterText(ownerDetailsTextBox, permissionDetails.getOwner(), driver);
        enterDate(permissionStartDateBox, getFutureDate(7), driver);
        enterDate(permissionEndDateBox, getFutureDate(37), driver);
        selectFromDropDown(advertismentDurationBox, permissionDetails.getAdvertisementDuration(), driver);
    }

    public void enterAgencyDetailsForCreationAdvertisement(String agencyName) {
        enterText(agencyTextBox, agencyName, driver);
        WebElement dropdown = driver.findElement(By.className("tt-dropdown-menu"));
        clickOnButton(dropdown, driver);
    }

    public void enterLocalityDetails1(LocalityDetails localityDetails) {
        selectFromDropDown(localityBox, localityDetails.getLocality(), driver);
        enterText(addressTextBox, localityDetails.getLocalityAddress(), driver);
        selectFromDropDown(electionWardBox, localityDetails.getElectionWard(), driver);
    }

    public void enterStructureDetails1(StructureDetails structureDetails) {
        enterText(measurementTextBox, structureDetails.getMeasurement(), driver);
        selectFromDropDown(measurementTypeBox, structureDetails.getMeasurementType(), driver);
        await().atMost(10, SECONDS).until(() -> driver.findElements(By.id("taxAmount")).size() == 1);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("document.getElementById('taxAmount').value = '100';");
    }

    public String forward() {
        clickOnButton(forwardButton, driver);

        if (driver.findElements(By.id("Forward")).size() == 1) {
            await().atMost(10, SECONDS).until(() -> driver.findElements(By.id("taxAmount")).size() == 1);
            JavascriptExecutor jse1 = (JavascriptExecutor) driver;
            jse1.executeScript("document.getElementById('taxAmount').value = '100';");
            clickOnButton(forwardButton, driver);
        }
        String Msg = getTextFromWeb(creationMsg, driver);
        String applicationNumber = Msg.substring(Msg.lastIndexOf(" ") + 1);
        return applicationNumber;
    }

    public String successMessage() {
        return getTextFromWeb(creationMsg, driver);
    }

    public void close() {
        clickOnButton(closeLink, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void approverComment() {
        enterText(commentBox, "Approved", driver);
    }

    public void approve() {
        clickOnButton(approveButton, driver);
        if (driver.findElements(By.id("Approve")).size() == 1) {
            clickOnButton(driver.findElement(By.id("Approve")), driver);
        }
    }

    public String getAdvertisementNumber() {
        String number = getTextFromWeb(creationMsg, driver);
        String applicationNumber = (number.split("\\ "))[8];
        return applicationNumber;
    }

    public void searchAndSelect(String number) {
        enterText(hoardingNumberTextBox, number, driver);
        clickOnButton(submitButton, driver);

        WebElement dropDownBox = driver.findElement(By.id("adtaxdropdown"));
        selectFromDropDown(dropDownBox, "View", driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void closeMultipleWindows(String url) {
        clickOnButton(closeLink, driver);

        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals(getEnvironmentURL() + url)) {
                break;
            }
        }

        close();
    }

    public void closeMultiple(String url) {
        clickOnButton(closeButton, driver);

        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals(getEnvironmentURL() + url)) {
                break;
            }
        }

        close();
    }

    public void searchByAdvertisementNumber(String advertisementNum) {
        clickOnButton(searchType, driver);
        enterText(advertisementNumber, advertisementNum, driver);
        clickOnButton(searchAdvertisementButton, driver);
    }

    public void collectAdvertisementTax() {
        clickOnButton(collectButton, driver);
        switchToNewlyOpenedWindow(driver);

        jsClick(chequeRadioButton, driver);

        jsClick(cashRadioButton, driver);
        waitForElementToBeVisible(totalamounttobepaid, driver);
        String AmountNum = totalamounttobepaid.getAttribute("value");
        String Amount = AmountNum.split("\\.")[0];
        enterText(amount, Amount, driver);
        jsClick(payButton, driver);
    }

    public String enterAgencyDetails() {
        enterText(agencyCodeTextBox, "Ac" + get6DigitRandomInt(), driver);

        String name = "Test" + get6DigitRandomInt();
        enterText(agencyNameTextBox, name, driver);
        enterText(depositAmountTextBox, "1000", driver);
        enterText(mobileNumberTextBox, "9885" + get6DigitRandomInt(), driver);
        WebElement element = statusDropDownBox.findElement(By.cssSelector("option[value='ACTIVE']"));
        clickOnButton(element, driver);

        return name;
    }

    public void searchByAgency(String name) {
        enterText(agencyTextBox, name, driver);
        WebElement dropdown = driver.findElement(By.className("tt-dropdown-menu"));
        clickOnButton(dropdown, driver);
        clickOnButton(searchAdvertisementButton, driver);
    }

    public void collectAdvertisementTaxByAgency() {
        waitForElementToBeVisible(totalamounttobepaid, driver);
        String AmountNum = totalamounttobepaid.getAttribute("value");
        String Amount = AmountNum.split("\\.")[0];
        enterText(amount, Amount, driver);
        jsClick(payButton, driver);
    }

    public void submit() {
        clickOnButton(submitAgencyDetailsButton, driver);
    }

    public String agencyCreationMessage() {
        return getTextFromWeb(agencyCreationMessage, driver);
    }

    public void CloseAgency() {
        clickOnButton(closeAgencyCreation, driver);

        switchToPreviouslyOpenedWindow(driver);
    }

    public void searchAgency(String name) {
        selectFromDropDown(searchAgencyBox, name, driver);
//        clickOnButton(viewButton, driver);
        clickOnButton(viewButton, driver);
    }

    public void CloseAgencySearch() {
        clickOnButton(closeAgencySearch, driver);

        switchToPreviouslyOpenedWindow(driver);
    }

    public void searchAdvertisementForDeactivate(String number) {
        enterText(hoardingNumberTextBox, number, driver);
        clickOnButton(submitButtonForDeactive, driver);
        clickOnButton(deactivateButton, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void selectAdvertisementAgency() {
        clickOnButton(collectButton, driver);
        switchToNewlyOpenedWindow(driver);
        clickOnButton(selectAllCheckBox, driver);
        clickOnButton(collectFeeButton, driver);
    }

    public void deactivatesAdvertisement() {
        enterText(deactivationRemarks, "Deactivate the Advertisement", driver);
        enterText(deactiveDate, getCurrentDate(), driver);
        clickOnButton(deactivateSubmitButton, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public String successMessageForDeactivation() {
        return getTextFromWeb(creationMsgForDeactivation, driver);
    }

    public void closeMultipleWindowsForDeactivateadvertisement(String url) {
        clickOnButton(closeButtonOfDeactivateSuccessPage, driver);

        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals(getEnvironmentURL() + url)) {
                break;
            }
        }
        close();
    }
}
