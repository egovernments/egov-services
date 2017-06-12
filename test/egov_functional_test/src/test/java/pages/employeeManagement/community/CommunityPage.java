package pages.employeeManagement.community;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class CommunityPage extends BasePage {

    @FindBy(id = "name")
    private WebElement communityNameTextBox;

    @FindBy(id = "description")
    private WebElement communityDescriptionTextBox;

    @FindBy(id = "active")
    private WebElement activeCheckBox;

    @FindBy(css = ".btn.btn-submit")
    private WebElement communitySubmitButton;

    private WebDriver webDriver;

    public CommunityPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void enterCommunityDetails() {
        enterText(communityNameTextBox, "name", webDriver);
        enterText(communityDescriptionTextBox, "Description", webDriver);
        clickOnButton(communitySubmitButton, webDriver);
    }
}
