package pages.employeeManagement.holiday;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class HolidayPage extends BasePage {

    @FindBy(css = "select[id='name']")
    private WebElement holidayYearSelectBox;

    @FindBy(css = "[name='applicableOn']")
    private WebElement holidayDateBox;

    @FindBy(css = "input[name='name']")
    private WebElement holidayNameTextBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement holidaySubmitButton;

    private WebDriver webDriver;

    public HolidayPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterHolidayDetails() {
        selectFromDropDown(holidayYearSelectBox, getCurrentYear(), webDriver);
        enterDate(holidayDateBox, "Date", webDriver);
        enterText(holidayNameTextBox, "Name", webDriver);
        clickOnButton(holidaySubmitButton, webDriver);
    }
}
