package pages.marriageRegistration;

import entities.marriageRegistration.MarriageRegistrationInformation;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MarriageRegistrationPage extends BasePage {
    public WebDriver driver;

    @FindBy(id = "select-registrationunit")
    private WebElement registrationUnit;

    @FindBy(id = "txt-street")
    private List<WebElement> street;

    @FindBy(id = "select-locality")
    private WebElement locality;

    @FindBy(id = "txt-locality")
    private List<WebElement> localityTxt;

    @FindBy(id = "txt-city")
    private WebElement city;

    @FindBy(id = "txt-dateOfMarriage")
    private WebElement dateOfMarriage;

    @FindBy(id = "select-venue")
    private WebElement venueOfMarriage;

    @FindBy(id = "txt-placeofmrg")
    private WebElement placeOfMarriage;

    @FindBy(css = "input[type='file'][id='marriage-photo']")
    private WebElement marriagePhoto;

    @FindBy(id = "husband.name.firstName")
    private WebElement brideFullName;

    @FindBy(id = "txt-lastName")
    private List<WebElement> lastName;

    @FindBy(id = "txt-parentsName")
    private WebElement parentsName;

    @FindBy(css = "input[type='file'][id='husband-photo']")
    private WebElement photoUpload;

    @FindBy(css = "input[type='file'][id='wife-photo']")
    private WebElement wifePhotoUpload;

    @FindBy(xpath = ".//*[@id='settingstab']/li[2]/a")
    private WebElement witnessInfoLink;

    @FindBy(id = "witnesses0.name.firstName")
    private WebElement witnessName1;

    @FindBy(id = "witnesses1.name.firstName")
    private WebElement witnessName2;

    @FindBy(id = "witnesses2.name.firstName")
    private WebElement witnessName3;

    @FindBy(id = "witnesses3.name.firstName")
    private WebElement witnessName4;

    @FindBy(xpath = ".//*[@id='settingstab']/li[3]/a")
    private WebElement checkListLink;

    @FindBy(id = "file1id")
    private WebElement memorandumofMarriage;

    @FindBy(id = "ageproofhusbandfile1id")
    private WebElement birthCertificateForBridegroom;

    @FindBy(id = "ageproofwifefile1id")
    private WebElement birthCertificateForBride;

    @FindBy(id = "addressproofhusbandfile5id")
    private WebElement proofofResidenceForBridegroom;

    @FindBy(id = "addressproofwifefile5id")
    private WebElement proofofResidenceForBride;

    @FindBy(xpath = ".//*[@id='registrationsuccess-form']/div/div[2]/div")
    private WebElement creationMessage;

    @FindBy(css = "input[type='button'][value='Close']")
    private WebElement closeButton;

    @FindBy(css = "input[id='inboxsearch'][type='text']")
    private WebElement inboxSearchTextBox;

    @FindBy(id = "Approve")
    private WebElement approveButton;

    @FindBy(id = "txt-serialNo")
    private WebElement marriageSerialNum;

    @FindBy(xpath = ".//*[@id='txt-pageNo']")
    private WebElement marriagePageNum;

    @FindBy(id = "dataEntrySubmit")
    private WebElement dataEntrySubmitButton;

    @FindBy(id = "file1id")
    private WebElement memorandumOfMarriage;

    @FindBy(className = "today active day")
    private WebElement todayDatePicker;

    @FindBy(id = "applicationNo")
    private WebElement applicationNumberTextBox;

    @FindBy(id = "btnregistrationsearch")
    private WebElement searchRegistrationButton;

    @FindBy(className = "dropchange")
    private WebElement actionDropDown;

    @FindBy(xpath = ".//*[@id='registration_table']/tbody/tr/td[10]/button")
    private WebElement editButton;

    @FindBy(xpath = ".//*[@id='form-updateregistration']/div[2]/div/button")
    private WebElement updateButton;

    @FindBy(id = "applicationNo")
    private WebElement applicationNo;

    @FindBy(xpath = ".//*[@id='registration_table']/tbody/tr[1]/td[10]/button")
    private WebElement reIssueCertificateLink;

    @FindBy(id = "txt-firstName")
    private WebElement firstName;

    @FindBy(id = "txt-residenceAddress")
    private WebElement residenceAddress;

    @FindBy(id = "txt-officeAddress")
    private WebElement officeAddress;

    @FindBy(id = "txt-phoneNo")
    private WebElement phoneNo;

    @FindBy(id = "marriageRegistrationType")
    private WebElement marriageRegistrationType;

    @FindBy(id = "official_inbox_wrapper")
    private WebElement workListTable;

    @FindBy(id = "button2")
    private WebElement closeButton2;

    @FindBy(id = "applicationNum")
    private WebElement marriageRegApplNum;

    @FindBy(xpath = ".//*[@id='registrationNum']")
    private WebElement marriageRegNum;

    @FindBy(id = "ageproofhusbandfile0id")
    private WebElement ageproofHusband;

    @FindBy(id = "ageproofwifefile0id")
    private WebElement ageproofWife;

    @FindBy(id = "Print Certificate")
    private WebElement printCertificateButton;

    public MarriageRegistrationPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterApplicantsInformation(MarriageRegistrationInformation marriageRegistrationInformation) {

        Boolean isPresent = driver.findElements(By.id("applicationNum")).size() > 0;

        if (isPresent) {
            enterText(marriageRegApplNum, "A" + get6DigitRandomInt(), driver);
            enterText(marriageRegNum, get6DigitRandomInt(), driver);
        }
        selectFromDropDown(registrationUnit, marriageRegistrationInformation.getRegistrationUnit(), driver);
        enterText(street.get(0), marriageRegistrationInformation.getStreet(), driver);
        selectFromDropDown(locality, marriageRegistrationInformation.getLocality(), driver);
        enterText(city, marriageRegistrationInformation.getCity(), driver);
        enterDate(dateOfMarriage, getPastDate(35), driver);
        selectFromDropDown(venueOfMarriage, marriageRegistrationInformation.getVenueOfMarriage(), driver);
        enterText(placeOfMarriage, marriageRegistrationInformation.getPlaceOfMarriage(), driver);
        uploadFile(marriagePhoto, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
    }


    public void enterBrideGroomInformation(MarriageRegistrationInformation marriageRegistrationInformation, String name) {

        enterText(driver.findElement(By.id(name + ".name.firstName")), marriageRegistrationInformation.getFullName(), driver);

        enterText(lastName.get(0), "N", driver);
        enterText(lastName.get(1), "P", driver);
        uploadFile(photoUpload, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);

        enterText(driver.findElement(By.name(name + ".parentsName")), marriageRegistrationInformation.getFathersMothersName(), driver);
        selectFromDropDown(driver.findElement(By.id(name + ".religion")), marriageRegistrationInformation.getReligion(), driver);

        WebElement ageInYears = driver.findElement(By.id(name + ".ageInYearsAsOnMarriage"));
        enterText(ageInYears, "30", driver);
        WebElement ageInMonth = driver.findElement(By.id(name + ".ageInMonthsAsOnMarriage"));
        enterText(ageInMonth, "8", driver);
        selectFromDropDown(driver.findElement(By.id(name + ".maritalStatus")), marriageRegistrationInformation.getStatusAtTheTimeMarriage(), driver);
        enterText(driver.findElement(By.id(name + ".contactInfo.residenceAddress")), marriageRegistrationInformation.getResidenceAddress(), driver);
        enterText(driver.findElement(By.id(name + ".contactInfo.officeAddress")), marriageRegistrationInformation.getOfficeAddress(), driver);
        enterText(driver.findElement(By.name(name + ".locality")), marriageRegistrationInformation.getLocality(), driver);
        enterText(driver.findElement(By.name(name + ".street")), marriageRegistrationInformation.getStreet(), driver);
        enterText(driver.findElement(By.name(name + ".city")), marriageRegistrationInformation.getCity(), driver);
        enterText(driver.findElement(By.id(name + ".contactInfo.mobileNo")), marriageRegistrationInformation.getPhoneNo(), driver);
        enterText(driver.findElement(By.id(name + ".occupation")), marriageRegistrationInformation.getOccupation(), driver);
        selectFromDropDown(driver.findElement(By.id(name + ".qualification")), marriageRegistrationInformation.getEducationQualification(), driver);
        uploadFile(wifePhotoUpload, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
    }

    public void entersWitnessesInformation() {
        waitForElementToBeClickable(witnessInfoLink, driver);
        jsClick(witnessInfoLink, driver);

        for (int i = 0; i <= 3; i++) {
            int res = 0;
            int sum = i + res;

            enterText(driver.findElement(By.id("witnesses" + sum + ".name.firstName")), "Witness Names", driver);
            enterText(driver.findElement(By.id("witnesses" + sum + ".name.lastName")), "Witness Last Names", driver);
            enterText(driver.findElement(By.id("witnesses[" + sum + "].relativeName")), "Relative Name", driver);
            enterText(driver.findElement(By.id("witnesses[" + sum + "].age")), "35", driver);
            enterText(driver.findElement(By.id("witnesses[" + sum + "].contactInfo.residenceAddress")), "Residence Address", driver);
        }
    }

    public void enterChecklist() {
        waitForElementToBeClickable(checkListLink, driver);
        jsClick(checkListLink, driver);

        uploadFile(memorandumofMarriage, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(birthCertificateForBridegroom, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(birthCertificateForBride, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(proofofResidenceForBridegroom, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(proofofResidenceForBride, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(ageproofHusband, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
        uploadFile(ageproofWife, System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg", driver);
    }

    public String getApplicationNumber() {
        String msg = creationMessage.getText();
        String number = msg.split("\\s")[6];
        return number;
    }

    public String getSuccessMessage() {
        return getTextFromWeb(creationMessage, driver);
    }

    public void close() {
        clickOnButton(closeButton, driver);
        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals(getEnvironmentURL() + "/mrs/registration/reissuecertificate")) {
                break;
            }
        }
        WebElement closeButton1 = driver.findElement(By.xpath(".//*[@id='page-content']/div/div[3]/div/a"));
        clickOnButton(closeButton1, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void approve() {
        clickOnButton(approveButton, driver);
    }

    public String getRegistrationNumber() {
        String msg = getTextFromWeb(creationMessage, driver);
        String number = msg.split("\\ ")[7];
        return number;
    }

    public void enterMarriageRegNum() {
        enterText(marriageSerialNum, get6DigitRandomInt(), driver);
        enterText(marriagePageNum, get6DigitRandomInt(), driver);
    }

    public String isSuccesful() {
        waitForElementToBeClickable(dataEntrySubmitButton, driver);
        jsClick(dataEntrySubmitButton, driver);
        String message = creationMessage.getText();
        return message;
    }

    public void searchForMarriageApplicationNumberToCollect(String applicationNumber, String type) {
        enterText(applicationNumberTextBox, applicationNumber, driver);
        if (type.equals("reissue")) {
            selectFromDropDown(marriageRegistrationType, "REISSUE", driver);
        }
        searchRegistrationButton.click();
    }

    public void clickOnCollectDropdown() {
        selectFromDropDown(actionDropDown, "Collect Fee", driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void searchForApplicationToModify(String applicationNumber) {
        enterText(applicationNumberTextBox, applicationNumber, driver);
        searchRegistrationButton.click();
    }

    public void clickOnEditButton() {
        clickOnButton(editButton, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void modifyAndUpdateMarriageApplication() {
        enterText(lastName.get(0), "Khan", driver);
        clickOnButton(updateButton, driver);
    }

    public String isSuccesfulForModification() {
        return creationMessage.getText();
    }

    public void closeMultipleWindows() {
        clickOnButton(closeButton, driver);
        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals("http://kurnool-qa.egovernments.org/mrs/registration/searchApproved")) {
                break;
            }
        }
        driver.findElement(By.linkText("Close")).click();
        switchToPreviouslyOpenedWindow(driver);

    }

    public void searchMarriageApplication(String applicationNumber) {
        enterText(applicationNo, applicationNumber, driver);
        clickOnButton(searchRegistrationButton, driver);
    }

    public void selectsReIssueCertificate() {
        clickOnButton(reIssueCertificateLink, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void entersMemorandumOfMarriage() {
        selectFromDropDown(registrationUnit, "Main office", driver);
        enterText(firstName, "Aazz", driver);
        enterText(residenceAddress, "Kurnool, Andra Pradesh", driver);
        enterText(officeAddress, "Kurnool, Andra Pradesh", driver);
        enterText(phoneNo, "9876589596", driver);
    }

    public String getReIssueNumber() {
        String message = creationMessage.getText();
        return message;
    }

    public void closeApplication() {
        clickOnButton(closeButton2, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void printMarriageCertificate() {
        clickOnButton(printCertificateButton, driver);
        await().atMost(10, SECONDS);
        driver.close();
        switchToPreviouslyOpenedWindow(driver);
    }
}
