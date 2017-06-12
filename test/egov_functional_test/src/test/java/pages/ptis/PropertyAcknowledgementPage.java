package pages.ptis;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class PropertyAcknowledgementPage extends BasePage {

    private WebDriver driver;

    @FindBy(css = "input[value='Close'][type='button']")
    private WebElement closeButton;

    @FindBy(linkText = "Close")
    private WebElement closeLink;

    @FindBy(id = "buttonClose")
    private WebElement propertyCloseButton;

    @FindBy(id = "view")
    private WebElement assessmentViewButton;

    @FindBy(className = "btn btn-default")
    private WebElement assessmentCloseButton;

    public PropertyAcknowledgementPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getAcknowledgementMessage() {
        return driver.findElement(By.xpath("//table/tbody/tr/td")).getText();
    }

    public void close() {

        try {
            clickOnButton(closeButton, driver);
        } catch (StaleElementReferenceException e) {
            WebElement element = driver.findElement(By.cssSelector("input[value='Close'][type='button']"));
//            WebElement element = driver.findElement(By.cssSelector("a[class='btn btn-default']"));
            waitForElementToBeClickable(element, driver);
            element.click();
        }
        await().atMost(5, SECONDS).until(() -> driver.getWindowHandles().size() == 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }

    }

    public void close1() {
        clickOnButton(propertyCloseButton, driver);
        await().atMost(5, SECONDS).until(() -> driver.getWindowHandles().size() == 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public String getSignatureNotification() {

        return driver.findElement(By.cssSelector("div.panel-title")).getText();
    }

    public void closeFromCommisionersLogin() {
        clickOnButton(closeLink, driver);
        await().atMost(5, SECONDS).until(() -> driver.getWindowHandles().size() == 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void cancelPrint() {

        driver.findElement(By.className("cancel")).click();
    }

    public void toViewSubmissionPage() {
        clickOnButton(assessmentViewButton, driver);
    }

    public void toCloseDataEntryPage() {
        clickOnButton(assessmentCloseButton, driver);
    }

    public void toCloseAdditionalConnectionPage() {
        jsClick(closeLink, driver);
        switchToPreviouslyOpenedWindow(driver);
    }

    public String getAssessmentNumber() {
        List<WebElement> elements = driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr")).findElement(By.tagName("td")).findElements(By.tagName("span"));
        return elements.get(1).getText();
    }

    public String getAssessmentNumberNew(String type) {

        WebElement element;
        if (type.equals("title")) {
            element = driver.findElement(By.xpath(".//*[@id='save']/div[1]/table/tbody/tr[1]/td[2]"));
        } else {
            element = driver.findElement(By.xpath(".//*[@id='" + type + "']/div[1]/table/tbody/tr[1]/td"));
        }

        return element.getText();
    }
}
