package pages.employeeManagement.createAndUpdateAttendance;

import entities.employeeManagement.createAttendance.AllEmployeeCode;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

public class CreateAndUpdateAttendancePage extends BasePage {

    @FindBy(css = "[id='year']")
    private WebElement yearSelectbox;

    @FindBy(css = "[id='month']")
    private WebElement monthSelectBox;

    @FindBy(css = "[name='code']")
    private WebElement employeeCodeTextBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement submitButton;

    @FindBy(css = "[id='attendanceTableBody'] tr td")
    private List<WebElement> employeeAttendanceDateTextBox;

    @FindBy(id = "addEmployee")
    private WebElement saveAttendanceButton;

    private WebDriver webDriver;

    public CreateAndUpdateAttendancePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterEmployeeDetailsToCreateAttendance(AllEmployeeCode allEmployeeCode) {
        selectFromDropDown(yearSelectbox, getCurrentDate().replaceAll("/", "-").split("-")[2], webDriver);
        selectFromDropDown(monthSelectBox, getParticularMonthName(1), webDriver);
        enterText(employeeCodeTextBox, allEmployeeCode.getEmployeeCode(), webDriver);
        clickOnButton(submitButton, webDriver);
        switchToNewlyOpenedWindow(webDriver);
    }

    public void enterAttendanceDetails(String status) {
        maximizeBrowserWindow(webDriver);

        boolean isHoliday = checkHolidayOrNot(getCurrentDate());

        int date = Integer.parseInt(getCurrentDate().replaceAll("/", "-").split("-")[0]);

        if (status.contains("present") && !isHoliday) {
            enterTextWithoutClearing(employeeAttendanceDateTextBox.get(++date).findElement(By.tagName("input"))
                    , "P", webDriver);
        } else if (status.contains("absent") && !isHoliday) {
            enterTextWithoutClearing(employeeAttendanceDateTextBox.get(++date).findElement(By.tagName("input"))
                    , "A", webDriver);
        } else if (isHoliday) {
            enterTextWithoutClearing(employeeAttendanceDateTextBox.get(++date).findElement(By.tagName("input"))
                    , "H", webDriver);
        } else {
            enterTextWithoutClearing(employeeAttendanceDateTextBox.get(++date).findElement(By.tagName("input"))
                    , "A", webDriver);
        }

        clickOnButton(saveAttendanceButton, webDriver);
    }

    private boolean checkHolidayOrNot(String currentDate) {

        if (currentDate.contains("26/01/17")) return true;
        else if (currentDate.contains("29/03/17")) return true;
        else if (currentDate.contains("14/04/17")) return true;
        else if (currentDate.contains("01/05/17")) return true;
        else if (currentDate.contains("26/06/17")) return true;
        else if (currentDate.contains("15/08/17")) return true;
        else if (currentDate.contains("25/08/17")) return true;
        else if (currentDate.contains("29/09/17")) return true;
        else if (currentDate.contains("02/10/17")) return true;
        else if (currentDate.contains("20/10/17")) return true;
        else if (currentDate.contains("01/11/17")) return true;
        else if (currentDate.contains("25/12/17")) return true;

        return false;
    }
}
