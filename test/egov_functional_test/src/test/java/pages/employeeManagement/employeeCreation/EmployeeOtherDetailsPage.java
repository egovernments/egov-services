package pages.employeeManagement.employeeCreation;

import entities.employeeManagement.createEmployee.JurisdictionDetails;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class EmployeeOtherDetailsPage extends BasePage {

    @FindBy(css = ".glyphicon.glyphicon-plus")
    private WebElement addImageButton;

    // Jurisdiction Details
    @FindBy(css = "select[id='jurisdictions.jurisdictionsType']")
    private WebElement jurisdictionTypeSelectBox;

    @FindBy(css = "select[id='jurisdictions.boundary']")
    private WebElement jurisdictionListSelectBox;

    // Service Details
    @FindBy(css = "textarea[id='serviceHistory.serviceInfo']")
    private WebElement serviceAreaDescriptionTextBox;

    @FindBy(css = "input[id='serviceHistory.serviceFrom']")
    private WebElement dateTextBox;

    @FindBy(css = "textarea[id='serviceHistory.remarks']")
    private WebElement remarksTextBox;

    @FindBy(css = "input[id='serviceHistory.orderNo']")
    private WebElement orderNumberTextBox;

    @FindBy(css = "input[id='serviceHistory.documents']")
    private WebElement documentsbutton;

    // Probation Details
    @FindBy(css = "select[id='probation.designation']")
    private WebElement probationDesignationSelectBox;

    @FindBy(css = "input[id='probation.declaredOn']")
    private WebElement probationDeclaredDate;

    @FindBy(css = "input[id='probation.orderNo']")
    private WebElement probationOrderNumberTextbox;

    @FindBy(css = "input[id='probation.orderDate']")
    private WebElement probationOrderDateTextbox;

    @FindBy(css = "textarea[id='probation.remarks']")
    private WebElement probationRemarksTextBox;

    @FindBy(css = "[id='probation.documents']")
    private WebElement probationDocumentsButton;

    // Regularisation Details

    @FindBy(css = "select[id='regularisation.designation']")
    private WebElement regularisationDesignationSelectBox;

    @FindBy(css = "input[id='regularisation.declaredOn']")
    private WebElement regularisationDeclaredDate;

    @FindBy(css = "input[id='regularisation.orderNo']")
    private WebElement regularisationOrderNumberTextbox;

    @FindBy(css = "input[id='regularisation.orderDate']")
    private WebElement regularisationOrderDateTextbox;

    @FindBy(css = "textarea[id='regularisation.remarks']")
    private WebElement regularisationRemarksTextBox;

    @FindBy(css = "[id='regularisation.documents']")
    private WebElement regularisationDocumentsButton;

    // Educational Details
    @FindBy(css = "[id='education.qualification']")
    private WebElement qualificationTextBox;

    @FindBy(css = "[id='education.majorSubject']")
    private WebElement majorSubjectTextBox;

    @FindBy(css = "[id='education.yearOfPassing']")
    private WebElement yearOfPassingSelectBox;

    @FindBy(css = "[id='education.university']")
    private WebElement universityTextBox;

    @FindBy(css = "[id='education.documents']")
    private WebElement educationDocumentButton;

    // Technical Qualification Details
    @FindBy(css = "[id='technical.skill']")
    private WebElement technicalSkillsTextBox;

    @FindBy(css = "[id='technical.grade']")
    private WebElement technicalGradeTextBox;

    @FindBy(css = "[id='technical.yearOfPassing']")
    private WebElement technicalYearOfPassingSelectBox;

    @FindBy(css = "[id='technical.remarks']")
    private WebElement technicalRemarksTextBox;

    @FindBy(css = "[id='technical.documents']")
    private WebElement technicalDocumentsTextBox;

    // Departmental Test Details
    @FindBy(css = "[id='test.test']")
    private WebElement departmentalTestNameTextBox;

    @FindBy(css = "[id='test.yearOfPassing']")
    private WebElement departmentalYearOfPassingTextBox;

    @FindBy(css = "[id='test.remarks']")
    private WebElement deparmentalRemarksTextBox;

    @FindBy(css = "[id='test.documents']")
    private WebElement departmentalDocumentButton;

    @FindBy(css = ".btn.btn-primary")
    private WebElement addOrEditButton;

    @FindBy(id = "addEmployee")
    private WebElement submitButton;

    @FindBy(id = "serviceHistory.orderNo-error")
    private WebElement serviceOrderNoError;

    @FindBy(id = "serviceHistory.remarks-error")
    private WebElement serviceRemarksError;

    @FindBy(id = "education.qualification-error")
    private WebElement qualificationError;

    @FindBy(id = "education.majorSubject-error")
    private WebElement subjectError;

    @FindBy(id = "education.university-error")
    private WebElement universityError;

    private WebDriver webDriver;

    public EmployeeOtherDetailsPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterJurisdictionDetails(JurisdictionDetails jurisdictionDetails) {

        jsClick(webDriver.findElement(By.cssSelector("a[href='#jurisdictionList']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#jurisdictionDetailModal']")), webDriver);
        selectFromDropDown(jurisdictionTypeSelectBox, jurisdictionDetails.getJurisdictionType(), webDriver);
        selectFromDropDown(jurisdictionListSelectBox, jurisdictionDetails.getJurisdictionList(), webDriver);

        clickOnButton(webDriver.findElement(By.id("jurisdictionAddOrUpdate")), webDriver);
//        for (int i = 1; i <= 3; i++) {
//            jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#jurisdictionDetailModal']")), webDriver);
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            selectFromDropDown(webDriver.findElement(By.id("jurisdictions.jurisdictionsType")), "Ward", webDriver);
//            selectFromDropDown(webDriver.findElement(By.cssSelector("select[id='jurisdictions.boundary']")), "Election Ward No. " + i, webDriver);
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            clickOnButton(webDriver.findElement(By.id("jurisdictionAddOrUpdate")), webDriver);
//        }
    }

    public void enterServiceSectionDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#serviceSection']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#serviceHistoryDetailModal']")), webDriver);
        enterText(serviceAreaDescriptionTextBox, "serviceAreaDescription", webDriver);
        enterDate(dateTextBox, getCurrentDate(), webDriver);
        enterText(orderNumberTextBox, "Order " + get6DigitRandomInt(), webDriver);
        enterText(remarksTextBox, "Remarks Text", webDriver);
        clickOnButton(webDriver.findElement(By.id("serviceHistoryAddOrUpdate")), webDriver);
    }

    public void enterProbationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#probationDetailModal']")), webDriver);
        selectFromDropDown(probationDesignationSelectBox, "Assistant Engineer", webDriver);
        enterDate(probationDeclaredDate, getCurrentDate(), webDriver);
        enterText(probationOrderNumberTextbox, "Order " + get6DigitRandomInt(), webDriver);
        enterDate(probationOrderDateTextbox, getCurrentDate(), webDriver);
        enterText(probationRemarksTextBox, "Remarks Text", webDriver);
        clickOnButton(webDriver.findElement(By.id("probationAddOrUpdate")), webDriver);
    }

    public void enterRegularisationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#regularisationDetailModal']")), webDriver);
        selectFromDropDown(regularisationDesignationSelectBox, "Assistant Executive Engineer", webDriver);
        enterDate(regularisationDeclaredDate, getCurrentDate(), webDriver);
        enterText(regularisationOrderNumberTextbox, "Order " + get6DigitRandomInt(), webDriver);
        enterDate(regularisationOrderDateTextbox, getCurrentDate(), webDriver);
        enterText(regularisationRemarksTextBox, "Remarks Text", webDriver);
        clickOnButton(webDriver.findElement(By.id("regularisationAddOrUpdate")), webDriver);
    }

    public void enterEducationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#otherDetails']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#educationDetailModal']")), webDriver);
        enterText(qualificationTextBox, "B.Tech", webDriver);
        selectFromDropDown(yearOfPassingSelectBox, getCurrentYear(), webDriver);
        clickOnButton(webDriver.findElement(By.id("educationAddOrUpdate")), webDriver);
    }

    public void enterTechnicalQualificationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#technicalDetailModal']")), webDriver);
        enterText(technicalSkillsTextBox, "Skills", webDriver);
        clickOnButton(webDriver.findElement(By.id("technicalAddOrUpdate")), webDriver);
    }

    public void enterDepartmentalTestDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#testDetailModal']")), webDriver);
        enterText(departmentalTestNameTextBox, "departmentalTestNameTextBox", webDriver);
        selectFromDropDown(departmentalYearOfPassingTextBox, getCurrentYear(), webDriver);
    }

    public void submitCreateEmployee() {
        jsClick(webDriver.findElement(By.id("addEmployee")), webDriver);
        await().atMost(20, TimeUnit.SECONDS).until(() -> webDriver.findElements(By.id("sub")).size() > 0);
        clickOnButton(webDriver.findElement(By.cssSelector(".btn.btn-close")), webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void closeEmployeeSearch() {
        clickOnButton(webDriver.findElement(By.cssSelector(".btn.btn-close")), webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void searchEmployeeFormViewScreen(String applicationNumber) {
        enterText(webDriver.findElement(By.cssSelector("[id='code']")), applicationNumber, webDriver);
        clickOnButton(webDriver.findElement(By.cssSelector("[id='sub']")), webDriver);
        waitForElementToBeVisible(webDriver.findElement(By.cssSelector("[data-label='code']")), webDriver);
        Assert.assertEquals(webDriver.findElement(By.cssSelector("[data-label='code']")).getText(), applicationNumber);
        clickOnButton(webDriver.findElement(By.cssSelector(".btn.btn-close")), webDriver);
        switchToPreviouslyOpenedWindow(webDriver);
    }

    public void searchEmployeeFromUpdateScreen(String applicationNumber) {
        enterText(webDriver.findElement(By.cssSelector("[id='code']")), applicationNumber, webDriver);
        clickOnButton(webDriver.findElement(By.cssSelector("[id='sub']")), webDriver);
        waitForElementToBeVisible(webDriver.findElement(By.cssSelector("[data-label='code']")), webDriver);
        Assert.assertEquals(webDriver.findElement(By.cssSelector("[data-label='code']")).getText(), applicationNumber);
        clickOnButton(webDriver.findElement(By.cssSelector(".btn.btn-default.btn-action")), webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void updateServiceSectionDetails() {
        clickOnButton(webDriver.findElement(By.cssSelector("a[href='#serviceSection']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("[onclick=\"markEditIndex(0,'serviceHistoryDetailModal','serviceHistory')\"]")), webDriver);
        enterText(serviceAreaDescriptionTextBox, "Updated Service Area Description", webDriver);
        clickOnButton(webDriver.findElement(By.id("serviceHistoryAddOrUpdate")), webDriver);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateProbationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("[onclick=\"markEditIndex(0,'probationDetailModal','probation')\"]")), webDriver);
        enterDate(webDriver.findElement(By.cssSelector("[id='probation.declaredOn']")), getPastDate(1), webDriver);
        clickOnButton(webDriver.findElement(By.id("probationAddOrUpdate")), webDriver);
    }

    public void updateRegularisationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("[onclick=\"markEditIndex(0,'regularisationDetailModal','regularisation')\"]")), webDriver);
        enterDate(regularisationOrderDateTextbox, getPastDate(1), webDriver);
        clickOnButton(webDriver.findElement(By.id("regularisationAddOrUpdate")), webDriver);
    }

    public void updateEducationDetails() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        jsClick(webDriver.findElement(By.cssSelector("a[href='#otherDetails']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("[onclick=\"markEditIndex(0,'educationDetailModal','education')\"]")), webDriver);
        enterText(qualificationTextBox, "BE", webDriver);
        clickOnButton(webDriver.findElement(By.id("educationAddOrUpdate")), webDriver);
    }

    public void updateTechnicalQualificationDetails() {
        jsClick(webDriver.findElement(By.cssSelector("[onclick=\"markEditIndex(0,'technicalDetailModal','technical')\"]")), webDriver);
        enterText(technicalSkillsTextBox, "Java And Selenium", webDriver);
        jsClick(webDriver.findElement(By.id("technicalAddOrUpdate")), webDriver);
    }

    public void checkValidationForServiceDetails() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#serviceSection']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#serviceHistoryDetailModal']")), webDriver);
        enterText(serviceAreaDescriptionTextBox, "serviceAreaDescription", webDriver);
        enterDate(dateTextBox, getCurrentDate(), webDriver);
        checkField(orderNumberTextBox, serviceOrderNoError, "@@@@@", "1234", "Only alphanumeric with -/_ allowed.");
        checkField(remarksTextBox, serviceRemarksError, "@@@@@", "Remarks", "Only alphanumeric with -/_ allowed.");
        remarksTextBox.clear();
        enterText(remarksTextBox, "Remarks", webDriver);
        clickOnButton(webDriver.findElement(By.id("serviceHistoryAddOrUpdate")), webDriver);
    }

    private void checkField(WebElement element, WebElement errorElement, String wrongData, String correctData, String errorMsg) {
        enterText(element, wrongData, webDriver);
        element.sendKeys(Keys.TAB);
        if (errorElement.getText().equals(errorMsg)) {
            enterText(element, correctData, webDriver);
        }
    }

    public void checkEducationDetailsFields() {
        jsClick(webDriver.findElement(By.cssSelector("a[href='#otherDetails']")), webDriver);
        jsClick(webDriver.findElement(By.cssSelector("a[href='#'][data-target='#educationDetailModal']")), webDriver);
        checkField(qualificationTextBox, qualificationError, "1234", "B.tech", "Only alphabets with special characters allowed.");
        checkField(majorSubjectTextBox, subjectError, "1234", "Testing", "Only alphabets with special characters allowed.");
        selectFromDropDown(yearOfPassingSelectBox, "2002", webDriver);
        checkField(universityTextBox, universityError, "1234", "Test", "Only alphabets with special characters allowed.");
        clickOnButton(webDriver.findElement(By.id("educationAddOrUpdate")), webDriver);
    }
}
