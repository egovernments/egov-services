package pages;

import entities.LoginDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

public class HomePage extends BasePage {
    private WebDriver driver;

    @FindBy(id = "j_username")
    private WebElement userNameTextBox;

    @FindBy(id = "j_password")
    private WebElement passwordTextBox;

    @FindBy(id = "locationId")
    private WebElement locationSelection;

    @FindBy(css = ".form-control.style-form.valid")
    private WebElement zoneSelect;


    @FindBy(xpath = "html/body/div[1]/div/div[2]/header/nav/div[2]/span[1]/a/i")
    private WebElement profileLink;

    @FindBy(linkText = "Sign out")
    private WebElement signOutLink;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void loginAs(LoginDetails loginDetails) {

        enterText(userNameTextBox, loginDetails.getLoginId(), driver);
        enterText(passwordTextBox, loginDetails.getPassword(), driver);
//        if (loginDetails.getHasZone()) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        }
        WebElement signForm = driver.findElement(By.id("signin-action"));
        waitForElementToBeClickable(signForm, driver);
//        clickOnButton(signForm, driver);
        signForm.submit();
    }

    public void visitWebsite() {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void signOut() {
        clickOnButton(profileLink, driver);
//        clickOnButton(profileLink, driver);
//        clickOnButton(signOutLink, driver);
    }
}
