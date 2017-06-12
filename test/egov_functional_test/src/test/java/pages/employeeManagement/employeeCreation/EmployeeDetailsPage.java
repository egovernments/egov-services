package pages.employeeManagement.employeeCreation;

import entities.employeeManagement.createEmployee.EmployeeDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class EmployeeDetailsPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "user.name")
    private WebElement employeeNameTextBox;

    @FindBy(id = "code")
    private WebElement employeeCodeTextBox;

    @FindBy(id = "employeeType")
    private WebElement employeeTypeBox;

    @FindBy(id = "employeeStatus")
    private WebElement employeeStatusBox;

    @FindBy(id = "group")
    private WebElement employeeGroupBox;

    @FindBy(id = "user.dob")
    private WebElement dobTextBox;

    @FindBy(id = "user.gender")
    private WebElement genderDropdown;

    @FindBy(id = "maritalStatus")
    private WebElement maritalStatusBox;

    @FindBy(id = "user.userName")
    private WebElement userNameTextBox;

    @FindBy(css = "input[id='user.active'][value='true']")
    private WebElement userActiveYesButton;

    @FindBy(css = "input[id='user.active'][value='false']")
    private WebElement userActiveNoButton;

    @FindBy(name = "user.active")
    private List<WebElement> userActive;

    @FindBy(id = "user.mobileNumber")
    private WebElement mobileNumberTextBox;

    @FindBy(id = "user.emailId")
    private WebElement emailIdTextBox;

    @FindBy(id = "user.fatherOrHusbandName")
    private WebElement fatherOrHusbandName;

    @FindBy(id = "placeOfBirth")
    private WebElement birthPlaceTextBox;

    @FindBy(id = "user.bloodGroup")
    private WebElement userBloodGroupBox;

    @FindBy(id = "motherTounge")
    private WebElement motherToungeBox;

    @FindBy(id = "religion")
    private WebElement religionBox;

    @FindBy(id = "community")
    private WebElement communityBox;

    @FindBy(id = "category")
    private WebElement categoryBox;

    @FindBy(css = "input[id='physicallyDisabled'][value='true']")
    private WebElement physicallyDisabledYesButton;

    @FindBy(css = "input[id='physicallyDisabled'][value='false']")
    private WebElement physicallyDisabledNoButton;

    @FindBy(css = "input[id='medicalReportProduced'][value='true']")
    private WebElement medicalReportAvailbleYesButton;

    @FindBy(css = "input[id='medicalReportProduced'][value='false']")
    private WebElement medicalReportAvailbleNoButton;

    @FindBy(id = "user.identificationMark")
    private WebElement identificationMarkTextBox;

    @FindBy(id = "user.pan")
    private WebElement panNumberTextBox;

    @FindBy(id = "passportNo")
    private WebElement passportNoTextBox;

    @FindBy(id = "gpfNo")
    private WebElement gpfNoTextBox;

    @FindBy(id = "user.aadhaarNumber")
    private WebElement aadhaarNumberTextBox;

    @FindBy(id = "bank")
    private WebElement bankBox;

    @FindBy(id = "bankBranch")
    private WebElement bankBranchBox;

    @FindBy(id = "bankAccount")
    private WebElement bankAccountNumberTextBox;

    @FindBy(id = "user.altContactNumber")
    private WebElement altContactNumberTextBox;

    @FindBy(id = "user.permanentAddress")
    private WebElement permanentAddressTextBox;

    @FindBy(id = "user.permanentCity")
    private WebElement permanentCityTextBox;

    @FindBy(id = "user.permanentPincode")
    private WebElement permanentPincodeTextBox;

    @FindBy(id = "user.correspondenceAddress")
    private WebElement correspondenceAddressTextBox;

    @FindBy(id = "user.correspondenceCity")
    private WebElement correspondenceCityTextBox;

    @FindBy(id = "user.correspondencePincode")
    private WebElement correspondencePincodeTextBox;

    @FindBy(id = "languagesKnown")
    private WebElement languagesKnownBox;

    @FindBy(id = "recruitmentMode")
    private WebElement recruitmentModeBox;

    @FindBy(id = "recruitmentType")
    private WebElement recruitmentTypeBox;

    @FindBy(id = "recruitmentQuota")
    private WebElement recruitmentQuotaBox;

    @FindBy(id = "dateOfAppointment")
    private WebElement dateOfAppointmentTextBox;

    @FindBy(id = "dateOfJoining")
    private WebElement dateOfJoiningTextBox;

    @FindBy(id = "retirementAge")
    private WebElement retirementAgeTextBox;

    @FindBy(id = "dateOfRetirement")
    private WebElement dateOfRetirementTextBox;

    @FindBy(id = "dateOfTermination")
    private WebElement dateOfTerminationTextBox;

    @FindBy(id = "dateOfResignation")
    private WebElement dateOfResignationTextBox;

    @FindBy(id = "user.photo")
    private WebElement choosePhoto;

    @FindBy(id = "user.signature")
    private WebElement chooseSignature;

    @FindBy(id = "documents")
    private WebElement chooseDocuments;

    @FindBy(id = "user.name-error")
    private WebElement userNameErrorMessage;

    @FindBy(id = "code-error")
    private WebElement codeErrorMessage;

    @FindBy(id = "user.pan-error")
    private WebElement panNumberError;

    @FindBy(id = "gpfNo-error")
    private WebElement gpfNumberError;

    @FindBy(id = "bankAccount-error")
    private WebElement bankAccountError;

    @FindBy(id = "user.permanentAddress-error")
    private WebElement permanentAddressError;

    @FindBy(id = "user.permanentCity-error")
    private WebElement permanentCityError;

    public EmployeeDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    public String enterEmployeeDetails(EmployeeDetails employeeDetails) {

        await().atMost(60, TimeUnit.SECONDS).until(() -> driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0);

        if(!(driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0)){
            driver.navigate().refresh();
            await().atMost(20, TimeUnit.SECONDS).until(() -> driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0);
        }

//        enterText(employeeNameTextBox, "TestUser ", driver);
        enterText(employeeNameTextBox, "TestUser " + getRandomUpperCaseCharacters(5), driver);
        String employeeCode = "KEMP" + get6DigitRandomInt();
        enterText(employeeCodeTextBox, employeeCode, driver);
        selectFromDropDown(employeeTypeBox, employeeDetails.getEmployeeType(), driver);
        selectFromDropDown(employeeStatusBox, employeeDetails.getStatus(), driver);
//        selectFromDropDown(employeeGroupBox, "Central", driver);
        enterText(dobTextBox, employeeDetails.getDateOfBirth(), driver);
        if (employeeDetails.getGender().equals("Male")) {
            selectFromDropDown(genderDropdown, "Male", driver);
        } else {
            selectFromDropDown(genderDropdown, "Female", driver);
        }
        selectFromDropDown(maritalStatusBox, employeeDetails.getMaritalStatus(), driver);
        enterText(userNameTextBox, employeeCode, driver);

        if (employeeDetails.getIsUserActive().equals("Yes")) {
            userActive.get(0).isSelected();
        } else {
            userActive.get(1).isSelected();
        }
        enterText(emailIdTextBox, "mail@mail.com", driver);
        enterText(fatherOrHusbandName, "Father.Spouse Name", driver);
        enterText(birthPlaceTextBox, "Native/Birth. Place", driver);
        enterText(mobileNumberTextBox, employeeDetails.getMobileNumber(), driver);
        enterText(passportNoTextBox, "IND" + get6DigitRandomInt(), driver);
        enterText(gpfNoTextBox, get6DigitRandomInt(), driver);
        enterText(aadhaarNumberTextBox, "111111" + get6DigitRandomInt(), driver);
        enterText(panNumberTextBox, "ABCDE" + Integer.toString(Integer.parseInt(get6DigitRandomInt()) / 100) + "F", driver);
        enterText(bankAccountNumberTextBox, "10101010101010" + get6DigitRandomInt(), driver);
        enterText(permanentAddressTextBox, employeeDetails.getPermanentAddress(), driver);
        enterText(permanentCityTextBox, employeeDetails.getPermanentCity(), driver);
//        enterText(permanentPincodeTextBox, employeeDetails.getPermanentPincode(), driver);
        enterText(dateOfAppointmentTextBox, employeeDetails.getDateOfAppointment(), driver);

        return employeeCode;
    }

    public void updateEmployeeDetails() {
//        clickOnButton(driver.findElement(By.cssSelector(".btn.btn-default.btn-action")), driver);
        selectFromDropDown(maritalStatusBox, "MARRIED", driver);
    }

    public String checkEmployeeTabFields() {
        await().atMost(60, TimeUnit.SECONDS).until(() -> driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0);

        if(!(driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0)){
            driver.navigate().refresh();
            await().atMost(20, TimeUnit.SECONDS).until(() -> driver.findElements(By.cssSelector("[class='blockUI']")).size() == 0);
        }

        checkField(employeeNameTextBox, userNameErrorMessage, "1234", "TestUser", "Only alphabets and spaces allowed.");
        checkField(employeeNameTextBox, userNameErrorMessage, "@@@@", "TestUser", "Only alphabets and spaces allowed.");
        checkField(employeeNameTextBox, userNameErrorMessage, "Test1234@@@", "TestUser", "Only alphabets and spaces allowed.");
        String employeeCode = "EMP" + get6DigitRandomInt();
        checkField(employeeCodeTextBox, codeErrorMessage, "@@@@", employeeCode, "Only alphanumeric characters allowed.");
        selectFromDropDown(employeeTypeBox, "Outsourced", driver);
        selectFromDropDown(employeeStatusBox, "EMPLOYED", driver);
        enterText(dobTextBox, "17/07/1985", driver);
        selectFromDropDown(genderDropdown, "Male", driver);
        selectFromDropDown(maritalStatusBox, "MARRIED", driver);
        enterText(userNameTextBox, employeeCode, driver);
        checkField(panNumberTextBox, panNumberError, "@@@@", "CPRPK2567K", "Please enter a valid pan.");
        checkField(gpfNoTextBox, gpfNumberError, "@@@@", "ABCD123", "Only alphanumeric characters allowed.");
        checkField(bankAccountNumberTextBox, bankAccountError, "@@@@@@@@@@", "1010101010" + get6DigitRandomInt(), "Only alphanumeric with -/_ allowed.");
        checkField(permanentAddressTextBox, permanentAddressError, "@@@@@@", "Municipal Office Rd, N.R.Peta, Near Appollo Hospital, Kurnool, Andhra Pradesh", "Only alphanumeric with -/_#(),.& allowed.");
        checkField(permanentCityTextBox, permanentCityError, "@@@@@", "kurnool", "Only alphanumeric characters and space allowed.");
        enterText(dateOfAppointmentTextBox, "01/01/2012", driver);

        return employeeCode;
    }

    private void checkField(WebElement element, WebElement errorElement, String wrongData, String correctData, String errorMsg) {
        enterText(element, wrongData, driver);
        element.sendKeys(Keys.TAB);
        jsClick(element, driver);
        element.sendKeys(Keys.TAB);
        if (errorElement.getText().equals(errorMsg)) {
            enterText(element, correctData, driver);
        }
    }
}