package pages.lcms;

import entities.lcms.CreateLegalCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class LegalCaseManagementPage extends BasePage {

    private WebDriver webDriver;

    @FindBy(id = "courtType")
    private WebElement courtTypeSelect;

    @FindBy(id = "petitionTypeMaster")
    private WebElement petitionTypeSelect;

    @FindBy(id = "courtMaster")
    private WebElement courtNameSelect;

    @FindBy(id = "caseTypeMaster")
    private WebElement caseTypeSelect;

    @FindBy(id = "caseNumber")
    private WebElement caseNumberTextBox;

    @FindBy(id = "wpYear")
    private WebElement yearSelect;

    @FindBy(id = "caseDate")
    private WebElement caseDate;

    @FindBy(id = "caseTitle")
    private WebElement caseTitleTextBox;

    @FindBy(id = "prayer")
    private WebElement prayerTextBox;

    @FindBy(id = "noticeDate")
    private WebElement noticeDate;

    @FindBy(id = "bipartisanPetitionerDetailsList[0].name")
    private WebElement petitonerNameTextBox;

    @FindBy(id = "bipartisanRespondentDetailsList[0].name")
    private WebElement respondentNameTextBox;

    @FindBy(id = "buttonid")
    private WebElement saveButton;

    @FindBy(css = ".panel-body.custom-form")
    private WebElement successMessage;

    @FindBy(linkText = "Close")
    private WebElement closeButtonWithLinkText;

    @FindBy(xpath = ".//*[@id='legalCaseForm']/div/div/div/div/div/div[3]/div[4]")
    private WebElement caseFileNumber;

    @FindBy(id = "lcNumber")
    private WebElement lcNumberTextBox;

    @FindBy(id = "legalcaseReportSearch")
    private WebElement legalcaseReportSearchButton;

    @FindBy(id = "additionconn")
    private WebElement additionalActionSelect;

    @FindBy(id = "buttonSubmit")
    private WebElement updateButton;

    @FindBy(id = "createnewinterimorder")
    private WebElement createInterimOrderButton;

    @FindBy(id = "createnewhearings")
    private WebElement createHearingsButton;

    @FindBy(id = "hearingDate")
    private WebElement hearingDateTextBox;

    @FindBy(id = "purposeofHearings")
    private WebElement purposeOfHearingsTextBox;

    @FindBy(css = "div[role='alert']")
    private WebElement successMessage1;

    @FindBy(linkText = "Edit")
    private WebElement editLinkText;

    @FindBy(id = "interimOrder")
    private WebElement interimOrderTypeSelect;

    @FindBy(id = "ioDate")
    private WebElement interimOrderDate;

    @FindBy(id = "mpNumber")
    private WebElement interimOrderMPNumberTextBox;

    @FindBy(id = "notes")
    private WebElement interimOrderNotesTextArea;

    @FindBy(id = "save")
    private WebElement saveButton1;

    @FindBy(id = "orderDate")
    private WebElement judgmentOrderDate;

    @FindBy(id = "sentToDeptOn")
    private WebElement judgmentDateSentToZone;

    @FindBy(id = "judgmentType")
    private WebElement judgmentTypeSelect;

    @FindBy(id = "judgmentDetails")
    private WebElement judgmentDetailsTextArea;

    @FindBy(id = "dateOfCompliance")
    private WebElement dateOfComplianceTextBox;

    @FindBy(id = "complianceReport")
    private WebElement complianceReportTextArea;

    @FindBy(id = "IsCompliedNo")
    private WebElement judgmentCompiledNo;

    @FindBy(id = "IsCompliedInProgress")
    private WebElement judgmentInProgress;

    @FindBy(id = "details")
    private WebElement inProgressDetailsTextArea;

    @FindBy(id = "appeal[0].srNumber")
    private WebElement appealNumberTextBox;

    @FindBy(id = "appeal0.appealFiledOn")
    private WebElement appealedDate;

    @FindBy(id = "appeal0.appealFiledBy")
    private WebElement appealedFilledByTextBox;

    @FindBy(id = "contempt[0].caNumber")
    private WebElement contemptCANumberTextBox;

    @FindBy(id = "contempt0.receivingDate")
    private WebElement contemptReceivedDate;

    @FindBy(id = "implementationFailure")
    private WebElement implementationFailureSelect;

    @FindBy(name = "isStatusExcluded")
    private WebElement excludeJudgmentImplementationCheckBox;

    @FindBy(id = "disposalDate")
    private WebElement closeDisposalDate;

    @FindBy(id = "disposalDetails")
    private WebElement closeDisposalDetailsTextArea;

    @FindBy(id = "standingCouncilName")
    private WebElement standingCouncilName;

    @FindBy(id = "assignedDate")
    private WebElement standingCoucilAssignedDate;

    @FindBy(id = "vakalatdate")
    private WebElement standingCouncilVakalatDate;

    @FindBy(id = "subitstandingcouncil")
    private WebElement submitStandingCouncil;

    @FindBy(id = "buttonid")
    private WebElement affidavitSubmit;

    private String message = null;
    private String caseFileNo = null;

    public LegalCaseManagementPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterLegalCaseDetails(CreateLegalCase createLegalCase) {

        webDriver.manage().window().maximize();

        selectFromDropDown(courtTypeSelect, createLegalCase.getTypeOfCourt(), webDriver);

        selectFromDropDown(petitionTypeSelect, createLegalCase.getPetitionType(), webDriver);

        selectFromDropDown(courtNameSelect, createLegalCase.getCourtName(), webDriver);

        selectFromDropDown(caseTypeSelect, createLegalCase.getCaseCategory(), webDriver);

        enterText(caseNumberTextBox, get6DigitRandomInt(), webDriver);

        selectFromDropDown(yearSelect, "2017", webDriver);

        enterDate(caseDate, getPreviousDate(), webDriver);

        enterText(caseTitleTextBox, "Testing", webDriver);

        enterText(prayerTextBox, "Tester", webDriver);

        enterDate(noticeDate, getPreviousDate(), webDriver);

        enterText(petitonerNameTextBox, createLegalCase.getPetitionerName(), webDriver);

        enterText(respondentNameTextBox, createLegalCase.getRespondentName(), webDriver);

        if (petitionTypeSelect.getText().equalsIgnoreCase("Select from below")) {
            selectFromDropDown(petitionTypeSelect, createLegalCase.getPetitionType(), webDriver);
        }

        clickOnButton(saveButton, webDriver);

        switchToNewlyOpenedWindow(webDriver);
    }

    public String closesAcknowledgementForm() {

        message = getTextFromWeb(successMessage.findElements(By.tagName("div")).get(0), webDriver);

        waitForElementToBeVisible(caseFileNumber, webDriver);
        caseFileNo = getTextFromWeb(caseFileNumber, webDriver);

        jsClick(closeButtonWithLinkText, webDriver);

        switchToPreviouslyOpenedWindow(webDriver);

        return message + ">" + caseFileNo;
    }

    public void searchCaseFile(String caseFileNumber) {

        enterText(lcNumberTextBox, caseFileNumber, webDriver);

        clickOnButton(excludeJudgmentImplementationCheckBox, webDriver);

        clickOnButton(legalcaseReportSearchButton, webDriver);
    }

    public void clickOnCorrespondingAction(String action) {

        if (webDriver.findElements(By.id("additionconn")).size() == 0) {
            clickOnButton(excludeJudgmentImplementationCheckBox, webDriver);
            clickOnButton(legalcaseReportSearchButton, webDriver);
        }
        switch
                (action) {

            case "editLegalCase":

                selectFromDropDown(additionalActionSelect, "Edit legalCase", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterEditLegalCaseDetails();

                break;

            case "hearings":

                selectFromDropDown(additionalActionSelect, "Hearings", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                clickOnCreateHearings();

                break;

            case "editHearings":

                selectFromDropDown(additionalActionSelect, "Hearings", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                clickOnEditHearings();

                break;

            case "interimOrder":

                selectFromDropDown(additionalActionSelect, "Interim Order", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                clickOnCreateInterimOrder();

                break;

            case "editInterim":

                selectFromDropDown(additionalActionSelect, "Interim Order", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                clickOnEditInterimOrder();

                break;

            case "addOrEditStandingCouncil":

                selectFromDropDown(additionalActionSelect, "Add/Edit Standing Counsel", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterStandingCouncilDetails();

                break;

            case "addOrEditCounterAffidavit":

                selectFromDropDown(additionalActionSelect, "Add/Edit Counter Affidavit Details", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterCounterAffidavitDetails();

                break;

            case "judgment":

                selectFromDropDown(additionalActionSelect, "Judgment", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterJudgmentDetails();

                break;

            case "editJudgment":

                selectFromDropDown(additionalActionSelect, "Edit Judgment", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterEditJudgmentDetails();

                break;

            case "judgmentImplementation":

                selectFromDropDown(additionalActionSelect, "Judgment Implementation", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                break;

            case "editJudgmentImplementation":

                selectFromDropDown(additionalActionSelect, "Edit Judgment Implementation", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                break;

            case "closeCase":

                selectFromDropDown(additionalActionSelect, "Close Case", webDriver);

                switchToNewlyOpenedWindow(webDriver);

                enterCloseCaseDetails();
                break;
        }
    }

    private void enterEditLegalCaseDetails() {

        enterText(caseTitleTextBox, "Tester", webDriver);

        enterText(petitonerNameTextBox, "Good Tester", webDriver);

        clickOnButton(updateButton, webDriver);

        switchToNewlyOpenedWindow(webDriver);
    }

    private void clickOnCreateHearings() {

        clickOnButton(createHearingsButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);

        enterDate(hearingDateTextBox, getPreviousDate(), webDriver);
        enterText(purposeOfHearingsTextBox, "Normal Use", webDriver);

        clickOnButton(saveButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public String closeCreatedOrUpdatedPage() {
        if (webDriver.findElements(By.cssSelector("div[role='alert']")).size() > 0) {
            message = getTextFromWeb(successMessage1, webDriver);
        } else {
            message = getTextFromWeb(webDriver.findElement(By.cssSelector("div[class='role=\"alert\"']")), webDriver);
        }

        clickOnButton(closeButtonWithLinkText, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
        return message;
    }

    private void clickOnEditHearings() {

        clickOnButton(editLinkText, webDriver);
        switchToNewlyOpenedWindow(webDriver);

        enterText(purposeOfHearingsTextBox, "General Purpose", webDriver);

        clickOnButton(saveButton, webDriver);
    }

    private void clickOnCreateInterimOrder() {

        clickOnButton(createInterimOrderButton, webDriver);

        switchToNewlyOpenedWindow(webDriver);

        selectFromDropDown(interimOrderTypeSelect, "Interim order", webDriver);

        enterDate(interimOrderDate, getPreviousDate(), webDriver);
        enterText(interimOrderMPNumberTextBox, get6DigitRandomInt(), webDriver);

        enterText(interimOrderNotesTextArea, "Creation Of Interim Order", webDriver);

        clickOnButton(saveButton1, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private void clickOnEditInterimOrder() {

        clickOnButton(editLinkText, webDriver);
        switchToNewlyOpenedWindow(webDriver);

        enterText(interimOrderNotesTextArea, "Editing Interim Order", webDriver);

        clickOnButton(updateButton, webDriver);
    }

    private void enterStandingCouncilDetails() {

        enterText(standingCouncilName, "Kothapalli Ram Mohan Chowdary(APAT)", webDriver);
//        enterText(standingCouncilName, "S.D.Gowd Advocate & MSC High Court", webDriver);
        WebElement element = webDriver.findElement(By.className("tt-dataset-0"));
        clickOnButton(element, webDriver);

        enterDate(standingCoucilAssignedDate, getPreviousDate(), webDriver);
        enterDate(standingCouncilVakalatDate, getPreviousDate(), webDriver);

        clickOnButton(submitStandingCouncil, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private void enterCounterAffidavitDetails() {
        enterDate(webDriver.findElement(By.id("pwrDueDate")), getPreviousDate(), webDriver);
        enterDate(webDriver.findElement(By.id("counterAffidavitDueDate")), getPreviousDate(), webDriver);
        clickOnButton(affidavitSubmit, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private void enterJudgmentDetails() {

        enterDate(judgmentOrderDate, getPreviousDate(), webDriver);
        enterDate(judgmentDateSentToZone, getPreviousDate(), webDriver);

        selectFromDropDown(judgmentTypeSelect, "Enquiry", webDriver);
        enterText(judgmentDetailsTextArea, "Judgment Is Under Process", webDriver);

        clickOnButton(saveButton1, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    private void enterEditJudgmentDetails() {

        selectFromDropDown(judgmentTypeSelect, "Allowed", webDriver);
        enterText(judgmentDetailsTextArea, "Judgment Is Allowed", webDriver);

        clickOnButton(updateButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void enterJudgmentImplementationDetails(String mode) {

        switch (mode) {

            case "Yes":
                enterDate(dateOfComplianceTextBox, getPreviousDate(), webDriver);
                enterText(complianceReportTextArea, "Judgment Implementation", webDriver);
                clickOnButton(saveButton, webDriver);
                break;

            case "No_Appeal":
                jsClick(judgmentCompiledNo, webDriver);

                selectFromDropDown(implementationFailureSelect, "Appeal", webDriver);
                enterText(appealNumberTextBox, get6DigitRandomInt(), webDriver);
                enterDate(appealedDate, getPreviousDate(), webDriver);
                enterText(appealedFilledByTextBox, "Tester", webDriver);

                clickOnButton(saveButton, webDriver);
                break;

            case "No_Contempt":

                jsClick(judgmentCompiledNo, webDriver);

                selectFromDropDown(implementationFailureSelect, "Contempt", webDriver);
                enterText(contemptCANumberTextBox, get6DigitRandomInt(), webDriver);

                enterDate(contemptReceivedDate, getPreviousDate(), webDriver);

                clickOnButton(saveButton, webDriver);
                break;


            case "InProgress":

                jsClick(judgmentInProgress, webDriver);
                enterText(inProgressDetailsTextArea, "Details Of InProgress", webDriver);

                clickOnButton(saveButton, webDriver);
                break;

            // From here we enter the edit details of judgment implementation
            case "edit_Yes":

                enterText(complianceReportTextArea, "Judgment Implementation Edited", webDriver);
                clickOnButton(saveButton, webDriver);
                break;

            case "edit_No_Appeal":

                enterText(appealedFilledByTextBox, "Tester1", webDriver);

                clickOnButton(saveButton, webDriver);
                break;

            case "edit_No_Contempt":

                enterText(contemptCANumberTextBox, get6DigitRandomInt(), webDriver);
                clickOnButton(saveButton, webDriver);
                break;

            case "edit_InProgress":
                enterText(inProgressDetailsTextArea, "Edited details Of InProgress", webDriver);
                clickOnButton(saveButton, webDriver);
                break;
        }

        switchToNewlyOpenedWindow(webDriver);
    }

    private void enterCloseCaseDetails() {
        enterDate(closeDisposalDate, getPreviousDate(), webDriver);
        enterText(closeDisposalDetailsTextArea, "Case is Closed", webDriver);

        clickOnButton(saveButton, webDriver);
    }
}
