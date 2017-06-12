package pages.Grievances;

import entities.grievances.CreateComplaintDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;


public class GrievancesPage extends BasePage {
    private WebDriver webDriver;

    @FindBy(xpath = "html/body/div[1]/div/div[1]/div[2]/div[2]/a/div[2]")
    private WebElement registerComplaintLink;

    @FindBy(id = "f-name")
    private WebElement citizenNameTextBox;

    @FindBy(id = "mob-no")
    private WebElement mobNoTextBox;

    @FindBy(id = "email")
    private WebElement emailIdTextBox;

    @FindBy(id = "complaintTypeCategory")
    private WebElement complaintTypeCategorySelect;

    @FindBy(id = "complaintType")
    private WebElement complaintTypeSelect;

    @FindBy(id = "complaintTypeName")
    private WebElement complaintTypeTextBox;

    @FindBy(id = "doc")
    private WebElement grievanceDetailsText;

    @FindBy(id = "location")
    private WebElement grievanceLocationText;

    @FindBy(id = "landmarkDetails")
    private WebElement locationLandmarkText;

    @FindBy(css = "button[class='btn btn-primary']")
    private WebElement createGrievanceButton;

    @FindBy(xpath = ".//*[@id='main']/div[1]/div/div/div[1]/div/strong")
    private WebElement successMsg;

    @FindBy(linkText = "Grievance Redressal")
    private WebElement newRequestLink;

    @FindBy(linkText = "Register Grievance")
    private WebElement registerComplaint;

    @FindBy(id = "ctn_no")
    private WebElement CRNNumber;

    @FindBy(id = "status")
    private WebElement selectStatus;

    @FindBy(id = "inc_messge")
    private WebElement incMessageBox;

    @FindBy(xpath = "html/body/div[1]/div/div[1]/div/div/div[1]/div/strong")
    private WebElement acknMsg;

    @FindBy(linkText = "Close")
    private WebElement closeButton;

    @FindBy(id = "receivingMode1")
    private WebElement receivingModeRadio;

    @FindBy(xpath = "html/body/div[3]/header/div/ul/li[2]/a")
    private WebElement draftButton;

    @FindBy(id = "status")
    private WebElement statusSelect;

    @FindBy(xpath = ".//*[@id='inbox-template']/div[1]/div[1]/input")
    private WebElement searchCitizenInbox;

    @FindBy(xpath = ".//*[@id='tabelPortal']/tbody[2]/tr[1]/td[2]")
    private WebElement complaintLink;

    @FindBy(css = "button[type=submit]")
    private WebElement submitButton;

    public GrievancesPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void openCreateGrievancePage() {
        clickOnButton(registerComplaintLink, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void enterCitizenContactDetails(CreateComplaintDetails createComplaintDetails) {
        clickOnButton(receivingModeRadio, webDriver);
        enterText(citizenNameTextBox, createComplaintDetails.getCitizenName(), webDriver);
        enterText(mobNoTextBox, createComplaintDetails.getcitizenMobNo(), webDriver);
//        enterText(emailIdTextBox, createComplaintDetails.getEmailId(), webDriver);
    }

    public String enterGrievanceDetails(CreateComplaintDetails createComplaintDetails, String user) {
        if (user.equals("citizen")) {
            enterText(complaintTypeTextBox, createComplaintDetails.getGrievanceType(), webDriver);
            WebElement dropdown1 = webDriver.findElement(By.className("tt-highlight"));
            dropdown1.click();
        } else {
            selectFromDropDown(complaintTypeCategorySelect, createComplaintDetails.getGrievanceCategory(), webDriver);
            selectFromDropDown(complaintTypeSelect, createComplaintDetails.getGrievanceType(), webDriver);
        }
        enterText(grievanceDetailsText, createComplaintDetails.getGrievanceDetails(), webDriver);
        enterText(grievanceLocationText, createComplaintDetails.getGrievanceLocation(), webDriver);
        WebElement dropdown = webDriver.findElement(By.className("tt-highlight"));
        dropdown.click();
        grievanceLocationText.sendKeys(Keys.TAB);
        enterText(locationLandmarkText, createComplaintDetails.getLocationLandmark(), webDriver);
        clickOnButton(createGrievanceButton, webDriver);
        return successMsg.getText();
    }

    public void getRegisterComplaintPage() {
        clickOnButton(newRequestLink, webDriver);
        clickOnButton(registerComplaint, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public String getCRN() {
        String CrnNum = CRNNumber.getText();
        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return CrnNum;
    }


    public String officialMarkStatus(String status) {
        selectFromDropDown(selectStatus, status, webDriver);
        enterText(incMessageBox, status, webDriver);
        clickOnButton(submitButton, webDriver);
        String success = webDriver.findElement(By.xpath(".//*[@id='main']/div[1]/div/div/div[1]/div/strong")).getText();
        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return success;
    }

    public void getProcessingStatus() {
        selectFromDropDown(statusSelect, "PROCESSING", webDriver);
    }

    public void searchInCitizenInbox(String crn) {
        webDriver.navigate().refresh();
        webDriver.findElement(By.xpath(".//*[@id='totalServicesAppliedDiv']/div/div[2]")).click();
//        enterText(searchCitizenInbox, crn, webDriver);
        clickOnButton(complaintLink, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void withdrawComplaint(String complaintStatus) {
//        selectFromDropDown(selectStatus, complaintStatus, webDriver);
        enterText(incMessageBox, complaintStatus, webDriver);
        webDriver.findElement(By.cssSelector("button[class='btn btn-primary'][type='submit'")).click();
        clickOnButton(closeButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
