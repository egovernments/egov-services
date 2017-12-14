package pages;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Properties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.jayway.awaitility.Awaitility.await;
import static java.util.concurrent.TimeUnit.SECONDS;

public class BasePage {

    protected void waitForElementVisibility(By locator, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, Properties.waitTime);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void waitForElementToBeVisible(WebElement element, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, Properties.waitTime);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementToBePresent(By by, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, Properties.waitTime);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    protected boolean isElementPresent(By by, WebDriver driver) {
        return driver.findElements(by).size() > 0;
    }

    protected boolean isElementPresentAndDisplayed(By by, WebDriver driver) {
        if (driver.findElements(by).size() > 0)
            return driver.findElement(by).isDisplayed();
        return false;
    }

    protected void waitForElementToDisappear(By locator, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, Properties.waitTime);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForElementToBeClickable(WebElement element, WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, Properties.waitTime);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForElementsSizeIsGreaterThan0(By by, WebDriver webDriver) {
        try {
            await().atMost(20, TimeUnit.SECONDS).until(() -> webDriver.findElements(by).size() > 0);
        } catch (Exception e) {
            waitForElementToBePresent(by, webDriver);
        }
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected void jsClick(WebElement webElement, WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", webElement);
    }

    protected void jsClickCheckbox(WebElement webElement, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", webElement);
    }

    protected void enterText(WebElement webElement, String value, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        webElement.clear();
        webElement.sendKeys(value);
    }

    protected void enterTextWithoutClearing(WebElement webElement, String value, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        webElement.sendKeys(value);
    }

    protected void selectFromDropDown(WebElement webElement, String value, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        await().atMost(20, SECONDS).until(() -> new Select(webElement).getOptions().size() > 0);
        new Select(webElement).selectByVisibleText(value);
    }

    protected void selectAParticularFromDropDown(WebElement webElement, int i, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        await().atMost(20, SECONDS).until(() -> new Select(webElement).getOptions().size() > 1);
        new Select(webElement).selectByIndex(i);
    }

    protected void clickOnElement(WebElement webElement, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        webElement.click();
    }

    protected String getTextFromWeb(WebElement webElement, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        return webElement.getText();
    }

    protected void uploadFile(WebElement element, String filePath, WebDriver driver) {
        element.sendKeys(filePath);
    }

    protected void enterDate(WebElement webElement, String date, WebDriver driver) {
        waitForElementToBeVisible(webElement, driver);
        waitForElementToBeClickable(webElement, driver);
        webElement.clear();
        webElement.sendKeys(date, Keys.TAB);
    }

    protected void checkDropdownIsLoadedOrNot(WebElement webElement, String text, WebDriver webDriver, String containsText) {
        if (webElement.getText().contains(containsText)) {
            selectFromDropDown(webElement, text, webDriver);
        }
    }

    protected void switchToNewlyOpenedWindow(WebDriver driver) {
        await().atMost(20, SECONDS).until(() -> driver.getWindowHandles().size() > 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    public void switchToPreviouslyOpenedWindow(WebDriver driver) {
        await().atMost(5, SECONDS).until(() -> driver.getWindowHandles().size() == 1);
        for (String winHandle : driver.getWindowHandles()) {
            driver.switchTo().window(winHandle);
        }
    }

    protected String getRandomNumber(int c) {
        Random random = new Random();
        char[] digits = new char[c];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < c; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return new String(digits);
    }

    protected String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected String getRandomEmail() {
        return "email" + String.valueOf(100 + (RandomUtils.nextInt(9999))) + "@xyz.com";
    }

    public String   autoGenerateValue(String value) {
        value = value.replaceAll("\"", "");
        if (value.contains("random") || value.contains("date") || value.contains("year") || value.contains("time")) {
            if (value.contains(",") && value.contains("numbers")) {
                value = (value.split(",")[0] +
                        getRandomNumber(Integer.parseInt(value.split(",")[1].replaceAll("[^0-9]+", ""))));
            } else if (value.contains(",") && value.contains("characters") && value.contains("upper")) {
                value = (value.split(",")[0] +
                        getRandomUpperCaseCharacters(Integer.parseInt(value.split(",")[1].replaceAll("[^0-9]+", ""))));
            } else if (value.contains(",") && value.contains("characters") && value.contains("lower")) {
                value = (value.split(",")[0] +
                        getRandomLowerCaseCharacters(Integer.parseInt(value.split(",")[1].replaceAll("[^0-9]+", ""))));
            } else if (value.contains(",") && value.contains("characters")) {
                value = value.split(",")[0] +
                        getRandomMixedCaseCharacters(Integer.parseInt(value.split(",")[1].replaceAll("[^0-9]+", "")));
            } else if (value.contains("characters") && value.contains("upper")) {
                value = getRandomUpperCaseCharacters(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            } else if (value.contains("characters") && value.contains("lower")) {
                value = getRandomLowerCaseCharacters(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            } else if (value.contains("characters")) {
                value = getRandomMixedCaseCharacters(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            } else if (value.contains("email")) {
                value = getRandomEmail();
            } else if (value.contains("current date")) {
                value = getCurrentDate();
            } else if (value.contains("previous date")) {
                value = getPreviousDate();
            } else if (value.contains("past") && value.contains("date")) {
                value = getPastDate(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            } else if (value.contains("future") && value.contains("date")) {
                value = getFutureDate(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            } else if (value.contains("current year")) {
                value = getCurrentYear();
            } else if (value.contains("current time")) {
                value = getCurrentTime();
            } else if (value.contains("number") || value.contains("numbers")) {
                value = getRandomNumber(Integer.parseInt(value.replaceAll("[^0-9]+", "")));
            }
        }
        return value;
    }

    protected String getCurrentYear() {
        return getCurrentDate().replaceAll("/", "-").split("-")[2];
    }

    protected String getPreviousDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    protected String getFutureDate(int i) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, i);
        return dateFormat.format(cal.getTime());
    }

    protected String getPastDate(int i) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);
        return dateFormat.format(cal.getTime());
    }

    protected String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected String getParticularMonthName(int monthId) {
        DateFormat fmt = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -monthId);
        return fmt.format(cal.getTime());
    }

    protected void maximizeBrowserWindow(WebDriver webDriver) {
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    protected void refreshBrowserWindow(WebDriver webDriver) {
        webDriver.navigate().refresh();
    }

    protected void isSuccesful(String expectedMessage, String actualMessage) {
        Boolean found = Arrays.asList(actualMessage.split("\\ ")).contains(expectedMessage);
        Assert.assertTrue(found);
    }

    protected String getRandomUpperCaseCharacters(int noOfCharacters) {

        Random random = new Random();
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String required = "";

        for (int i = 0; i < noOfCharacters; i++) {
            required = required + alphabet[random.nextInt(26)];
        }
        return required;
    }

    protected String getRandomLowerCaseCharacters(int noOfCharacters) {

        Random random = new Random();
        String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String required = "";

        for (int i = 0; i < noOfCharacters; i++) {
            required = required + alphabet[random.nextInt(26)];
        }
        return required;
    }

    protected String getRandomMixedCaseCharacters(int noOfCharacters) {

        Random random = new Random();
        String[] alphabet = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        String required = "";

        for (int i = 0; i < noOfCharacters; i++) {
            required = required + alphabet[random.nextInt(52)];
        }
        return required;
    }

    protected String getEnvironmentURL() {
        return Properties.url;
    }

    protected String checkValueIsInDateFormatOrNot(String value) throws ParseException {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (value.split("/").length == 3) {
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        }

        return date != null ? value : value;
    }
}