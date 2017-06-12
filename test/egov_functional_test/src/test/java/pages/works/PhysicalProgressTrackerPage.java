package pages.works;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

public class PhysicalProgressTrackerPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "btnsearch")
    private WebElement searchEstimateButton;

    @FindBy(xpath = "//*[@id='resultTable']/tbody/tr[1]/td[6]/a")
    private WebElement uploadPhotoLink;

    @FindBy(css = "#before")
    private WebElement browseFile1;

    @FindBy(css = "#during")
    private WebElement browseFile2;

    @FindBy(css = "#after")
    private WebElement browseFile3;

    @FindBy(linkText = "Close")
    private WebElement closeLink;

    public PhysicalProgressTrackerPage(WebDriver driver) {
        this.driver = driver;
    }

    public void searchEstimate() {
        clickOnButton(searchEstimateButton, driver);
        clickOnButton(uploadPhotoLink, driver);
        switchToNewlyOpenedWindow(driver);
    }

    public void uploadEstimatePhotos() {
        browseFile1.sendKeys(System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg");
        browseFile2.sendKeys(System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg");
        browseFile3.sendKeys(System.getProperty("user.dir") + "/src/test/resources/dataFiles/logo.jpg");
    }

    public void close() {
        clickOnButton(closeLink, driver);

        for (String winHandle : driver.getWindowHandles()) {
            if (driver.switchTo().window(winHandle).getCurrentUrl().equals(getEnvironmentURL() + "/egworks/lineestimate/searchlineestimateform")) {
                break;
            }
        }
        clickOnButton(closeLink, driver);
        switchToPreviouslyOpenedWindow(driver);
    }
}
