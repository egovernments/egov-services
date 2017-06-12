package pages.employeeManagement.category;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class CategoryPage extends BasePage {

    @FindBy(id = "name")
    private WebElement categoryNameTextBox;

    @FindBy(id = "description")
    private WebElement categoryDescriptionTextBox;

    @FindBy(id = "active")
    private WebElement activeCheckBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement categorySubmitButton;

    private WebDriver webDriver;

    public CategoryPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterCategoryDetails() {
        enterText(categoryNameTextBox, "name", webDriver);
        enterText(categoryDescriptionTextBox, "Description", webDriver);
        clickOnButton(categorySubmitButton, webDriver);
    }
}
