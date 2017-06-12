package pages.employeeManagement.leaveManagement;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class LeaveOpeningBalancePage extends BasePage {

    @FindBy(css = "[id='employee']")
    private WebElement employeeIdTextBox;

    @FindBy(css = "[id='sub']")
    private WebElement searchButton;

    @FindBy(id = "leaveType")
    private WebElement leaveTypeSelectBox;

    @FindBy(id = "calendarYear")
    private WebElement calenderYearSelectBox;

    @FindBy(name = "noOfDays")
    private WebElement noOfDaysTextBox;

    @FindBy(css = "[type='button'][class='btn btn-submit']")
    private WebElement addButton;

    private WebDriver webDriver;

    public LeaveOpeningBalancePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void searchEmployeeToCreateLeaveOpeningBalance(String employeeCode) {
        enterText(employeeIdTextBox, employeeCode, webDriver);
        selectFromDropDown(leaveTypeSelectBox, "Half Pay Leave", webDriver);
        selectFromDropDown(calenderYearSelectBox, getCurrentDate().replaceAll("/", "-").split("-")[2], webDriver);
        clickOnButton(searchButton, webDriver);
    }

    public void enterDetailsOfLeaveOpeningBalance(String noOfDays) {
        enterText(noOfDaysTextBox, noOfDays, webDriver);
        clickOnButton(addButton, webDriver);
        checkIfLeaveOpeningBalanceIsAddedOrNot();
    }

    private void checkIfLeaveOpeningBalanceIsAddedOrNot() {
        Assert.assertEquals(webDriver.findElement(By.cssSelector("[id='employee']")).getText(), "");
    }
}
