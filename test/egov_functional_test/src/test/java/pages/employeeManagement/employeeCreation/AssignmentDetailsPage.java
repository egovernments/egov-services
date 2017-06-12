package pages.employeeManagement.employeeCreation;

import entities.employeeManagement.createEmployee.AssignmentDetails;
import org.joda.time.Seconds;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class AssignmentDetailsPage extends BasePage {

    @FindBy(css = ".glyphicon.glyphicon-plus")
    private WebElement addImageButton;

    @FindBy(css = "input[id='assignments.isPrimary'][value='true']")
    private WebElement isPrimaryTrueRadio;

    @FindBy(css = "input[id='assignments.isPrimary'][value='false']")
    private WebElement isPrimaryFalseRadio;

    @FindBy(css = "input[id='assignments.fromDate']")
    private WebElement fromDateTextBox;

    @FindBy(css = "input[id='assignments.toDate']")
    private WebElement toDateTextBox;

    @FindBy(css = "select[id='assignments.department']")
    private WebElement departmentSelectBox;

    @FindBy(css = "select[id='assignments.designation']")
    private WebElement designationSelectBox;

    @FindBy(css = "[id='assignments.position']")
    private WebElement positionSelectBox;

    @FindBy(css = "select[id='assignments.grade']")
    private WebElement gradeSelectBox;

    @FindBy(css = "select[id='assignments.function']")
    private WebElement functionSelectBox;

    @FindBy(css = "select[id='assignments.functionary']")
    private WebElement functionarySelectBox;

    @FindBy(css = "select[id='assignments.fund']")
    private WebElement fundSelectBox;

    @FindBy(css = "input[id='assignments.hod'][value='true']")
    private WebElement isHODTrueRadioButton;

    @FindBy(css = "input[id='assignments.hod'][value='false']")
    private WebElement isHODFalseRadioButton;

    @FindBy(css = "input[id='assignments.govtOrderNumber']")
    private WebElement govtOrderNumberTextBox;

    @FindBy(css = "input[id='assignments.documents']")
    private WebElement attachFileButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement addOrEditButton;

    @FindBy(id = "addEmployee")
    private WebElement submitButton;

    @FindBy(id = "assignments.govtOrderNumber-error")
    private WebElement govtOrderNumbererror;

//    @FindBy(xpath = ".//*[@id='agreementTableBody']/td[14]/button[1]")
    @FindBy(css = "td[data-label= 'Action'] button")
    private List<WebElement> editButtons;

    private WebDriver webDriver;

    public AssignmentDetailsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterAssignmentDetails(AssignmentDetails assignmentDetails) {
        webDriver.manage().window().maximize();
        jsClick(webDriver.findElement(By.cssSelector("a[href='#assignmentDetails']")), webDriver);
        jsClick(addImageButton, webDriver);
        if (assignmentDetails.getIsPrimary().equalsIgnoreCase("Yes")) {
            clickOnButton(isPrimaryTrueRadio, webDriver);
        } else {
            clickOnButton(isPrimaryFalseRadio, webDriver);
        }

        enterDate(fromDateTextBox, getCurrentDate(), webDriver);
        enterDate(toDateTextBox, getCurrentDate(), webDriver);
        selectFromDropDown(departmentSelectBox, assignmentDetails.getDepartment(), webDriver);
        selectFromDropDown(designationSelectBox, assignmentDetails.getDesignation(), webDriver);
        waitForElementToBeVisible(webDriver.findElement(By.cssSelector("[class='col-sm-6'] [id='assignments.position']")), webDriver);
        waitForElementToBeClickable(webDriver.findElement(By.cssSelector("[class='col-sm-6'] [id='assignments.position']")), webDriver);
//        webDriver.findElement(By.cssSelector("[class='col-sm-6'] [id='assignments.position']")).click();
        jsClick(positionSelectBox, webDriver);
        await().atMost(10, SECONDS).until(() -> positionSelectBox.findElements(By.tagName("option")).size() > 1);
        clickOnButton(positionSelectBox.findElements(By.tagName("option")).get(1), webDriver);

        clickOnButton(addOrEditButton, webDriver);
    }


    public void checkValidationFieldsInAssisgnmentTab() {
        await().atMost(10 , TimeUnit.SECONDS).until(()-> webDriver.findElements(By.cssSelector("[id='agreementTableBody'] tr")).size() > 0);
        jsClick(webDriver.findElements(By.cssSelector("td[data-label='Action'] button")).get(0),webDriver);
        enterText(govtOrderNumberTextBox,"@@@@@",webDriver);
        govtOrderNumberTextBox.sendKeys(Keys.TAB);
        if(govtOrderNumbererror.getText().equals("Only alphanumeric with -/_ allowed.")){
            enterText(govtOrderNumberTextBox,"1234",webDriver);
        }
        clickOnButton(addOrEditButton,webDriver);
    }
}
