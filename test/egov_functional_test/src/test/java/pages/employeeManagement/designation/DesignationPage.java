package pages.employeeManagement.designation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class DesignationPage extends BasePage {

    @FindBy(css = "[name='name']")
    private WebElement designationNameTextBox;

    @FindBy(css = "[name='code']")
    private WebElement desginationCodeTextBox;

    @FindBy(id = "description")
    private WebElement designationDescriptionTextBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement designationSubmitButton;

    private WebDriver webDriver;

    public DesignationPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterDesignationDetails() {
        enterText(designationNameTextBox, "name", webDriver);
        enterText(desginationCodeTextBox, "code", webDriver);
        enterText(designationDescriptionTextBox, "Designation Description", webDriver);
        clickOnButton(designationSubmitButton, webDriver);
    }
}