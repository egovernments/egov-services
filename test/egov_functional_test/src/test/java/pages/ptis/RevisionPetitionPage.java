package pages.ptis;

import entities.ptis.HearingDetails;
import entities.ptis.RevisionPetitionDetails;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import pages.BasePage;

public class RevisionPetitionPage extends BasePage {

    private WebDriver webDriver;
    @FindBy(id = "assessmentNum")
    private WebElement revisionPetitiontextBox;
    @FindBy(id = "assessmentform_search")
    private WebElement rpSearchButton;
    @FindBy(id = "header_2")
    private WebElement revisionPetitionHeader;
    @FindBy(id = "details")
    private WebElement revisionPetitionDetailTextBox;
    @FindBy(id = "plannedHearingDtId")
    private WebElement hearingDateTextBox;
    @FindBy(id = "hearingTime")
    private WebElement hearingTimeSelection;
    @FindBy(id = "hearingVenue")
    private WebElement venueTextBox;
    @FindBy(id = "Forward")
    private WebElement forwardButton;
    @FindBy(id = "approverComments")
    private WebElement approveRemarkHearingTextBox;
    @FindBy(id = "reasonForModify")
    private WebElement reasonForModificationDropDown;
    @FindBy(id = "inspectionRemarks")
    private WebElement inspectionTextBox;
    @FindBy(id = "Approve")
    private WebElement approveRpbutton;
    @FindBy(id = "Print Endoresement")
    private WebElement printEndoresementNoticeButton;
    @FindBy(id = "buttonClose")
    private WebElement PrintCloseButton;
    @FindBy(id = "Print Special Notice")
    private WebElement printSpecialNotice;

    public RevisionPetitionPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void revisionPetitionSearchScreen(String Rpscreen) {
        enterText(revisionPetitiontextBox, Rpscreen, webDriver);
        clickOnButton(rpSearchButton, webDriver);
    }

    public void chooseRevisionPetitionHeader() {
        clickOnButton(revisionPetitionHeader, webDriver);
    }

    public void revisionPetitionBlock(RevisionPetitionDetails revisionPetitionDetails) {
        enterText(revisionPetitionDetailTextBox, revisionPetitionDetails.getRevisionPetitionDetail(), webDriver);
    }

    public void enterHearingDetails(HearingDetails hearingDetails) {
        enterText(hearingDateTextBox, hearingDetails.getHearingDate(), webDriver);
        selectFromDropDown(hearingTimeSelection, hearingDetails.getHearingTime(), webDriver);
        enterText(venueTextBox, hearingDetails.getVenue(), webDriver);
        clickOnButton(forwardButton, webDriver);
    }

    public void enterApproverRemarks() {
        enterText(approveRemarkHearingTextBox, "ApproverRemarkOfRP", webDriver);
    }

    public void selectReasonForModification() {
        new Select(reasonForModificationDropDown).selectByIndex(1);
    }

    public void enterInspectionDetails() {
        enterText(inspectionTextBox, "Inspection Details of property", webDriver);
    }

    public void rpApprove() {
        clickOnButton(approveRpbutton, webDriver);
    }

    public void clickPrintEndoresementNotice() {
        clickOnButton(printEndoresementNoticeButton, webDriver);
        clickOnButton(PrintCloseButton, webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void clickOnPrintSpecialNotice() {
        clickOnButton(printSpecialNotice, webDriver);
        switchToNewlyOpenedWindow(webDriver);
        webDriver.close();
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
