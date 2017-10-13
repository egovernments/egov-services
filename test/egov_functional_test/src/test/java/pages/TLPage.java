package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class TLPage extends BasePage {

    private WebDriver driver;

    public TLPage(WebDriver driver) {
        this.driver = driver;
    }

    public void TLLogout() {

        await().atMost(30, TimeUnit.SECONDS).until(()->driver.findElements(By.xpath("//i[text()='more_vert']")).size() == 1);
        while (driver.findElements(By.xpath("//i[text()='more_vert']")).size() == 1) {
            waitForElementToBeVisible(driver.findElement(By.xpath("//i[text()='more_vert']")), driver);
            waitForElementToBeClickable(driver.findElement(By.xpath("//i[text()='more_vert']")), driver);
            driver.findElement(By.xpath("//i[text()='more_vert']")).click();
            driver.findElement(By.xpath(".//div[text()='Sign Out']")).click();
            driver.navigate().refresh();
            try {
                driver.switchTo().alert().accept();
                driver.navigate().refresh();
            } catch (NoAlertPresentException Ex) {
            }
        }

    }
}
