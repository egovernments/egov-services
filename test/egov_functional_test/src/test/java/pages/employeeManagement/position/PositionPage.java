package pages.employeeManagement.position;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class PositionPage extends BasePage {

    @FindBy(css = "[id='department']")
    private WebElement positionDepartmentSelectBox;

    @FindBy(css = "[id='designation']")
    private WebElement positionDesignationSelectBox;

    @FindBy(css = "[name='name']")
    private WebElement positionNameTextBox;

    @FindBy(id = "active")
    private WebElement activeCheckBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement positionSubmitButton;

    private WebDriver webDriver;

    public PositionPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterPositionDetails() {
        selectFromDropDown(positionDepartmentSelectBox, "Department", webDriver);
        selectFromDropDown(positionDesignationSelectBox, "Designation", webDriver);
        enterText(positionNameTextBox, "Name", webDriver);
        clickOnButton(positionSubmitButton, webDriver);
    }
}
