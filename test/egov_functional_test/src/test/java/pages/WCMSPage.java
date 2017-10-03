package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class WCMSPage extends BasePage {

    private WebDriver driver;

    public WCMSPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterDemandAndCollectionDetails() {
        waitForElementToBeVisible(driver.findElement(By.xpath("//*[text()='Applicant Particulars']")), driver);
        List<WebElement> demandRows = driver.findElements(By.cssSelector("tbody tr"));

        for (int k = 1; k <= demandRows.size(); k++) {
            List<WebElement> fields = driver.findElements(By.cssSelector("tbody tr:nth-child(" + k + ") td input"));
            for (int i = 0; i < 1; i++) {
                enterText(fields.get(i), getRandomNumber(2), driver);
                enterText(fields.get(i + 1), getRandomNumber(2), driver);
            }
        }

    }
}
