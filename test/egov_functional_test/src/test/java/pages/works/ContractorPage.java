package pages.works;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

public class ContractorPage extends BasePage {
    private WebDriver driver;

    @FindBy(id = "code")
    private WebElement contractorCode;

    @FindBy(id = "name")
    private WebElement contractorName;

    @FindBy(className = "yui-dt-dropdown")
    private WebElement department;

    @FindBy(id = "statusDescyui-rec0")
    private WebElement status;

    @FindBy(name = "actionContractorDetails[0].validity.startDate")
    private WebElement fromDate;

    @FindBy(id = "saveButton")
    private WebElement saveButton;

    @FindBy(xpath = "//*[@id='department']")
    private WebElement contractorDepartment;

    @FindBy(id = "status")
    private WebElement contractorStatus;

    @FindBy(id = "searchButton")
    private WebElement contractorSearchButton;

    @FindBy(css = "input[id='closeButton'][value='Close']")
    private WebElement closeButton;

    @FindBy(id = "contractorName")
    private WebElement searchContractorNameBox;

    @FindBy(id = "currentRow")
    private WebElement contractorsListTable;

    @FindBy(css = "input[value='Modify'][type='submit']")
    private WebElement modifyButton;

    @FindBy(css = "input[type='text'][name='contactPerson']")
    private WebElement modifyNameBox;

    @FindBy(xpath = ".//*[@id='msgsDiv']/ul/li/span")
    private WebElement creationMsg;

    @FindBy(id = "correspondenceAddress")
    private WebElement correspondenceAddress;

    @FindBy(id = "panNumber")
    private WebElement panNumber;

    @FindBy(id = "bank")
    private WebElement bankAction;

    @FindBy(id = "ifscCode")
    private WebElement ifscCode;

    @FindBy(xpath = "//*[@id='exemptionForm']")
    private WebElement exemptionFormAction;

    @FindBy(id = "contactPerson")
    private WebElement contactPerson;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "mobileNumber")
    private WebElement mobileNumber;

    @FindBy(id = "bankAccount")
    private WebElement bankAccount;

    @FindBy(id = "tinNumber")
    private WebElement tinNumber;

    @FindBy(id = "gradeNameyui-rec0")
    private WebElement contractorClass;

    @FindBy(id = "registrationNumberyui-rec0")
    private WebElement registrationNumber;

    @FindBy(xpath = ".//*[@id='yui-rec0']/td[8]/div/select")
    private WebElement categoryDropDownBox;

    public ContractorPage(WebDriver driver) {
        this.driver = driver;
    }

    public String entersContractorMasterData() {
        String Name = "KMC" + get6DigitRandomInt();
        enterText(contractorName, Name, driver);
        enterText(correspondenceAddress, "A P State Agro industries Development Corporation Ltd ATP", driver);
        enterText(panNumber, "PANUM1234P", driver);
        enterText(contactPerson, Name, driver);
        waitForElementToBeClickable(email, driver);
        enterText(email, Name + "@egov.org", driver);
        enterText(mobileNumber, "9988" + get6DigitRandomInt(), driver);
        enterText(tinNumber, get6DigitRandomInt(), driver);
        selectFromDropDown(bankAction, "STATE BANK OF MYSORE", driver);
        enterText(ifscCode, "IFSC" + get6DigitRandomInt(), driver);
        enterText(bankAccount, get6DigitRandomInt(), driver);
        selectFromDropDown(exemptionFormAction, "EARNEST MONEY DEPOSIT", driver);
        selectFromDropDown(department, "ENGINEERING", driver);
        enterDate(registrationNumber, "0123" + get6DigitRandomInt(), driver);
        selectFromDropDown(categoryDropDownBox, "Transport", driver);
        selectFromDropDown(contractorClass, "Class-I", driver);
        selectFromDropDown(status, "Active", driver);
        enterDate(fromDate, getCurrentDate(), driver);
        clickOnButton(saveButton, driver);
        return Name;
    }

    public void searchContractor(String name) {
        enterText(searchContractorNameBox, name, driver);
        clickOnButton(contractorSearchButton, driver);
    }

    public void close() {
        clickOnButton(closeButton, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public void select() {
        waitForElementToBeVisible(contractorsListTable, driver);
        List<WebElement> totalRows = contractorsListTable.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));
        System.out.println("Rows:" + totalRows.size());
        WebElement requiredRow = totalRows.get(totalRows.size() - 1);
        WebElement element = requiredRow.findElements(By.tagName("td")).get(0).findElement(By.id("radio"));
        jsClick(element, driver);
        clickOnButton(modifyButton, driver);
    }

    public void modify() {
        clickOnButton(modifyNameBox, driver);
        modifyNameBox.click();
        modifyNameBox.clear();
        enterText(modifyNameBox, "Testing", driver);
        clickOnButton(modifyButton, driver);
    }

    public String successMessage() {
        String msg = getTextFromWeb(creationMsg, driver);
        return msg;
    }
}
