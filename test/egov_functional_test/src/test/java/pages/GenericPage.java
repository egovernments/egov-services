package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import steps.GenericSteps;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;

public class GenericPage extends BasePage {

    private WebDriver driver;
    private ConstructElement constructElement;

    public GenericPage(WebDriver driver) {
        this.driver = driver;
        constructElement = new ConstructElement(driver);
    }

    public void buildElementForAction(String screen, String action, String element, String value) throws Exception {
        if (!value.equals("NA")) {
            value = findValueIsComingFromDataTable(value);
            if (GenericSteps.copyValues.containsKey(value))
                value = GenericSteps.copyValues.get(value);
            else
                value = autoGenerateValue(value);
            value = checkValueIsInDateFormatOrNot(value);
            WebElement webElement = constructElement.buildElement(screen, element, value);

            try {
                performActionOnElement(webElement, action, value);
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
                performActionOnElement(webElement, action, value);
            }
        }
    }

    private void performActionOnElement(WebElement webElement, String action, String value) throws InterruptedException {
        switch (action) {

            case "selects":
            case "select":
                clickOnDropdown(webElement, value);
                break;

            case "dropdown":
                selectFromDropDown(webElement, value, driver);
                break;

            case "types":
            case "type":
                try {
                    enterText(webElement, value, driver);
                } catch (Exception e) {
                    enterTextWithoutClearing(webElement, value, driver);
                }
                break;

            case "uploads":
            case "upload":
                uploadFile(webElement, System.getProperty("user.dir") + "/src/test/resources/" + value, driver);
                break;

            case "clicks":
            case "click":
                clickOnElement(webElement, driver);
                break;

            case "double clicks":
            case "double click":
            case "doubleclick":
            case "doubleclicks":
                clickOnElement(webElement, driver);
                break;

            case "force clicks":
            case "force click":
            case "forceclicks":
            case "forceClicks":
            case "forceclick":
            case "forceClick":
                jsClick(webElement, driver);
                break;

            case "visible":
                waitForElementToBeVisible(webElement, driver);
                Assert.assertTrue("Text not present ", webElement.getText().contains(value));
                break;

            case "notVisible":
                waitForElementToBeVisible(webElement, driver);
                Assert.assertFalse("Text is present ", webElement.getText().contains(value));
                break;

            case "enabled":
                waitForElementToBeVisible(webElement, driver);
                Assert.assertEquals("Element not enabled ", true, webElement.isEnabled());
                break;

            case "isNotEnabled":
                waitForElementToBeVisible(webElement, driver);
                Assert.assertEquals("Element is enabled ", false, webElement.isEnabled());
                break;

        }
    }

    private void clickOnDropdown(WebElement webElement, String value) throws InterruptedException {

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", webElement);
        try {
            clickOnElement(webElement, driver);
        } catch (Exception e) {
            TimeUnit.SECONDS.sleep(1);
            clickOnElement(webElement, driver);
        }
        await().atMost(10, TimeUnit.SECONDS).until(() -> driver.findElements(By.cssSelector("div[role=\"presentation\"]:nth-child(1) div div span div div div")).size() >= 1);

        List<WebElement> dropdown = driver.findElements(By.cssSelector("div[role=\"presentation\"]:nth-child(1) div div span div div div"));
        for (WebElement w : dropdown) {
            if (w.getText().equals(value)) {
                try {
                    clickOnElement(w, driver);
                    break;
                } catch (Exception e) {
                    jsClick(w, driver);
                }
            }
        }
    }

    private String findValueIsComingFromDataTable(String value) {
        if (value.contains("<"))
            return GenericSteps.dataTableStore.get(GenericSteps.i++);
        return value;
    }

    public void actionOnSuggestionBox(WebElement webElement, String value) throws InterruptedException {
        waitForElementToBeVisible(webElement, driver);
        webElement.sendKeys(value);
        clickOnDropdown(webElement, value);
        webElement.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
    }

    public boolean checkValidDataEnteredOrNot(String inValidMessage) {
        String e = "//*[text()='" + inValidMessage + "']";
        await().atMost(20, TimeUnit.SECONDS).until(() -> driver.findElements(By.xpath(e)).size() == 1);
        return driver.findElements(By.xpath(e)).size() == 1;
    }

    public WebElement openApplication(String number) throws IOException {

        List<WebElement> totalRows;
        totalRows = constructElement.buildElement("Home", "dashBoardApplications", "").findElements(By.tagName("tr"));
        try {
            for (WebElement applicationRow : totalRows) {
                if (applicationRow.findElements(By.tagName("td")).get(4).getText().contains(number)) {
                    return applicationRow;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new RuntimeException("No application row found in Inbox -- " + number);
    }
}
