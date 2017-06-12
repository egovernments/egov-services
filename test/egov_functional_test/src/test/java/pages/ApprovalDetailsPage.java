package pages;

import entities.ApprovalDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ApprovalDetailsPage extends BasePage {
    private WebDriver webDriver;

    @FindBy(id = "approvalDepartment")
    private WebElement approvalDepartmentSelect;

    @FindBy(id = "approvalDesignation")
    private WebElement approvalDesignationSelect;

    @FindBy(id = "approvalPosition")
    private WebElement approvalPositionSelect;

    @FindBy(xpath = ".//*[@id='complaintUpdate']/div[6]/div/button[1]")
    private WebElement grievanceSubmit;

    @FindBy(linkText = "Close")
    private WebElement closeButton;

//    @FindBy(css = "textarea[name='approvalComent']")
//    private WebElement approverRemarkTextBox;

    @FindBy(id = "Approve")
    private WebElement approveButton;

    @FindBy(css = "textarea[name='approvalComent']")
    private WebElement approvalCommentsTextBox;

    @FindBy(css = "textarea[name='approverComments']")
    private WebElement approverCommentsTextBox;

    @FindBy(id = "approverDepartment")
    private WebElement approverDepartmentSelection;

    @FindBy(id = "approverDesignation")
    private WebElement approverDesignationSelection;

    @FindBy(id = "approverPositionId")
    private WebElement approverSelection;

    @FindBy(id = "Forward")
    private WebElement forwardButton;

    @FindBy(id = "approvalDepartment")
    private WebElement approvalDepartmentSelection;

    @FindBy(id = "approvalDesignation")
    private WebElement approvalDesignationSelection;

    public ApprovalDetailsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterApproverDetails(ApprovalDetails approvalDetails) {

        selectFromDropDown(approverDepartmentSelection, approvalDetails.getApproverDepartment(), webDriver);

        await().atMost(50, SECONDS).until(() -> new Select(approverDesignationSelection).getOptions().size() > 1);
        selectFromDropDown(approverDesignationSelection, approvalDetails.getApproverDesignation(), webDriver);

        await().atMost(50, SECONDS).until(() -> new Select(approverSelection).getOptions().size() > 1);
        selectFromDropDown(approverSelection, approvalDetails.getApprover(), webDriver);

        if (webDriver.findElements(By.cssSelector("textarea[name='approverComments']")).size() > 0) {
            enterText(approverCommentsTextBox, approvalDetails.getApproverRemarks(), webDriver);
        }
    }

    public void enterApprovalDetails(ApprovalDetails approvalDetails) {

        selectFromDropDown(approvalDepartmentSelection, approvalDetails.getApproverDepartment(), webDriver);
        await().atMost(10, SECONDS).until(() -> new Select(approvalDesignationSelection).getOptions().size() > 1);

        selectFromDropDown(approvalDesignationSelection, approvalDetails.getApproverDesignation(), webDriver);
        await().atMost(10, SECONDS).until(() -> new Select(approvalPositionSelect).getOptions().size() > 1);

        selectFromDropDown(approvalPositionSelect, approvalDetails.getApprover(), webDriver);
        if (approvalCommentsTextBox.isDisplayed()) {
            enterText(approvalCommentsTextBox, approvalDetails.getApproverRemarks(), webDriver);
        }
    }

    public void forward() {
        clickOnButton(forwardButton, webDriver);
    }

    public void createGrievance() {
        grievanceSubmit.click();
        closeButton.click();
        switchToPreviouslyOpenedWindow(webDriver);
    }
}
