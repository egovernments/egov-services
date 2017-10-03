package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

public class PTISPage extends BasePage {

    private WebDriver driver;

    public PTISPage(WebDriver driver) {
        this.driver = driver;
    }

    public void enterDemandAndCollectionDetails() {
        waitForElementToBeVisible(driver.findElement(By.xpath("//*[text()='Add Demand']")), driver);
        List<WebElement> demandRows = driver.findElements(By.cssSelector("tbody tr"));

        for (int k = 1; k < demandRows.size(); k++) {
            List<WebElement> fields = driver.findElements(By.cssSelector("tbody tr:nth-child(" + (k + 1) + ") td input"));
            for (int i = 0; i < fields.size() / 2; i++) {
                enterText(fields.get(i), getRandomNumber(2), driver);
                enterText(fields.get(i + fields.size() / 2), fields.get(i).getAttribute("value"), driver);
            }
        }
    }
}
