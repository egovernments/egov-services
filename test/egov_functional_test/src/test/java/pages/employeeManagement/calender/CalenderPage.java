package pages.employeeManagement.calender;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class CalenderPage extends BasePage {

    @FindBy(css = "[id='name']")
    private WebElement calenderYearTextBox;

    @FindBy(id = "startDate")
    private WebElement calenderStartDate;

    @FindBy(id = "endDate")
    private WebElement calenderEndDate;

    @FindBy(id = "active")
    private WebElement activeCheckBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement calenderSubmitButton;

    private WebDriver webDriver;

    public CalenderPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterCalenderDetails() {
        enterText(calenderYearTextBox, getCurrentYear(), webDriver);
        enterDate(calenderStartDate, "Start Date", webDriver);
        enterDate(calenderEndDate, "End Date", webDriver);
        clickOnButton(calenderSubmitButton, webDriver);
    }
}
