package pages.councilManagement;

import entities.councilManagement.CreatePreambleDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.BasePage;

import java.util.List;

public class CouncilManagementPage extends BasePage {

    private WebDriver webDriver;

    @FindBy(id = "department")
    private WebElement preambleDepartment;

    @FindBy(id = "sanctionAmount")
    private WebElement sanctionAmount;

    @FindBy(id = "gistOfPreamble")
    private WebElement gistOfPreamble;

    @FindBy(id = "attachments")
    private WebElement attachment;

    @FindBy(id = "wards")
    private WebElement wards;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(id = "Approve")
    private WebElement approve;

    @FindBy(id = "preambleNumber")
    private WebElement preambleNumberTextBox;

    @FindBy(id = "btnsearch")
    private WebElement searchbutton;

    @FindBy(id = "committeeType")
    private WebElement committeeTypeSelect;

    @FindBy(id = "btnsave")
    private WebElement saveButton;

    @FindBy(id = "agendaNumber")
    private WebElement agendaNumberTextBox;

    @FindBy(id = "meetingDate")
    private WebElement meetingDateText;

    @FindBy(id = "meetingTime")
    private WebElement meetingTimeSelect;

    @FindBy(id = "meetingLocation")
    private WebElement meetingLocationText;

    @FindBy(id = "buttonSubmit")
    private WebElement createButton;

    @FindBy(linkText = "Close")
    private WebElement CloseButton;

    @FindBy(id = "meetingNumber")
    private WebElement meetingNumberText;

    @FindBy(id = "committeechk")
    private WebElement committeechkCheckBox;

    @FindBy(id = "finalizeAttendanceBtn")
    private WebElement finalizeAttendanceBtn;

    @FindBy(xpath = ".//*[@id='showModal']")
    private WebElement resolutionComment;

    @FindBy(id = "meetingMOMs0.resolutionStatus")
    private WebElement resolutionStatusSelect;

    @FindBy(id = "buttonFinalSubmit")
    private WebElement resolutionPDFgenerationButton;

    @FindBy(xpath = ".//*[@id='textarea-updatedcontent']")
    private WebElement textEntry;

    @FindBy(xpath = ".//*[@id='textarea-btnupdate']")
    private WebElement updateButton;


    public CouncilManagementPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterCreatePreambleDetails(CreatePreambleDetails createPreambleDetails) {
        selectFromDropDown(preambleDepartment, createPreambleDetails.getPreambleDepartment(), webDriver);
        enterText(sanctionAmount, createPreambleDetails.getAmount(), webDriver);
        enterText(gistOfPreamble, createPreambleDetails.getGistOfPreamble(), webDriver);
        uploadFile(attachment, System.getProperty("user.dir") + "/src/test/resources/dataFiles/PTISTestData.xlsx", webDriver);
        Select sel = new Select(wards);
        for (int i = 1; i < 3; i++) {
            sel.selectByIndex(i);
        }
    }


    public String getPreambleNumber() {
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".col-sm-3.add-margin.view-content"));
        return elements.get(0).getText();
    }

    public String getStatus() {
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".col-sm-3.add-margin.view-content"));
        String ele = elements.get(1).getText();
        clickOnButton(CloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return ele;
    }

    public String approve() {
        approve.click();
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".col-sm-3.add-margin.view-content"));
        String ele = elements.get(1).getText();
        clickOnButton(CloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return ele;
    }

    public void enterCreateAgenda(CreatePreambleDetails createPreambleDetails) {
        clickOnButton(searchbutton, webDriver);
        List<WebElement> ele = webDriver.findElements(By.cssSelector(".btn.btn-xs.btn-secondary.add"));
        jsClick(ele.get(0), webDriver);
        selectFromDropDown(committeeTypeSelect, createPreambleDetails.getCommitteeType(), webDriver);
        jsClick(saveButton, webDriver);
    }

    public void enterCreateAgendaDetails(String preambleNumber) {
        enterText(preambleNumberTextBox, preambleNumber, webDriver);
    }

    public String getAgendaNumber() {
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".col-sm-3.add-margin.view-content"));
        String agendaNumber = elements.get(2).getText();
        clickOnButton(CloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return agendaNumber;
    }

    public void enterCreateMeetingDetails(String agendaNumber) {
        enterText(agendaNumberTextBox, agendaNumber, webDriver);
        clickOnButton(searchbutton, webDriver);
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".btn.btn-xs.btn-secondary.view"));
        jsClick(elements.get(0), webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void enterCouncilMeetingDetails(CreatePreambleDetails createMeetingData) {
        enterText(meetingDateText, createMeetingData.getcouncilMeetingDate(), webDriver);
        selectFromDropDown(meetingTimeSelect, createMeetingData.getCouncilMeetingTime(), webDriver);
        enterText(meetingLocationText, createMeetingData.getCouncilMeetingPlace(), webDriver);
        jsClick(createButton, webDriver);
    }


    public String getMeetingNumber() {
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".col-sm-3.add-margin.view-content"));
        String meetingNumber = elements.get(1).getText();
        clickOnButton(CloseButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
        clickOnButton(CloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return meetingNumber;
    }

    public void enterMeetingNumber(String meetingNumber) {
        enterText(meetingNumberText, meetingNumber, webDriver);
        clickOnButton(searchbutton, webDriver);

    }

    public void enterAttendanceDetails() {
        List<WebElement> elements = webDriver.findElements(By.className("dropchange"));
        selectFromDropDown(elements.get(0), "Edit", webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void finalizeAttendance() {
        committeechkCheckBox.click();
        clickOnButton(finalizeAttendanceBtn, webDriver);
        webDriver.switchTo().activeElement();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div[class~='modal-footer'] button[data-bb-handler~='confirm']")));
        WebElement element = webDriver.findElement(By.cssSelector("div[class~='modal-footer'] button[data-bb-handler~='confirm']"));
        element.click();
        webDriver.close();
        switchToNewlyOpenedWindow(webDriver);
        webDriver.close();
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void searchMeetingNumber(String meetingNumber) {
        enterText(meetingNumberText, meetingNumber, webDriver);
        clickOnButton(searchbutton, webDriver);
        List<WebElement> elements = webDriver.findElements(By.cssSelector(".btn.btn-xs.btn-secondary.view"));
        jsClick(elements.get(0), webDriver);
        switchToNewlyOpenedWindow(webDriver);

    }

    public void enterCouncilMOMDetails(CreatePreambleDetails councilMOMData) {
        clickOnButton(resolutionComment, webDriver);
        webDriver.switchTo().activeElement();
        enterText(textEntry, councilMOMData.getCouncilMOMResolution(), webDriver);
        clickOnButton(updateButton, webDriver);
        webDriver.switchTo().activeElement();
        selectFromDropDown(resolutionStatusSelect, councilMOMData.getCouncilMOMAction(), webDriver);
        clickOnButton(resolutionPDFgenerationButton, webDriver);
        webDriver.switchTo().activeElement();
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("html/body/div[4]/div/div/div[2]/button[2]")));
        WebElement element = webDriver.findElement(By.xpath("html/body/div[4]/div/div/div[2]/button[2]"));
        element.click();
        switchToNewlyOpenedWindow(webDriver);
        webDriver.close();
        switchToNewlyOpenedWindow(webDriver);
        clickOnButton(CloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
