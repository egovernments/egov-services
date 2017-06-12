package pages.employeeManagement.grade;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class GradePage extends BasePage {

    @FindBy(css = "[name='name']")
    private WebElement gradeNameTextBox;

    @FindBy(css = "[name='description']")
    private WebElement gradeDescriptionTextBox;

    @FindBy(id = "orderNo")
    private WebElement gradeOrderNumberTextBox;

    @FindBy(id = "active")
    private WebElement activeCheckBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement gradeSubmitButton;

    private WebDriver webDriver;

    public GradePage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterGradeDetails() {
        enterText(gradeNameTextBox, "name", webDriver);
        enterText(gradeDescriptionTextBox, "description", webDriver);
        enterText(gradeOrderNumberTextBox, "1234", webDriver);
        clickOnButton(gradeSubmitButton, webDriver);
    }
}
