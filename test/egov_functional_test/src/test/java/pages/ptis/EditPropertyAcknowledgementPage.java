package pages.ptis;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.BasePage;

import java.util.List;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class EditPropertyAcknowledgementPage extends BasePage {

    private WebDriver driver;

    @FindBy(id = "button2")
    private WebElement closeButton;

    public void close() {
        clickOnButton(closeButton, driver);
        await().atMost(5, SECONDS).until(() -> driver.getWindowHandles().size() == 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public String getApplicationNumber() {
        List<WebElement> elements = driver.findElement(By.tagName("table")).findElement(By.tagName("tbody"))
                .findElement(By.tagName("tr")).findElement(By.tagName("td")).findElements(By.tagName("span"));
        return elements.get(1).getText();
    }
}


